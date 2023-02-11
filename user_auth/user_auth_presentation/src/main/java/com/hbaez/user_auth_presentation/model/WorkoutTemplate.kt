package com.hbaez.user_auth_presentation.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentId

data class WorkoutTemplate(
    @DocumentId val id: String = "",
    val name: String,
    val exerciseName: String,
    val exerciseId: Int?,
    val sets: Int,
    val rest: Int,
//    val restList: List<String> = List(rest) { "" },
    val reps: Int,
//    val repsList: List<String> = List(reps) { "" },
    val weight: Int,
//    val weightList: List<String> = List(weight) { "" },
    val rowId: Int,
    val lastUsedId: Int,
//    val isCompleted: List<Boolean> = List(sets) { false },
//    val timerStatus: String = "START",
//    val checkedColor: List<Color> = List(sets) { Color.DarkGray }
)