package com.example.workout_logger_presentation.start_workout

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.example.workout_logger_presentation.start_workout.components.TimerExpiredReceiver
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.CompletedWorkout
import com.hbaez.user_auth_presentation.model.WorkoutTemplate
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.Duration
import java.util.Date

@HiltViewModel
class StartWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences,
    private val storageService: StorageService,
    private val startWorkoutUseCases: ExerciseTrackerUseCases,
    logService: LogService
): AuthViewModel(logService) {

    var state by mutableStateOf(StartWorkoutState())
        private set

    var currentTime by mutableStateOf(state.remainingTime)
        private set
    var workoutIds: List<String>

    val workoutTemplates = storageService.workouts

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null
    private val workoutId: Int

    init {
        workoutId = savedStateHandle["workoutId"] ?: -1
        workoutIds = (savedStateHandle["workoutIds"] ?: "").trim('[').trim(']').replace(" ","").split(',').toList()
        Log.println(Log.DEBUG, "workoutids viewmodel", workoutIds.toString())
    }

    fun onEvent(event: StartWorkoutEvent) {
        when(event) {
            is StartWorkoutEvent.OnUpdateWorkoutName -> {
                state = state.copy(
                    workoutName = event.workoutName
                )
            }
            is StartWorkoutEvent.OnRepsChange -> {
                Log.println(Log.DEBUG, "on reps change", event.reps)
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.reps.toMutableList()
                            tmp[event.index] = event.reps
                            it.copy(reps = tmp)
                        } else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }
            is StartWorkoutEvent.AddLoggerList -> {
                val tmp = state.loggerListStates
                tmp.add(event.loggerListState)
                state = state.copy(
                    loggerListStates = tmp
                )
                Log.println(Log.DEBUG, "loggerlist add item vm", state.loggerListStates.size.toString())
            }
            is StartWorkoutEvent.OnWeightChange -> {
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.toList().map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.weight.toMutableList()
                            tmp[event.index]=event.weight
                            it.copy(weight = tmp.toList())
                        }else {
                            counter++
                            it
                        }
                    }.toMutableList()
                )
            }
            is StartWorkoutEvent.OnCheckboxChange -> {
                if(state.currRunningIndex != event.currRunningIndex){
                    state = state.copy(
                        startTime = Date()
                    )
                }
                Log.println(Log.DEBUG, "loggerliststates size", state.loggerListStates.size.toString())
                var counter = 0
                state = state.copy(
                    loggerListStates = state.loggerListStates.toList().map {
                        if(counter == event.page){
                            counter++
                            val tmp = it.isCompleted.toMutableList()
                            tmp[event.index] = event.isChecked
                            it.copy(isCompleted = tmp, timerStatus = TimerStatus.RUNNING)
                        } else {
                            counter++
                            it
                        }
                    }.toMutableList(),
                    timerStatus = event.timerStatus,
                    pagerIndex = event.page,
                    timeDuration = Duration.ofSeconds(state.loggerListStates[event.page].rest[event.index].toLong()),
                    currRunningIndex = event.currRunningIndex,
                    currRunningId = event.rowId
                )
                currentTime = if(event.isChecked && event.timerStatus == TimerStatus.RUNNING) { state.timeDuration.seconds * 1000L } else currentTime
            }
            is StartWorkoutEvent.ChangeRemainingTime -> {
                val diff = Date().time - state.startTime.time
                currentTime = state.timeDuration.toMillis() - diff
            }
            is StartWorkoutEvent.UpdateRemainingTime -> {
                currentTime = currentTime
            }
            is StartWorkoutEvent.OnChangePage -> {
                if(state.timerStatus != TimerStatus.RUNNING){
                    state = state.copy(
                        remainingTime = state.loggerListStates[event.currentPage].rest[0].toLong(),
                        timeDuration = Duration.ofSeconds(state.loggerListStates[event.currentPage].rest[0].toLong()),
                        pagerIndex = event.currentPage
                    )
                }
            }
            is StartWorkoutEvent.TimerFinished -> {
                Log.println(Log.DEBUG, "!!!! current page", state.pagerIndex.toString())
                state = state.copy(
                    timerStatus = TimerStatus.FINISHED,
                    currRunningIndex = -1,
                    currRunningId = -1
                )
            }
            is StartWorkoutEvent.ChangeCheckboxColor -> {
                state = state.copy(
                    loggerListStates = state.loggerListStates.toList().map {
                        if(it.id == event.id){
                            val tmp = it.checkedColor.toMutableList()
                            tmp[event.index] = event.color
                            it.copy(checkedColor = tmp)
                        } else it
                    }.toMutableList()
                )
            }
            is StartWorkoutEvent.OnSubmitWorkout -> {
                run breaking@{
                    if(state.timerStatus == TimerStatus.RUNNING){
                        return@breaking
                    }
                    var counter = 0
                    event.trackableExercises.forEach{// for each exercise
                        val repsList = mutableListOf<String>()
                        val weightList = mutableListOf<String>()
                        val isCompletedList = mutableListOf<String>()
                        it.isCompleted.forEachIndexed { index, b ->  // for each set in exercise
                            if(b){
                                isCompletedList.add("true")
                                if(it.reps[index].isBlank()){
                                    Log.println(Log.DEBUG, "it(loggerlist) exercisename", it.exerciseName)
                                    event.workoutTemplates.forEach { workoutTemplate ->
                                        Log.println(Log.DEBUG, "workouttemplate exercisename", workoutTemplate.exerciseName)
                                        Log.println(Log.DEBUG, "if statement", workoutTemplate.name)
                                        Log.println(Log.DEBUG, "if statement", state.workoutName)
                                        Log.println(Log.DEBUG, "if statement", workoutTemplate.exerciseName)
                                        Log.println(Log.DEBUG, "if statement", it.exerciseName)
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            repsList.add(workoutTemplate.reps[index])
                                            Log.println(Log.DEBUG, "reached inside", repsList.toString())
                                        }
                                    }
                                } else repsList.add(it.reps[index])
                                if(it.weight[index].isBlank()){
                                    event.workoutTemplates.forEach {workoutTemplate ->
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            weightList.add(workoutTemplate.weight[index])
                                        }
                                    }
                                } else weightList.add(it.weight[index])
                            } else {
                                isCompletedList.add("false")
                                if(it.reps[index].isBlank()){
                                    event.workoutTemplates.forEach {workoutTemplate ->
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            repsList.add(workoutTemplate.reps[index])
                                        }
                                    }
                                } else repsList.add(it.reps[index])
                                if(it.weight[index].isBlank()){
                                    event.workoutTemplates.forEach {workoutTemplate ->
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            weightList.add(workoutTemplate.weight[index])
                                        }
                                    }
                                } else weightList.add(it.weight[index])
                            }
                        }
