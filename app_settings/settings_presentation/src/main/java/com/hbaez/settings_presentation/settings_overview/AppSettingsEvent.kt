package com.hbaez.settings_presentation.settings_overview

sealed class AppSettingsEvent{

    object OnSignupClick: AppSettingsEvent()

    object OnLoginClick: AppSettingsEvent()
}
