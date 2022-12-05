package com.example.workout_logger_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
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
