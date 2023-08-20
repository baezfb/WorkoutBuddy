package com.hbaez.settings_presentation.settings_overview

import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.UserInfo
import java.util.prefs.Preferences

data class AppSettingsUiState(
    val shouldShowLogoutCard: Boolean = false,
    val shouldShowDeleteCard: Boolean = false,
    val preferences: UserInfo
)

enum class BottomSheetType{
    GENDER, DOB, HEIGHT, WEIGHT, ACTIVITYLEVEL, BODYGOAL, NUTRIENTGOAL, TIMERJUMP, TIMERSECONDS
}