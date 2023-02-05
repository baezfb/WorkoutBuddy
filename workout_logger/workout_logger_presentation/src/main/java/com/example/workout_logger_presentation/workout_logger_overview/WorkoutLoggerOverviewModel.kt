package com.example.workout_logger_presentation.workout_logger_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workout_logger_domain.model.CompletedWorkout
import com.example.workout_logger_domain.use_case.ExerciseTrackerUseCases
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutLoggerOverviewModel @Inject constructor(
    preferences: Preferences,
    private val exerciseTrackerUseCases: ExerciseTrackerUseCases,
    private val storageService: StorageService
): ViewModel() {

    var state by mutableStateOf(WorkoutLoggerOverviewState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getWorkoutsForDateJob: Job? = null
    private var getWorkoutNames: Job? = null

    val workoutTemplates = storageService.workouts

    init {
        refreshWorkouts()
    }
    fun onEvent(event: WorkoutLoggerOverviewEvent) {
        when(event) {
            is WorkoutLoggerOverviewEvent.OnNextDayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                refreshWorkouts()
            }
            is WorkoutLoggerOverviewEvent.OnPreviousDayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                refreshWorkouts()
            }
            is WorkoutLoggerOverviewEvent.OnStartWorkoutClick -> {
                getWorkoutNames?.cancel()
                val workoutNames = mutableListOf<String>()
                val workoutId = mutableListOf<Int>()
                getWorkoutNames = exerciseTrackerUseCases.getWorkouts().onEach {
                    it.onEach { trackedWorkout ->
                        state = state.copy(
                            workoutNames = (workoutNames + trackedWorkout.name).toMutableList(),
                            workoutId = (workoutId + trackedWorkout.workoutId!!).toMutableList()
                        )
                        workoutNames.add(trackedWorkout.name)
                        workoutId.add(trackedWorkout.workoutId!!)
                    }
                }.launchIn(viewModelScope)
            }
            is WorkoutLoggerOverviewEvent.OnCompletedWorkoutClick -> {
                state = state.copy(
                    completedWorkouts = state.completedWorkouts.map {
                        if(it.id == event.completedWorkout.id){
                            it.copy(
                                isExpanded = !it.isExpanded
                            )
                        } else it
                    }
                )
            }
        }
    }

    private fun refreshWorkouts(){
        getWorkoutsForDateJob?.cancel()
        getWorkoutsForDateJob = exerciseTrackerUseCases
            .getWorkoutsForDate(state.date)
            .onEach { completedWorkouts ->
                state = state.copy(
                    completedWorkouts = completedWorkouts
                    )
            }
            .launchIn(viewModelScope)
    }

}