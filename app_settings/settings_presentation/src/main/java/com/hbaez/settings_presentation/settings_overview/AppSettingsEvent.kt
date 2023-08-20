package com.hbaez.settings_presentation.settings_overview

sealed class AppSettingsEvent{
//    data class OnEmailFieldChange(val email: String): UserAuthEvent()
    object OnLogoutButtonClick: AppSettingsEvent()
    object OnSignOut: AppSettingsEvent()
    object OnDeleteButtonClick: AppSettingsEvent()
    object OnDeleteAccount: AppSettingsEvent()
    object RefreshPreferences: AppSettingsEvent()
    data class UpdateTimer(val time: Int, val isJump: Boolean): AppSettingsEvent()
}
