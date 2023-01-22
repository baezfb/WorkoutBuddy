package com.hbaez.settings_presentation.settings_overview

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.user_auth_presentation.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val accountService: AccountService
): ViewModel(){

    var state by mutableStateOf(AppSettingsState())
        private set
    init {
        viewModelScope.launch {
            state = if(!accountService.currentUser.first().isAnonymous) {
                state.copy(
                    hasAccount = true
                )
            } else {
                state.copy(
                    hasAccount = false
                )
            }
        }
    }

    fun onEvent(event: AppSettingsEvent){
        when(event) {
            is AppSettingsEvent.OnLoginClick -> {
                TODO()
            }

            is AppSettingsEvent.OnSignupClick -> {
                TODO()
            }
        }
    }
}