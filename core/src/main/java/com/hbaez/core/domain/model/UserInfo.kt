package com.hbaez.core.domain.model

import com.google.firebase.firestore.DocumentId

data class UserInfo(
    @DocumentId val id: String,
    val gender: Gender,
    val age: Long,
    val weight: Float,
    val height: Int,
    val activityLevel: ActivityLevel,
    val goalType: GoalType,
    val carbRatio: Float,
    val proteinRatio: Float,
    val fatRatio: Float,
    val timerJump: Int,
    val timerSeconds: Int
)
