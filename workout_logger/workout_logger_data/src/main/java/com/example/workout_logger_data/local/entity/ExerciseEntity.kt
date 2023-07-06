package com.example.workout_logger_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val exerciseBase: Int,
    val description: String?,
    val muscles: String?,
    val muscles_secondary: String?,
    val equipment: String?,
    val image_url: String?,
    val is_main: String?,
    val is_front: String?,
    val muscle_name_main: String?,
    val image_url_main: String?,
    val image_url_secondary: String?,
    val muscle_name_secondary: String?
)
