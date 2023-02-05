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
import com.example.workout_logger_presentation.start_workout.components.TimerExpiredReceiver
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
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

    private var workoutName: String = ""
    private var workoutId: Int

    val workoutTemplates = storageService.workouts

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null

    init {
//        workoutName = savedStateHandle.get("workoutName") ?: ""
        workoutId = savedStateHandle["workoutId"] ?: -1
        workoutTemplates.onEach { exercises ->
            state = state.copy(
                loggerListStates = exercises.map {
                    LoggerListState(
                        id = it.rowId,
                        exerciseName = it.exerciseName,
                        exerciseId = it.exerciseId,
                        timerStatus = TimerStatus.START,
                        checkedColor = List(exercises.size) { _ -> List(it.sets) { Color.DarkGray } }
                    )
                }.toMutableList()
            )
        }
        launchCatching {
            workoutName = workoutTemplates.first()[0].name
        }
    }

//    fun onEvent(event: StartWorkoutEvent) {
//        when(event) {
//            is StartWorkoutEvent.OnRepsChange -> {
//                state = state.copy(
//                    loggerListStates = state.loggerListStates.toList().map {
//                        if(it.id == event.id){
//                            val tmp = it.reps.toMutableList()
//                            tmp[event.index]=event.reps
//                            it.copy(reps = tmp.toList())
//                        }else it
//                    }.toMutableList()
//                )
//            }
//            is StartWorkoutEvent.OnWeightChange -> {
//                state = state.copy(
//                    loggerListStates = state.loggerListStates.toList().map {
//                        if(it.id == event.id){
//                            val tmp = it.weight.toMutableList()
//                            tmp[event.index]=event.weight
//                            it.copy(weight = tmp.toList())
//                        }else it
//                    }.toMutableList()
//                )
//            }
//            is StartWorkoutEvent.OnCheckboxChange -> {
//                if(state.currRunningIndex != event.currRunningIndex){
//                    state = state.copy(
//                        startTime = Date()
//                    )
//                }
//                Log.println(Log.DEBUG, "loggerliststates size", state.loggerListStates.size.toString())
//                state = state.copy(
//                    loggerListStates = state.loggerListStates.toList().map {
//                        if(it.id == event.id){
//                            val tmp = it.isCompleted.toMutableList()
//                            tmp[event.index] = event.isChecked
//                            it.copy(isCompleted = tmp, timerStatus = TimerStatus.RUNNING)
//                        } else it
//                    }.toMutableList(),
//                    timerStatus = event.timerStatus,
//                    pagerIndex = event.page,
//                    timeDuration = Duration.ofSeconds(state.loggerListStates[event.page].origRest.toLong()),
//                    currRunningIndex = event.currRunningIndex,
//                    currRunningId = event.id
//                )
//                currentTime = if(event.isChecked && event.timerStatus == TimerStatus.RUNNING) { state.timeDuration.seconds * 1000L } else currentTime
//            }
//            is StartWorkoutEvent.ChangeRemainingTime -> {
//                val diff = Date().time - state.startTime.time
//                currentTime = state.timeDuration.toMillis() - diff
//            }
//            is StartWorkoutEvent.UpdateRemainingTime -> {
//                currentTime = currentTime
//            }
//            is StartWorkoutEvent.OnChangePage -> {
//                state = state.copy(
//                    remainingTime = state.loggerListStates[event.currentPage].origRest.toLong(),
//                    timeDuration = Duration.ofSeconds(state.loggerListStates[event.currentPage].origRest.toLong()),
//                    pagerIndex = event.currentPage
//                )
//            }
//            is StartWorkoutEvent.TimerFinished -> {
//                Log.println(Log.DEBUG, "!!!! current page", state.pagerIndex.toString())
//                state = state.copy(
//                    timerStatus = TimerStatus.FINISHED,
//                    currRunningIndex = -1,
//                    currRunningId = -1
//                )
//            }
//            is StartWorkoutEvent.ChangeCheckboxColor -> {
//                state = state.copy(
//                    loggerListStates = state.loggerListStates.toList().map {
//                        if(it.id == event.id){
//                            val tmp = it.checkedColor.toMutableList()
//                            tmp[event.index] = event.color
//                            it.copy(checkedColor = tmp)
//                        } else it
//                    }.toMutableList()
//                )
//            }
//            is StartWorkoutEvent.OnSubmitWorkout -> {
//                run breaking@{
//                    if(state.timerStatus == TimerStatus.RUNNING){
//                        return@breaking
//                    }
//                    event.trackableExercises.forEach{// for each exercise
//                        val repsList = mutableListOf<Int>()
//                        val weightList = mutableListOf<Int>()
//                        it.isCompleted.forEachIndexed { index, b ->  // for each set in exercise
//                            if(b){
//                                if(it.reps[index].isEmpty()){
//                                    repsList.add(it.origReps)
//                                } else repsList.add(it.reps[index].toInt())
//                                if(it.weight[index].isEmpty()){
//                                    weightList.add(it.origWeight)
//                                } else weightList.add(it.weight[index].toInt())
//                            }
//                        }
//                        if(repsList.isNotEmpty() && weightList.isNotEmpty()){
//                            trackCompletedWorkout(it, repsList, weightList, event.dayOfMonth, event.month, event.year)
//                        }
//                    }
//                }
//                viewModelScope.launch {
//                    _uiEvent.send(UiEvent.NavigateUp)
//                }
//            }
//        }
//    }

//    private fun trackCompletedWorkout(loggerListState: LoggerListState, repsList: List<Int>, weightList: List<Int>, dayOfMonth: Int, month: Int, year: Int){
//        viewModelScope.launch {
//            startWorkoutUseCases.addCompletedWorkout(
//                workoutName = workoutName,
//                workoutId = workoutId,
//                exerciseName = loggerListState.exerciseName,
//                exerciseId = loggerListState.exerciseId,
//                sets = loggerListState.sets,
//                rest = loggerListState.origRest,
//                reps = repsList.toString(),
//                weight = weightList.toString(),
//                dayOfMonth = dayOfMonth,
//                month = month,
//                year = year
//            )
//        }
//    }

    companion object {
        private var isRunning: Boolean = false

        fun setAlarm(context: Context, timeDuration: Duration): Long{
            Log.println(Log.DEBUG, "setAlarm", "reached setAlarm")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            Log.println(Log.DEBUG, "current Time", Date().time.toString())
            val wakeUpTime = (Date().time + timeDuration.toMillis())
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
