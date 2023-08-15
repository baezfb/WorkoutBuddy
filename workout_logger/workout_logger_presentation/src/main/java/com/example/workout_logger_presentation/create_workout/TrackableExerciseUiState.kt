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
    val position: Int,
    val exercise: TrackedExercise?,
    val isDeleted: Boolean = false,
    val lastUsedDate: String? = null

)
