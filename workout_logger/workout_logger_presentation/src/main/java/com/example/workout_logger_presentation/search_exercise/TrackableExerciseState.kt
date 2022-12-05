package com.example.workout_logger_presentation.search_exercise

import com.example.workout_logger_domain.model.TrackedExercise

data class TrackableExerciseState(
    val exercise: TrackedExercise,
    val isExpanded: Boolean = false,
    val isDescrExpanded: Boolean = false
)