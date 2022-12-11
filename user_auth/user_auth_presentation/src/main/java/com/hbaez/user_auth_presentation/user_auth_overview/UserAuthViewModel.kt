package com.hbaez.user_auth_presentation.user_auth_overview

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
import com.hbaez.core.R

@HiltViewModel
class UserAuthViewModel @Inject constructor(
//    preferences: Preferences, // need user_auth_domain module
): ViewModel() {
    var state by mutableStateOf(UserAuthState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: UserAuthEvent) {
        when(event) {
            is UserAuthEvent.OnEmailFieldChange -> {
                state = state.copy(
                    email = event.email
                )
            }
            is UserAuthEvent.OnPasswordFieldChange -> {
                state = state.copy(
                    password = event.password
                )
            }
            is UserAuthEvent.OnPasswordRetypeFieldChange -> {
                state = state.copy(
                    passwordRetyped = event.passwordRetyped
                )
            }
            is UserAuthEvent.OnForgotPasswordClick -> {
                /*TODO*/
            }
            is UserAuthEvent.OnLoginClick -> {
                /*TODO*/
            }
            is UserAuthEvent.OnSignupClick -> {
                if (!event.email.isValidEmail()) {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.invalid_email)))
                    }
                    return
                }

                if (!event.password.isValidPassword()) {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.invalid_password)))
                    }
                    return
                }

                if (!event.password.passwordMatches(event.passwordRetyped)) {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.mismatched_password)))
                    }
                    return
                }
            }
        }
    }
}

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}