package com.hbaez.settings_presentation.settings_overview

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.user_auth_presentation.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val accountService: AccountService
): ViewModel(){

    val state = accountService.currentUser.map {
        AppSettingsState(it.isAnonymous)
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