package com.hbaez.workoutbuddy.user_auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hbaez.core.R
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import com.hbaez.user_auth_presentation.user_auth_overview.isValidEmail
import com.hbaez.workoutbuddy.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: Preferences,
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService
): AuthViewModel(logService) {

    var state by mutableStateOf(LoginState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.OnEmailFieldChange -> {
                state = state.copy(
                    email = event.email
                )
            }
            is LoginEvent.OnPasswordFieldChange -> {
                state = state.copy(
                    password = event.password
                )
            }
            is LoginEvent.OnLoginClick -> {
                launchCatching {
                    accountService.authenticate(event.email, event.password)
                    preferences.updateUserInfo(storageService.getUserInfo()!!, accountService.currentUserId)

                    event.openAndPopUp(Route.VERIFY_MOBILE_APP, Route.LOGIN)
                }
            }
        }
    }
}