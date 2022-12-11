package com.hbaez.user_auth_presentation.user_auth_overview

sealed class UserAuthEvent {
    data class OnEmailFieldChange(val email: String): UserAuthEvent()
    data class OnPasswordFieldChange(val password: String): UserAuthEvent()
    object OnForgotPasswordClick: UserAuthEvent()
    data class OnLoginClick(val email: String, val password: String): UserAuthEvent()
}

