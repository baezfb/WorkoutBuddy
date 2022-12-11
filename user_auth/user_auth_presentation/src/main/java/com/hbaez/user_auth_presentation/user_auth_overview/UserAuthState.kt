package com.hbaez.user_auth_presentation.user_auth_overview

data class UserAuthState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordRetyped: String = ""
)
