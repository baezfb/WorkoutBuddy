package com.hbaez.settings_presentation.settings_overview

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(
                "Settings",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = spacing.spaceMedium)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /*TODO: copy user ID to clipboard*/ }
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unique User ID: ${state.userId}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            SettingsButton(text = R.string.update_prefs) {
                onNavigateToWelcome()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            SettingsButton(text = R.string.wear_companion) {
                onNavigateToWear()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            SettingsButton(text = R.string.manage_data) {
                /*TODO: Feature to export / import CSV data of workouts*/
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            SettingsButton(text = R.string.logout) {
                viewModel.onEvent(AppSettingsEvent.OnLogoutButtonClick)
            }
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            SettingsButton(text = R.string.delete_account, horizontalArrangement = Arrangement.Center, color = MaterialTheme.colorScheme.error) {
                viewModel.onEvent(AppSettingsEvent.OnDeleteButtonClick)
            }
        }
    }

    if(uiState.shouldShowLogoutCard) { LogoutCard(viewModel = viewModel, onNavigateToUserAuthWelcome) }

    if(uiState.shouldShowDeleteCard) { DeleteMyAccountCard(viewModel = viewModel, deleteMyAccount = deleteMyAccount) }
}

@Composable
fun SettingsButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    color: Color = MaterialTheme.colorScheme.onBackground,
    action: () -> Unit
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                action()
            }
            .padding(spacing.spaceMedium),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
    }
}

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