package com.hbaez.user_auth_presentation.model

import com.google.firebase.firestore.DocumentId

data class WorkoutTemplate(
    @DocumentId val id: String = "",
    val name: String,
    val exerciseName: String,
    val exerciseId: String?,
    val sets: Int,
    val rest: List<String> = emptyList(),
    val reps: List<String> = emptyList(),
    val weight: List<String> = emptyList(),
    val currentSet: Int = 0,
    val rowId: Int,
    val lastUsedId: Int,
)