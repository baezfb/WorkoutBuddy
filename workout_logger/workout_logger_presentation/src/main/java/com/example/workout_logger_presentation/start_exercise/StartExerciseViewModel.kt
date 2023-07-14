package com.example.workout_logger_presentation.start_exercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.example.workout_logger_presentation.start_workout.StartWorkoutEvent
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StartExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences,
    private val storageService: StorageService,
    logService: LogService
): AuthViewModel(logService) {

    var state by mutableStateOf(StartExerciseState())
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
            is StartExerciseEvent.GetExerciseInfo -> TODO()
            is StartExerciseEvent.OnToggleExerciseDescription -> TODO()
            is StartExerciseEvent.OnTimeJump -> TODO()
            is StartExerciseEvent.ChangeCheckboxColor -> TODO()
            is StartExerciseEvent.OnCheckboxChange -> TODO()
            is StartExerciseEvent.OnSubmitWorkout -> TODO()
        }
    }
}