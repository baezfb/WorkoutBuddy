package com.hbaez.user_auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.user_auth_presentation.common.snackbar.SnackbarManager
import com.hbaez.user_auth_presentation.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.hbaez.user_auth_presentation.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class AuthViewModel(private val logService: LogService): ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}