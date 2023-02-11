package com.hbaez.user_auth_presentation.model

data class CompletedWorkout(
    val workoutName: String,
    val workoutId: Int,
    val exerciseName: String,
    val exerciseId: Int?,
    val sets: Int,
    val rest: Int,
    val reps: String,
    val weight: String,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
)
