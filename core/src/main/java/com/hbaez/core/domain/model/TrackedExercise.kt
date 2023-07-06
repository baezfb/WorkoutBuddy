package com.hbaez.core.domain.model

data class TrackedExercise(
    val rowId: Int,
    val id: String?,
    val name: String,
    val exerciseBase: Int,
    val description: String?,
    val muscles: String?,
    val muscles_secondary: String?,
    val equipment: String?,
    val image_url: Set<String?>,
    val is_main: String?,
    val is_front: String?,
    val muscle_name_main: String?,
    val image_url_main: Set<String?>,
    val image_url_secondary: Set<String?>,
    val muscle_name_secondary: String?
)
