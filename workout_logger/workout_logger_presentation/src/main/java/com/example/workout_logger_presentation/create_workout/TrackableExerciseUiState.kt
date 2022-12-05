package com.example.workout_logger_presentation.create_workout

import com.example.workout_logger_domain.model.TrackedExercise

data class TrackableExerciseUiState(
    val name: String = "",
    val sets: String = "",
    val reps: String = "",
    val rest: String = "",
    val weight: String = "",
    val id: Int = 0,
    val exercise: TrackedExercise?,
    val isRevealed: Boolean = false,
    val isSearchRevealed: Boolean = false,
    val isDeleted: Boolean = false
)
