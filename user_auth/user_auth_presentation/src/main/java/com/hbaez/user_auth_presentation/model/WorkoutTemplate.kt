package com.hbaez.user_auth_presentation.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentId

data class WorkoutTemplate(
    @DocumentId val id: String = "",
    val name: String,
    val exerciseName: String,
    val exerciseId: Int?,
    val sets: Int,
    val rest: List<String> = emptyList(),
    val reps: List<String> = emptyList(),
    val weight: List<String> = emptyList(),
    val rowId: Int,
    val lastUsedId: Int,
)