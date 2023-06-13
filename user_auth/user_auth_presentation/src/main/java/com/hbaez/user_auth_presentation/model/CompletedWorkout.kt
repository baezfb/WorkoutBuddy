package com.hbaez.user_auth_presentation.model

data class CompletedWorkout(
    val workoutName: String,
    val workoutId: Int,
    val exerciseName: String,
    val exerciseId: Int?,
    val sets: Int,
    val rest: List<String>,
    val reps: List<String>,
    val weight: List<String>,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
)