//                        Log.println(Log.DEBUG, "lists", repsList.toString())
//                        Log.println(Log.DEBUG, "lists", weightList.toString())
//                        Log.println(Log.DEBUG, "lists", isCompletedList.toString())
//                        Log.println(Log.DEBUG, "workout templates 0", event.workoutTemplates[0].toString())
//                        Log.println(Log.DEBUG, "workout templates 1", event.workoutTemplates[1].toString())
//                        Log.println(Log.DEBUG, "workout templates 2", event.workoutTemplates[2].toString())
//                        Log.println(Log.DEBUG, "workout templates 3", event.workoutTemplates[3].toString())
//                        Log.println(Log.DEBUG, "workout templates 4", event.workoutTemplates[4].toString())
                        if(repsList.isNotEmpty() && weightList.isNotEmpty()){
                            trackCompletedWorkout(it, repsList, weightList, isCompletedList.toList(), event.dayOfMonth, event.month, event.year)
                        }
                        counter++
                    }
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.NavigateUp)
                    }
                }
            }

            is StartWorkoutEvent.GetExerciseInfo -> {
                getExerciseByName(event.exerciseName)
            }

            is StartWorkoutEvent.OnToggleExerciseDescription -> {
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.map {
                        if(it.exercise.id == event.trackableExerciseState.exercise.id){
                            it.copy(isDescrExpanded = !it.isDescrExpanded)
                        } else it
                    }
                )
            }
        }
    }

    private fun trackCompletedWorkout(loggerListState: LoggerListState, repsList: List<String>, weightList: List<String>, isCompletedList: List<String>, dayOfMonth: Int, month: Int, year: Int){
        viewModelScope.launch {
            val date = "${year}-${month.toString().padStart(2,'0')}-${dayOfMonth.toString().padStart(2, '0')}"
            storageService.saveCompletedWorkout(
                CompletedWorkout(
                    workoutName = state.workoutName,
                    workoutId = workoutId,
                    exerciseName = loggerListState.exerciseName,
                    exerciseId = loggerListState.exerciseId,
                    sets = loggerListState.sets.toInt(),
                    rest = loggerListState.rest,
                    reps = repsList,
                    weight = weightList,
                    isCompleted = isCompletedList,
                    dayOfMonth = dayOfMonth,
                    month = month,
                    year = year
                ),
                date = date
            )
        }
    }

    private fun getExerciseByName(name: String) {
        getExerciseJob?.cancel()
        getExerciseJob = startWorkoutUseCases
            .getExerciseForName(name)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    exerciseInfo = exercises.map {
                        TrackableExerciseState(exercise = it)
                    }
                )
            }
            .launchIn(viewModelScope)
    }

    companion object {
        private var isRunning: Boolean = false

        fun setAlarm(context: Context, timeDuration: Duration): Long{
            Log.println(Log.DEBUG, "setAlarm", "reached setAlarm")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            Log.println(Log.DEBUG, "current Time", Date().time.toString())
            val wakeUpTime = (System.currentTimeMillis() + timeDuration.toMillis())
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}
