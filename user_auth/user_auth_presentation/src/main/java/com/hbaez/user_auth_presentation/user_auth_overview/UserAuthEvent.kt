package com.hbaez.user_auth_presentation.user_auth_overview

sealed class UserAuthEvent {
    data class OnEmailFieldChange(val email: String): UserAuthEvent()
    data class OnPasswordFieldChange(val password: String): UserAuthEvent()
    data class OnPasswordRetypeFieldChange(val passwordRetyped: String): UserAuthEvent()
    data class OnForgotPasswordClick(val email: String): UserAuthEvent()
    data class OnLoginClick(val email: String, val password: String, val openAndPopUp: (String, String) -> Unit): UserAuthEvent()
    data class OnSignupClick(val email: String, val password: String, val passwordRetyped: String, val openAndPopUp: (String, String) -> Unit): UserAuthEvent()
}

