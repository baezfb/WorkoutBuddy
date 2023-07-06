package com.example.workout_logger_domain.model

data class TrackedExercise(
    val id: String? = null,
    val name: String?,
    val exerciseBase: Int?,
    val description: String? = null,
    val muscles: String? = null,
    val muscles_secondary: String? = null,
    val equipment: String? = null,
    val image_url: List<String?> = emptyList(),
    val is_main: String? = null,
    val is_front: String? = null,
    val muscle_name_main: String? = null,
    val image_url_main: List<String?> = emptyList(),
    val image_url_secondary: List<String?> = emptyList(),
    val muscle_name_secondary: String? = null
)
