package com.example.workout_logger_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompletedWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
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
