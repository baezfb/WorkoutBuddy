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
import com.example.workout_logger_domain.model.TrackedExercise
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.create_workout.TrackableExerciseUiState
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState
import com.example.workout_logger_presentation.start_workout.components.TimerExpiredReceiver
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.CalendarDates
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
import java.time.LocalDate
import java.util.Date

@HiltViewModel
class StartWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences,
    private val storageService: StorageService,
    private val startWorkoutUseCases: ExerciseTrackerUseCases,
    logService: LogService
): AuthViewModel(logService) {

    var state by mutableStateOf(StartWorkoutState(timerJump = preferences.loadUserInfo().timerJump.toLong()))
        private set

    var currentTime by mutableStateOf(state.remainingTime)
        private set
    var workoutIds: List<String>

    val workoutTemplates = storageService.workouts

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null
    private var getExerciseByNameJob: Job? = null
    private val workoutId: Int
    private val workoutName: String

    init {
        workoutName = savedStateHandle["workoutName"] ?: ""
        workoutId = savedStateHandle["workoutId"] ?: -1
        workoutIds = (savedStateHandle["workoutIds"] ?: "").trim('[').trim(']').replace(" ","").split(',').toList()
        Log.println(Log.DEBUG, "workoutids viewmodel", workoutIds.toString())

        val initExercises: MutableList<LoggerListState> = emptyList<LoggerListState>().toMutableList()
        val initRoutine: MutableList<WorkoutTemplate> = emptyList<WorkoutTemplate>().toMutableList()
        viewModelScope.launch {
            workoutTemplates.first().forEach {
                //TODO: get completedWorkout by date it.lastUsedDate
                val currExercise: LoggerListState
                if(it.name == workoutName){
                    if(it.lastUsedDate != null && !it.lastUsedDate.equals("null")){
                        val completedWorkouts = storageService.getCompletedWorkoutByDate(it.lastUsedDate!!).toMutableList()
                        val completedWorkout = completedWorkouts.find { completed ->
                            it.exerciseName == completed.exerciseName
                        }
                        currExercise = LoggerListState(
                            id = it.rowId,
                            position = it.position,
                            exerciseName = it.exerciseName,
                            exerciseId = it.exerciseId,
                            timerStatus = TimerStatus.START,
                            sets = (completedWorkout?.sets ?: it.sets).toString(),
                            rest = completedWorkout?.rest ?: it.rest,
                            reps = completedWorkout?.reps ?: it.reps,
                            weight = completedWorkout?.weight ?: it.weight,
                            isCompleted = List(completedWorkout?.sets ?: it.sets) { false },
                            checkedColor = List(completedWorkout?.sets ?: it.sets) { Color.DarkGray },
                        )
                    } else {
                        currExercise = LoggerListState(
                            id = it.rowId,
                            position = it.position,
                            exerciseName = it.exerciseName,
                            exerciseId = it.exerciseId,
                            timerStatus = TimerStatus.START,
                            sets = it.sets.toString(),
                            rest = it.rest,
                            reps = it.reps,
                            weight = it.weight,
                            isCompleted = List(it.sets) { false },
                            checkedColor = List(it.sets) { Color.DarkGray },
                        )
                    }
                    initExercises.add(currExercise)
                    initRoutine.add(it)
                }
            }
            initExercises.sortBy { it.position }
            initRoutine.sortBy { it.position }
            state = state.copy(
                loggerListStates = initExercises,
                routineWorkoutTemplate = initRoutine
            )
        }
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
                Log.println(Log.DEBUG, "loggerlsitstate size", state.loggerListStates.size.toString())
                Log.println(Log.DEBUG, "onrepschange name", event.exerciseName)
                state = state.copy(
                    loggerListStates = state.loggerListStates.map {
                        if(it.position == event.page && it.exerciseName == event.exerciseName){
                            val tmp = it.reps.toMutableList()
                            tmp[event.index] = event.reps
                            it.copy(reps = tmp)
                        } else it
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
                state = state.copy(
                    loggerListStates = state.loggerListStates.toList().map {
                        if(it.position == event.page && it.exerciseName == event.exerciseName){
                            val tmp = it.weight.toMutableList()
                            tmp[event.index]=event.weight
                            it.copy(weight = tmp.toList())
                        }else it
                    }.toMutableList()
                )
            }
            is StartWorkoutEvent.OnCheckboxChange -> {
                Log.println(Log.DEBUG, "event.page", event.page.toString())
                Log.println(Log.DEBUG, "event.index", event.index.toString())
                Log.println(Log.DEBUG, "loggerListStates rest size", state.loggerListStates[event.page].rest.size.toString())
                Log.println(Log.DEBUG, "loggerListStates rest", state.loggerListStates[event.page].rest.toString())
                if(state.currRunningIndex != event.currRunningIndex && state.timerStatus != TimerStatus.RUNNING){
                    state = state.copy(
                        startTime = Date()
                    )
                }
                if(event.shouldUpdateTime){
                    state = state.copy(
                        loggerListStates = state.loggerListStates.toList().map {
                            if(it.position == event.page){
                                val tmp = it.isCompleted.toMutableList()
                                tmp[event.index] = event.isChecked
                                it.copy(isCompleted = tmp, timerStatus = TimerStatus.RUNNING)
                            } else it
                        }.toMutableList(),
                        timerStatus = event.timerStatus,
                        pagerIndex = event.page,
                        timeDuration = Duration.ofSeconds(state.loggerListStates[event.page].rest[event.index].toLong()),
                        currRunningIndex = event.currRunningIndex,
                        currRunningId = event.rowId
                    )
                    currentTime = if(event.isChecked && event.timerStatus == TimerStatus.RUNNING) { state.timeDuration.seconds * 1000L } else currentTime
                }
                else {
                    state = state.copy(
                        loggerListStates = state.loggerListStates.toList().map {
                            if(it.position == event.page){
                                val tmp = it.isCompleted.toMutableList()
                                tmp[event.index] = event.isChecked
                                it.copy(isCompleted = tmp, timerStatus = TimerStatus.RUNNING)
                            } else it
                        }.toMutableList()
                    )
                }
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
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            repsList.add(workoutTemplate.reps.getOrElse(index) { workoutTemplate.reps.last() })
                                            Log.println(Log.DEBUG, "reached inside", repsList.toString())
                                        }
                                    }
                                } else repsList.add(it.reps[index])
                                if(it.weight[index].isBlank()){
                                    event.workoutTemplates.forEach {workoutTemplate ->
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            weightList.add(workoutTemplate.weight.getOrElse(index) { workoutTemplate.weight.last() })
                                        }
                                    }
                                } else weightList.add(it.weight[index])
                            } else {
                                isCompletedList.add("false")
                                if(it.reps[index].isBlank()){
                                    event.workoutTemplates.forEach {workoutTemplate ->
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            repsList.add(workoutTemplate.reps.getOrElse(index) { workoutTemplate.reps.last() })
                                        }
                                    }
                                } else repsList.add(it.reps[index])
                                if(it.weight[index].isBlank()){
                                    event.workoutTemplates.forEach {workoutTemplate ->
                                        if(workoutTemplate.name == state.workoutName && workoutTemplate.exerciseName == it.exerciseName){
                                            weightList.add(workoutTemplate.weight.getOrElse(index) { workoutTemplate.weight.last() })
                                        }
                                    }
                                } else weightList.add(it.weight[index])
                            }
                        }
                        if(repsList.isNotEmpty() && weightList.isNotEmpty()){
                            trackCompletedWorkout(it, repsList, weightList, isCompletedList.toList(), event.dayOfMonth, event.month, event.year)
                            trackCalendarDate(event.year, event.month, event.dayOfMonth)
                        }
                    }
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.NavigateUp)
                    }
                }
            }

            is StartWorkoutEvent.GetExerciseInfo -> {
                val nameList: MutableList<String> = mutableListOf()
                state.loggerListStates.forEach {
                    if (it.position == event.page){
                        nameList.add(it.exerciseName)
                    }
                }
                if(nameList.size == 1){
                    getExerciseByName(nameList.first())
                } else {
                    getSupersetExerciseByName(name1 = nameList[0], name2 = nameList[1])
                }
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

            is StartWorkoutEvent.OnTimeJump -> {
                state = if(event.increase){
                    state.copy(
                        timeDuration = state.timeDuration + Duration.ofSeconds(event.timeJump)
                    )
                } else {
                    state.copy(
                        timeDuration = state.timeDuration - Duration.ofSeconds(event.timeJump)
                    )
                }
            }

            is StartWorkoutEvent.OnRemoveSet -> {
                state = state.copy(
                    loggerListStates = state.loggerListStates.toList().map {
                        if(it.position == event.page){
                            val tmpRest = it.rest.toMutableList()
                            tmpRest.removeAt(it.rest.size - 1)
                            val tmpReps = it.reps.toMutableList()
                            tmpReps.removeAt(it.reps.size - 1)
                            val tmpWeight = it.weight.toMutableList()
                            tmpWeight.removeAt(it.weight.size - 1)
                            val tmpCompleted = it.isCompleted.toMutableList()
                            tmpCompleted.removeAt(it.isCompleted.size - 1)
                            val tmpColor = it.checkedColor.toMutableList()
                            tmpColor.removeAt(it.checkedColor.size - 1)
                            it.copy(
                                sets = (it.sets.toInt() - 1).toString(),
                                rest = tmpRest.toList(),
                                reps = tmpReps.toList(),
                                weight = tmpWeight.toList(),
                                isCompleted = tmpCompleted.toList(),
                                checkedColor = tmpColor.toList()
                            )
                        } else it
                    }.toMutableList()
                )
            }

            is StartWorkoutEvent.OnAddSet -> {
                state = state.copy(
                    loggerListStates = state.loggerListStates.toList().map {
                        if(it.position == event.page){
                            it.copy(
                                sets = (it.sets.toInt() + 1).toString(),
                                rest = it.rest + it.rest.last(),
                                reps = it.reps + it.reps.last(),
                                weight = it.weight + it.weight.last(),
                                isCompleted = it.isCompleted + false,
                                checkedColor = it.checkedColor + Color.DarkGray
                            )
                        } else it
                    }.toMutableList()
                )
            }
        }
    }

    private fun trackCompletedWorkout(loggerListState: LoggerListState, repsList: List<String>, weightList: List<String>, isCompletedList: List<String>, dayOfMonth: Int, month: Int, year: Int){
        viewModelScope.launch {
            val date = "${year}-${month.toString().padStart(2,'0')}-${dayOfMonth.toString().padStart(2, '0')}"
            storageService.saveCompletedWorkout(
                CompletedWorkout(
                    docId = "",
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
            // update lastUsedDate
            workoutTemplates.first().onEach {
                Log.println(Log.DEBUG, "StartWorkoutViewModel workoutTemplates", it.name)
                if(it.name == state.workoutName){
                    Log.println(Log.DEBUG, "inside if StartWorkoutViewModel", it.name)
                    Log.println(Log.DEBUG, "inside if StartWorkoutViewModel", LocalDate.of(year, month, dayOfMonth).toString())
                    storageService.updateWorkoutTemplate(
                        WorkoutTemplate(
                            id = it.id,
                            name = it.name,
                            exerciseName = it.exerciseName,
                            exerciseId = null,
                            sets = it.sets,
                            rest = it.rest,
                            reps = it.reps,
                            weight = it.weight,
                            rowId = -1,
                            position = -1,
                            lastUsedId = it.lastUsedId,
                            lastUsedDate = LocalDate.of(year, month, dayOfMonth).toString(),
                            isSuperset = it.isSuperset
                        )
                    )
                }
            }
        }
    }

    private fun trackCalendarDate(year: Int, month: Int, dayOfMonth: Int){
        viewModelScope.launch {
            if(LocalDate.of(year, month, dayOfMonth).toString() !in storageService.calendarDates.first().calendarDates){
                storageService.saveCalendarDate(
                    CalendarDates(storageService.calendarDates.first().calendarDates + LocalDate.of(year, month, dayOfMonth).toString())
                )
            }
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
    private fun getSupersetExerciseByName(name1: String, name2: String) {
        state = state.copy(
            exerciseInfo = List(2) { TrackableExerciseState(exercise = TrackedExercise(name=null, exerciseBase = null))}
        )
        getExerciseJob?.cancel()
        getExerciseJob = startWorkoutUseCases
            .getExerciseForName(name1)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.mapIndexed { index, trackableExerciseState ->
                        if(index == 0){
                            TrackableExerciseState(exercise = exercises.first())
                        } else trackableExerciseState
                    }
                )
            }
            .launchIn(viewModelScope)
        getExerciseByNameJob?.cancel()
        getExerciseByNameJob = startWorkoutUseCases
            .getExerciseForName(name2)
            .onEach { exercises ->
                if(exercises.isEmpty()){
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.empty_results)
                        )
                    )
                }
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.mapIndexed { index, trackableExerciseState ->
                        if(index == 1){
                            TrackableExerciseState(exercise = exercises.first())
                        } else trackableExerciseState
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
