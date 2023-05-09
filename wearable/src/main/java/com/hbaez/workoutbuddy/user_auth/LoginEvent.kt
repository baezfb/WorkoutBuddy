package com.hbaez.workoutbuddy.user_auth

sealed class LoginEvent {
    data class OnEmailFieldChange(val email: String): LoginEvent()
    data class OnPasswordFieldChange(val password: String): LoginEvent()
    data class OnLoginClick(val email: String, val password: String, val openAndPopUp: (String, String) -> Unit): LoginEvent()

}
