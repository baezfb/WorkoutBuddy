package com.example.workout_logger_presentation.create_exercise

import com.example.workout_logger_presentation.create_exercise.model.Muscle

sealed class CreateExerciseEvent {

    data class OnUpdateExerciseName(val exerciseName: String): CreateExerciseEvent()

    data class OnUpdateDescription(val description: String): CreateExerciseEvent()

    data class OnCheckboxAdd(val muscle: Muscle, val isPrimary: Boolean): CreateExerciseEvent()

    data class OnCheckboxRemove(val muscle: Muscle, val isPrimary: Boolean): CreateExerciseEvent()

    object OnSubmitExercise: CreateExerciseEvent()
}