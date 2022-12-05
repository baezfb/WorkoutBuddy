package com.example.workout_logger_domain.model

data class TrackedWorkout(
    val workoutId: Int?,
    val name: String,
    val exerciseName: String,
    val exerciseId: Int?,
    val sets: Int,
    val rest: Int,
    val reps: Int,
    val weight: Int,
    val rowId: Int,
    val lastUsedId: Int
)
