package com.hbaez.workoutbuddy.user_auth

import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.ConfigurationService
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.workoutbuddy.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : AuthViewModel(logService) {
    init {
        launchCatching { configurationService.fetchConfiguration() }
    }
    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountService.hasUser) openAndPopUp(Route.VERIFY_MOBILE_APP, Route.SPLASH)
        else openAndPopUp(Route.LOGIN, Route.SPLASH)
    }
}