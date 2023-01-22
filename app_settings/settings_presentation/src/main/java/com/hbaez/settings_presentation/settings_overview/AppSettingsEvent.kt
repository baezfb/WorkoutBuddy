package com.hbaez.settings_presentation.settings_overview

sealed class AppSettingsEvent{
    object OnSignupClick: AppSettingsEvent()
    object OnLoginClick: AppSettingsEvent()
//    data class OnEmailFieldChange(val email: String): UserAuthEvent()
    object OnLogoutButtonClick: AppSettingsEvent()
    object OnSignOut: AppSettingsEvent()
}
