package com.example.workout_logger_presentation.create_workout

import com.example.workout_logger_domain.model.TrackedExercise

data class TrackableExerciseUiState(
    val docId: String = "",
    val name: String = "",
    val sets: Int = 0,
    val reps: List<String> = List(0) { "" },
    val rest: List<String> = List(0) { "" },
    val weight: List<String> = List(0) { "" },
    val id: Int = 0,
    val exercise: TrackedExercise?,
    val isDeleted: List<Boolean> = List(0) { false }
)
