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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.onboarding_presentation.activity.ActivityScreen
import com.hbaez.onboarding_presentation.age.AgeScreen
import com.hbaez.onboarding_presentation.gender.GenderScreen
import com.hbaez.onboarding_presentation.goal.GoalScreen
import com.hbaez.onboarding_presentation.height.HeightScreen
import com.hbaez.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.hbaez.onboarding_presentation.weight.WeightScreen
import com.hbaez.settings_presentation.settings_overview.components.TimerBottomSheet
import com.hbaez.user_auth_presentation.common.composable.DialogCancelButton
import com.hbaez.user_auth_presentation.common.composable.DialogConfirmButton
import com.hbaez.user_auth_presentation.components.BasicButton
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

@ExperimentalCoilApi
@Composable
fun AppSettingsOverviewScreen(
    snackBarHost: SnackbarHostState,
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
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    /*DO NOTHING*/
                }
                is UiEvent.ShowSnackbar -> {
                    snackBarHost.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                else -> Unit
            }
        }
    }
    val uiState = viewModel.uiState
    val state by viewModel.state.collectAsState(
        initial = AppSettingsState(false, "test")
    )
    val showBottomSheet = remember { mutableStateOf(false) }
    val bottomSheetType = remember { mutableStateOf(BottomSheetType.GENDER) }

    if(showBottomSheet.value){
        BottomSheet(showBottomSheet, bottomSheetType.value, snackBarHost, viewModel)
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
                value = uiState.preferences.gender.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.GENDER
            }
            SettingsButton(
                text = R.string.dob,
                value = Instant.ofEpochMilli(uiState.preferences.age).atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.DOB
            }
            SettingsButton(
                text = R.string.height,
                value = "${uiState.preferences.height / 12} ft ${uiState.preferences.height % 12} in",
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.HEIGHT
            }
            SettingsButton(
                text = R.string.weight,
                value =  uiState.preferences.weight.toString() + " " + stringResource(id = R.string.lbs),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.WEIGHT
            }
            SettingsButton(
                text = R.string.activity_level,
                value = uiState.preferences.activityLevel.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                } /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.ACTIVITYLEVEL
            }
            SettingsButton(
                text = R.string.body_goal,
                value = uiState.preferences.goalType.name.replace("_", " ")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.BODYGOAL
            }
            SettingsButton(
                text = R.string.nutrient_goal,
                value = "${(uiState.preferences.carbRatio*100).toInt()}%, ${(uiState.preferences.proteinRatio*100).toInt()}%, ${(uiState.preferences.fatRatio*100).toInt()}%" /*TODO*/,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.NUTRIENTGOAL
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
                value = "${uiState.preferences.timerJump} s",
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.TIMERJUMP
            }
            SettingsButton(
                text = R.string.default_timer_secs,
                value = "${uiState.preferences.timerSeconds} s",
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                showBottomSheet.value = !showBottomSheet.value
                bottomSheetType.value = BottomSheetType.TIMERSECONDS
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
private fun BottomSheet(showBottomSheet: MutableState<Boolean>, bottomSheetType: BottomSheetType, snackBarHost: SnackbarHostState, viewModel: AppSettingsViewModel){
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(0.65f),
        onDismissRequest = {
            showBottomSheet.value = !showBottomSheet.value
        }) {
        when(bottomSheetType){
            BottomSheetType.GENDER -> {
                GenderScreen(onNextClick = {
                    showBottomSheet.value = !showBottomSheet.value
                    viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                })
            }
            BottomSheetType.DOB -> {
                AgeScreen(
                    snackBarHost = snackBarHost,
                    onNextClick = {
                        showBottomSheet.value = !showBottomSheet.value
                        viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                    }
                )
            }
            BottomSheetType.HEIGHT -> {
                HeightScreen(
                    snackBarHost = snackBarHost,
                    onNextClick = {
                        showBottomSheet.value = !showBottomSheet.value
                        viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                    }
                )
            }
            BottomSheetType.WEIGHT -> {
                WeightScreen(
                    snackBarHost = snackBarHost,
                    onNextClick = {
                        showBottomSheet.value = !showBottomSheet.value
                        viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                    }
                )
            }
            BottomSheetType.ACTIVITYLEVEL -> {
                ActivityScreen(onNextClick = {
                    showBottomSheet.value = !showBottomSheet.value
                    viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                })
            }
            BottomSheetType.BODYGOAL -> {
                GoalScreen(onNextClick = {
                    showBottomSheet.value = !showBottomSheet.value
                    viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                })
            }
            BottomSheetType.NUTRIENTGOAL -> {
                NutrientGoalScreen(
                    snackBarHost = snackBarHost,
                    onNextClick = {
                        showBottomSheet.value = !showBottomSheet.value
                        viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                    }
                )
            }
            BottomSheetType.TIMERJUMP -> {
                TimerBottomSheet(
                    onNextClick = { time ->
                        showBottomSheet.value = !showBottomSheet.value
                        viewModel.onEvent(AppSettingsEvent.UpdateTimer(time, true))
                        viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                    },
                    isJump = true
                )
            }
            BottomSheetType.TIMERSECONDS -> {
                TimerBottomSheet(
                    onNextClick = { time ->
                        showBottomSheet.value = !showBottomSheet.value
                        viewModel.onEvent(AppSettingsEvent.UpdateTimer(time, false))
                        viewModel.onEvent(AppSettingsEvent.RefreshPreferences)
                    },
                    isJump = false
                )
            }
        }
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