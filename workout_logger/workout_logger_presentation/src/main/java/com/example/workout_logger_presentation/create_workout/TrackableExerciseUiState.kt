package com.example.workout_logger_presentation.create_workout

import com.example.workout_logger_domain.model.TrackedExercise

data class TrackableExerciseUiState(
    val name: String = "",
    val sets: Int = 0,
    val reps: MutableList<String> = mutableListOf(),
    val rest: MutableList<String> = mutableListOf(),
    val weight: MutableList<String> = mutableListOf(),
    val id: Int = 0,
    val exercise: TrackedExercise?,
    val isDeleted: MutableList<Boolean> = mutableListOf()
)
