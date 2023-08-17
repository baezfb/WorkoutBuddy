package com.hbaez.settings_presentation.settings_overview

import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType

data class AppSettingsUiState(
    val shouldShowLogoutCard: Boolean = false,
    val shouldShowDeleteCard: Boolean = false,
//    val gender: Gender,
//    val dob: String,
//    val height: Int,
//    val weight: Int,
//    val activityLevel: ActivityLevel,
//    val bodyGoalType: GoalType,
//    val nutrientGoal: List<Int>,
//    val timerJump: Int,
//    val timerSecs: Int
)
