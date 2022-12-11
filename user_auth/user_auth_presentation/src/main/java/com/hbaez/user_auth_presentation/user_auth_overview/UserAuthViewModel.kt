package com.hbaez.user_auth_presentation.user_auth_overview

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.hbaez.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

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
            is UserAuthEvent.OnForgotPasswordClick -> {
                /*TODO*/
            }
            is UserAuthEvent.OnLoginClick -> {
                /*TODO*/
            }
        }
    }
}