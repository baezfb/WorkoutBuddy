package com.example.workout_logger_presentation.create_workout

data class CreateWorkoutState(
    val workoutName: String = "",
    val isHintVisible: Boolean = false,
    val trackableExercises: MutableList<TrackableExerciseUiState> = mutableListOf(),
    val lastUsedId: Int = 0,
    val pageCount: Int = 0
)