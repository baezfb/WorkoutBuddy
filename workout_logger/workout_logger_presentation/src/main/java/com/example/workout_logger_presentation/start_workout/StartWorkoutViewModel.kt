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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.example.workout_logger_presentation.start_workout.components.TimerExpiredReceiver
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.Duration
import java.util.Date
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class StartWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences,
    private val startWorkoutUseCases: ExerciseTrackerUseCases
): ViewModel() {

    var state by mutableStateOf(StartWorkoutState())
        private set

    var currentTime by mutableStateOf(state.remainingTime)
        private set

    private var workoutName: String
    private var workoutId: Int

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getExerciseJob: Job? = null

    init {
        workoutName = savedStateHandle.get("workoutName") ?: ""
        workoutId = savedStateHandle.get("workoutId") ?: -1
        getWorkout()
    }

    fun onEvent(event: StartWorkoutEvent) {
        when(event) {
            is StartWorkoutEvent.OnRepsChange -> {
                state = state.copy(
                    trackableInProgressExercise = state.trackableInProgressExercise.toList().map {
                        if(it.id == event.id){
                            val tmp = it.reps.toMutableList()
                            tmp[event.index]=event.reps
                            it.copy(reps = tmp.toList())
                        }else it
                    }.toMutableList()
                )
            }
            is StartWorkoutEvent.OnWeightChange -> {
                state = state.copy(
                    trackableInProgressExercise = state.trackableInProgressExercise.toList().map {
                        if(it.id == event.id){
                            val tmp = it.weight.toMutableList()
                            tmp[event.index]=event.weight
                            it.copy(weight = tmp.toList())
                        }else it
                    }.toMutableList()
                )
            }
            is StartWorkoutEvent.OnCheckboxChange -> {
                if(state.currRunningIndex != event.currRunningIndex){
                    state = state.copy(
                        startTime = Date()
                    )
                }
                state = state.copy(
                    trackableInProgressExercise = state.trackableInProgressExercise.toList().map {
                        if(it.id == event.id){
                            val tmp = it.isCompleted.toMutableList()
                            tmp[event.index] = event.isChecked
                            it.copy(isCompleted = tmp, timerStatus = TimerStatus.RUNNING)
                        } else it
                    }.toMutableList(),
                    timerStatus = event.timerStatus,
                    pagerIndex = event.page,
                    timeDuration = Duration.ofSeconds(state.trackableInProgressExercise[event.page].origRest.toLong()),
                    currRunningIndex = event.currRunningIndex,
                    currRunningId = event.id
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
                Log.println(Log.DEBUG, "currentTime update", state.trackableInProgressExercise[event.currentPage].origRest)
                state = state.copy(
                    remainingTime = state.trackableInProgressExercise[event.currentPage].origRest.toLong(),
                    timeDuration = Duration.ofSeconds(state.trackableInProgressExercise[event.currentPage].origRest.toLong()),
                    pagerIndex = event.currentPage
                )
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
                    trackableInProgressExercise = state.trackableInProgressExercise.toList().map {
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
                        it.isCompleted.forEachIndexed { index, b ->  // for each set in exercise
                            if(b){
                                if(it.reps[index].isEmpty()){
                                    repsList.add(it.origReps)
                                } else repsList.add(it.reps[index])
                                if(it.weight[index].isEmpty()){
                                    weightList.add(it.origWeight)
                                } else weightList.add(it.weight[index])
                            }
                        }
                        if(repsList.isNotEmpty() && weightList.isNotEmpty()){
                            trackCompletedWorkout(it, repsList, weightList, event.dayOfMonth, event.month, event.year)
                        }
                    }
                }
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun getWorkout(){
        getExerciseJob?.cancel()
        getExerciseJob = startWorkoutUseCases
            .getWorkoutsByName(workoutName)
            .onEach { exercises ->
                state = state.copy(
                    trackableInProgressExercise = exercises.map {
                        TrackableInProgressExerciseUi(
                            name = it.exerciseName,
                            origSets = it.sets.toString(),
                            sets = it.sets.toString(),
                            origReps = it.reps.toString(),
                            reps = List(it.sets) { "" },
                            origRest = it.rest.toString(),
                            rest = List(it.sets) { _ -> it.rest.toString() },
                            origWeight = it.weight.toString(),
                            weight = List(it.sets) { "" },
                            id = it.rowId,
                            exerciseId = it.exerciseId,
                            exercise = null,
                            isCompleted = List(it.sets) { false },
                            timerStatus = TimerStatus.START
                        )
                    }.toMutableList()
                )
            }.launchIn(viewModelScope)
    }

    private fun trackCompletedWorkout(trackableInProgressExerciseUi: TrackableInProgressExerciseUi, repsList: List<String>, weightList: List<String>, dayOfMonth: Int, month: Int, year: Int){
        viewModelScope.launch {
            startWorkoutUseCases.addCompletedWorkout(
                workoutName = workoutName,
                workoutId = workoutId,
                exerciseName = trackableInProgressExerciseUi.name,
                exerciseId = trackableInProgressExerciseUi.exerciseId,
                sets = trackableInProgressExerciseUi.origSets.toInt(),
                rest = trackableInProgressExerciseUi.origRest.toInt(),
                reps = repsList.toString(),
                weight = weightList.toString(),
                dayOfMonth = dayOfMonth,
                month = month,
                year = year
            )
        }
    }

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
