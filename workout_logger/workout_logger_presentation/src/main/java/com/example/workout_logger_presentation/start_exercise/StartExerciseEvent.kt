package com.example.workout_logger_presentation.start_exercise

import androidx.compose.ui.graphics.Color
import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState

sealed class StartExerciseEvent {
    data class OnRepsChange(val reps: String, val index: Int): StartExerciseEvent()

    data class OnWeightChange(val weight: String, val index: Int): StartExerciseEvent()

    data class OnCheckboxChange(val isChecked: Boolean, val timerStatus: TimerStatus, val currRunningIndex: Int, val index: Int, val shouldUpdateTime: Boolean): StartExerciseEvent()

    data class ChangeCheckboxColor(val color: Color, val index: Int): StartExerciseEvent()

    data class OnSubmitWorkout(val exerciseName: String, val exerciseID: String, val sets: String, val reps: List<String>, val weight: List<String>, val rest: List<String>, val dayOfMonth: Int, val month: Int, val year: Int): StartExerciseEvent()

    data class GetExerciseInfo(val exerciseName: String): StartExerciseEvent()

    data class OnToggleExerciseDescription(val trackableExerciseState: TrackableExerciseState): StartExerciseEvent()

    data class OnTimeJump(val increase: Boolean, val timeJump: Long): StartExerciseEvent()

    object OnAddSet: StartExerciseEvent()

    object OnRemoveSet: StartExerciseEvent()

    object ChangeRemainingTime: StartExerciseEvent()

    object TimerFinished: StartExerciseEvent()
}
