package com.example.workout_logger_presentation.create_workout

import com.example.workout_logger_presentation.search_exercise.TrackableExerciseState

data class CreateWorkoutState(
    val workoutName: String = "",
    val isHintVisible: Boolean = false,
    val trackableExercises: List<TrackableExerciseUiState> = emptyList(),
    val trackableExercisesSize: Int = 0,
    val lastUsedId: Int = 0,
    val pageCount: Int = 0,
    val exerciseInfo: List<TrackableExerciseState> = emptyList()
)