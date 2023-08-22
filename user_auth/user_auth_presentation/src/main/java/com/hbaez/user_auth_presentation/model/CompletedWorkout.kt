package com.hbaez.user_auth_presentation.model

data class CompletedWorkout(
    val docId: String,
    val workoutName: String,
    val workoutId: Int,
    val exerciseName: String,
    val exerciseId: String?,
    val sets: Int,
    val rest: List<String>,
    val reps: List<String>,
    val weight: List<String>,
    val isCompleted: List<String>,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
)
