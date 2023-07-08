package com.hbaez.user_auth_presentation.model

data class ExerciseTemplate(
    val docId: String = "",
    val id: String = "",
    val name: String,
    val exerciseBase: Int = -1,
    val description: String = "N/A",
    val muscle_name_main: String,
    val muscle_name_secondary: String,
    val image_url_main: String,
    val image_url_secondary: String,
    val image_url: String? = null,
    val equipment: String? = null,
    val is_front: String? = null,
    val is_main: String? = null,
    val muscles: String? = null,
    val muscles_secondary: String? = null
)
