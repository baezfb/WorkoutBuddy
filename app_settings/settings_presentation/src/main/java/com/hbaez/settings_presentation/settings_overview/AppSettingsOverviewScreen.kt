package com.hbaez.settings_presentation.settings_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.common.composable.DialogCancelButton
import com.hbaez.user_auth_presentation.common.composable.DialogConfirmButton
import com.hbaez.user_auth_presentation.components.BasicButton

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@Composable
fun AppSettingsOverviewScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    onNavigateToUserAuthWelcome: () -> Unit,
    onNavigateToWear: () -> Unit,
    deleteMyAccount: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val uiState = viewModel.uiState
    val state by viewModel.state.collectAsState(
        initial = AppSettingsState(false, "test")
    )

    if (state.isAnonymous){
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = state.userId, textAlign = TextAlign.Center)
            Text(stringResource(id = R.string.signed_in_as_guest), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            BasicButton(
                R.string.login,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                onNavigateToLogin()
            }
            Text(stringResource(id = R.string.or), textAlign = TextAlign.Center)
            BasicButton(
                R.string.sign_up,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                onNavigateToSignUp()
            }
        }
    } else {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = state.userId, textAlign = TextAlign.Center)
            Text("Settings placeholder", textAlign = TextAlign.Center)
            BasicButton(
                R.string.update_prefs,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                onNavigateToWelcome()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            BasicButton(
                R.string.wear_companion,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                onNavigateToWear()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            BasicButton(
                R.string.logout,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                viewModel.onEvent(AppSettingsEvent.OnLogoutButtonClick)
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            BasicButton(
                R.string.delete_account,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                viewModel.onEvent(AppSettingsEvent.OnDeleteButtonClick)
            }
        }
    }

    if(uiState.shouldShowLogoutCard) { LogoutCard(viewModel = viewModel, onNavigateToUserAuthWelcome) }

    if(uiState.shouldShowDeleteCard) { DeleteMyAccountCard(viewModel = viewModel, deleteMyAccount = deleteMyAccount) }
}

@ExperimentalMaterialApi
@Composable
private fun LogoutCard(viewModel: AppSettingsViewModel, onNavigateToUserAuthWelcome: () -> Unit) {
    AlertDialog(
        title = { Text(stringResource(R.string.logout)) },
        text = { Text(stringResource(R.string.logout_description)) },
        dismissButton = { DialogCancelButton(R.string.cancel) { viewModel.onEvent(AppSettingsEvent.OnLogoutButtonClick) } },
        confirmButton = {
            DialogConfirmButton(R.string.logout) {
                viewModel.onEvent(AppSettingsEvent.OnLogoutButtonClick)
                viewModel.onEvent(AppSettingsEvent.OnSignOut)
                onNavigateToUserAuthWelcome()
            }
        },
        onDismissRequest = { viewModel.onEvent(AppSettingsEvent.OnLogoutButtonClick) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun DeleteMyAccountCard(viewModel: AppSettingsViewModel, deleteMyAccount: () -> Unit) {
    AlertDialog(
        title = { Text(stringResource(R.string.delete_account)) },
        text = { Text(stringResource(R.string.delete_account_description)) },
        dismissButton = { DialogCancelButton(R.string.cancel) { viewModel.onEvent(AppSettingsEvent.OnDeleteButtonClick) } },
        confirmButton = {
            DialogConfirmButton(R.string.delete_account) {
                viewModel.onEvent(AppSettingsEvent.OnDeleteButtonClick)
                viewModel.onEvent(AppSettingsEvent.OnDeleteAccount)
                deleteMyAccount()
            }
        },
        onDismissRequest = { viewModel.onEvent(AppSettingsEvent.OnDeleteButtonClick) }
    )
}