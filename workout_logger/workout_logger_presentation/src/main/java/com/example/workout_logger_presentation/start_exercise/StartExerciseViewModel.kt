package com.example.workout_logger_presentation.start_exercise

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.hbaez.user_auth_presentation.model.CalendarDates
import com.hbaez.user_auth_presentation.model.CompletedWorkout
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
import java.time.Duration
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StartExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences,
    private val storageService: StorageService,
    private val startWorkoutUseCases: ExerciseTrackerUseCases,
    logService: LogService
): AuthViewModel(logService) {

    var state by mutableStateOf(StartExerciseState(
        timerJump = preferences.loadUserInfo().timerJump.toLong(),
        rest = listOf(preferences.loadUserInfo().timerSeconds.toString())))
        private set

    var currentTime by mutableStateOf(state.remainingTime)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null

    init {
        state = state.copy(
            exerciseName = savedStateHandle["exerciseName"] ?: ""
        )
        onEvent(StartExerciseEvent.GetExerciseInfo(state.exerciseName))
    }

    fun onEvent(event: StartExerciseEvent){
        when(event) {
            is StartExerciseEvent.OnRepsChange -> {
                val tmp = state.reps.toMutableList()
                tmp[event.index] = event.reps
                state = state.copy(
                    reps = tmp.toList()
                )
            }
            is StartExerciseEvent.OnWeightChange -> {
                val tmp = state.weight.toMutableList()
                tmp[event.index] = event.weight
                state = state.copy(
                    weight = tmp.toList()
                )
            }
            is StartExerciseEvent.ChangeRemainingTime -> {
                val diff = Date().time - state.startTime.time
                currentTime = state.timeDuration.toMillis() - diff
            }
            is StartExerciseEvent.TimerFinished -> {
                state = state.copy(
                    timerStatus = TimerStatus.FINISHED,
                    currRunningIndex = -1
                )
            }
            is StartExerciseEvent.OnAddSet -> {
                state = state.copy(
                    sets = (state.sets.toInt() + 1).toString(),
                    rest = state.rest + state.rest.last(),
                    reps = state.reps + state.reps.last(),
                    weight = state.weight + state.weight.last(),
                    isCompleted = state.isCompleted + false,
                    checkedColor = state.checkedColor + Color.DarkGray
                )
            }
            is StartExerciseEvent.OnRemoveSet -> {
                val tmpRest = state.rest.toMutableList()
                tmpRest.removeAt(state.rest.size - 1)
                val tmpReps = state.reps.toMutableList()
                tmpReps.removeAt(state.reps.size - 1)
                val tmpWeight = state.weight.toMutableList()
                tmpWeight.removeAt(state.weight.size - 1)
                val tmpCompleted = state.isCompleted.toMutableList()
                tmpCompleted.removeAt(state.isCompleted.size - 1)
                val tmpColor = state.checkedColor.toMutableList()
                tmpColor.removeAt(state.checkedColor.size - 1)

                state = state.copy(
                    sets = (state.sets.toInt() - 1).toString(),
                    rest = tmpRest.toList(),
                    reps = tmpReps.toList(),
                    weight = tmpWeight.toList(),
                    isCompleted = tmpCompleted.toList(),
                    checkedColor = tmpColor.toList()
                )
            }
            is StartExerciseEvent.GetExerciseInfo -> {
                getExerciseByName(event.exerciseName)
            }
            is StartExerciseEvent.OnToggleExerciseDescription -> {
                state = state.copy(
                    exerciseInfo = state.exerciseInfo.map {
                        it.copy(isDescrExpanded = !it.isDescrExpanded)
                    }
                )
            }
            is StartExerciseEvent.OnTimeJump -> {
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
            is StartExerciseEvent.ChangeCheckboxColor -> {
                val tmp = state.checkedColor.toMutableList()
                tmp[event.index] = event.color
                state = state.copy(
                    checkedColor = tmp.toList()
                )
            }
            is StartExerciseEvent.OnCheckboxChange -> {
                if(state.currRunningIndex != event.currRunningIndex && state.timerStatus != TimerStatus.RUNNING){
                    state = state.copy(
                        startTime = Date()
                    )
                }
                if(event.shouldUpdateTime){
                    val tmp = state.isCompleted.toMutableList()
                    tmp[event.index] = event.isChecked
                    state = state.copy(
                        isCompleted = tmp.toList(),
                        timerStatus = event.timerStatus,
                        timeDuration = Duration.ofSeconds(state.rest[event.index].toLong()),
                        currRunningIndex = event.currRunningIndex,
                    )
                    currentTime = if(event.isChecked && event.timerStatus == TimerStatus.RUNNING) { state.timeDuration.seconds * 1000L } else currentTime
                }
                else {
                    val tmp = state.isCompleted.toMutableList()
                    tmp[event.index] = event.isChecked
                    state = state.copy(
                        isCompleted = tmp.toList()
                    )
                }
            }
            is StartExerciseEvent.OnSubmitWorkout -> {
                run breaking@{
                    if(state.timerStatus == TimerStatus.RUNNING){
                        return@breaking
                    }
                    val repsList = mutableListOf<String>()
                    val weightList = mutableListOf<String>()
                    val isCompletedList = mutableListOf<String>()
                    state.isCompleted.forEachIndexed { index, b ->  // for each set in exercise
                        if(b){
                            isCompletedList.add("true")
                            if(state.reps[index].isBlank()){
                                repsList.add("10") /*TODO: replace with user preferences*/
                            } else repsList.add(state.reps[index])
                            if(state.weight[index].isBlank()){
                                weightList.add("100") /*TODO: replace with user preferences*/
                            } else weightList.add(state.weight[index])
                        } else {
                            isCompletedList.add("false")
                            if(state.reps[index].isBlank()){
                                repsList.add("10") /*TODO: replace with user preferences*/
                            } else repsList.add(state.reps[index])
                            if(state.weight[index].isBlank()){
                                weightList.add("100") /*TODO: replace with user preferences*/
                            } else weightList.add(state.weight[index])
                        }
                    }
                    val restList = List(repsList.size) { preferences.loadUserInfo().timerSeconds.toString() } /*TODO: replace with user preferences*/
                    if(repsList.isNotEmpty() && weightList.isNotEmpty()){
                        trackCompletedExercise(event, restList, repsList, weightList, isCompletedList.toList())
                        trackCalendarDate(event.year, event.month, event.dayOfMonth)
                    }
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.NavigateUp)
                    }
                }
            }
        }
    }

    private fun trackCompletedExercise(event: StartExerciseEvent.OnSubmitWorkout, restList: List<String>, repsList: List<String>, weightList: List<String>, isCompletedList: List<String>){
        viewModelScope.launch {
            val date = "${event.year}-${event.month.toString().padStart(2,'0')}-${event.dayOfMonth.toString().padStart(2, '0')}"
            storageService.saveCompletedWorkout(
                CompletedWorkout(
                    docId = "",
                    workoutName = "",
                    workoutId = -1,
                    exerciseName = event.exerciseName,
                    exerciseId = event.exerciseID,
                    sets = event.sets.toInt(),
                    rest = restList,
                    reps = repsList,
                    weight = weightList,
                    isCompleted = isCompletedList,
                    dayOfMonth = event.dayOfMonth,
                    month = event.month,
                    year = event.year
                ),
                date = date
            )
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

    companion object {
        fun setAlarm(context: Context, timeDuration: Duration): Long{
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
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