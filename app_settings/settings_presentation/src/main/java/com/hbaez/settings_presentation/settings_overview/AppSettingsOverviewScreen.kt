package com.hbaez.settings_presentation.settings_overview

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.onboarding_presentation.gender.GenderScreen
import com.hbaez.user_auth_presentation.common.composable.DialogCancelButton
import com.hbaez.user_auth_presentation.common.composable.DialogConfirmButton
import com.hbaez.user_auth_presentation.components.BasicButton

@OptIn(ExperimentalMaterial3Api::class)
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
    val showBottomSheet = remember { mutableStateOf(false) }

    if(showBottomSheet.value){
        BottomSheet(showBottomSheet)
    }
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
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = spacing.spaceMedium),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
//                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                Text(
                    stringResource(id = R.string.settings),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = spacing.spaceMedium),
                    style = MaterialTheme.typography.displayLarge
                )
            }
//            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(spacing.spaceLarge))

            Text(
                stringResource(id = R.string.user),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = spacing.spaceMedium)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall
            )
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /*TODO: copy user ID to clipboard*/ }
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.uuid),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraLarge))
                Text(
                    text = state.userId,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))

            SettingsButton(
                text = R.string.gender,
                value = "Male" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
            }
            SettingsButton(
                text = R.string.dob,
                value = "03/24/2000" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            SettingsButton(
                text = R.string.height,
                value = "5 ft 9 in" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            SettingsButton(
                text = R.string.weight,
                value = "185 lbs" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            SettingsButton(
                text = R.string.activity_level,
                value = "Low" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            SettingsButton(
                text = R.string.body_goal,
                value = "Lose" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            SettingsButton(
                text = R.string.nutrient_goal,
                value = "40%, 30%, 30%" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }


            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            Text(
                stringResource(id = R.string.exercise_defaults),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = spacing.spaceMedium)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall
            )
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            SettingsButton(
                text = R.string.timer_jump,
                value = "5 s" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            SettingsButton(
                text = R.string.default_timer_secs,
                value = "60 s" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }


            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            Text(
                stringResource(id = R.string.watch),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = spacing.spaceMedium)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall
            )
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            SettingsButton(text = R.string.wear_companion) {
                onNavigateToWear()
            }


            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            Text(
                stringResource(id = R.string.manage_data),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = spacing.spaceMedium)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall
            )
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            SettingsButton(text = R.string.export_data) {
                /*TODO: Feature to export / import CSV data of workouts*/
            }


            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            Text(
                stringResource(id = R.string.account),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = spacing.spaceMedium)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall
            )
            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            SettingsButton(text = R.string.logout) {
                viewModel.onEvent(AppSettingsEvent.OnLogoutButtonClick)
            }
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            SettingsButton(text = R.string.delete_account, horizontalArrangement = Arrangement.Center, color = MaterialTheme.colorScheme.error) {
                viewModel.onEvent(AppSettingsEvent.OnDeleteButtonClick)
            }
//            SettingsButton(text = R.string.update_prefs) {
//                onNavigateToWelcome()
//            }
            Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
        }
    }

    if(uiState.shouldShowLogoutCard) { LogoutCard(viewModel = viewModel, onNavigateToUserAuthWelcome) }

    if(uiState.shouldShowDeleteCard) { DeleteMyAccountCard(viewModel = viewModel, deleteMyAccount = deleteMyAccount) }
}

@Composable
fun SettingsButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    value: String = "",
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
            style = MaterialTheme.typography.headlineMedium,
            color = color
        )
        if(value.isNotEmpty()){
            Spacer(modifier = Modifier.width(spacing.spaceExtraLarge))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(showBottomSheet: MutableState<Boolean>){
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(0.75f),
        onDismissRequest = {
            showBottomSheet.value = !showBottomSheet.value
        }) {
        GenderScreen(onNextClick = {
            /*TODO UPDATE USER PREFERENCES*/
            showBottomSheet.value = !showBottomSheet.value
        })
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