package com.hbaez.settings_presentation.settings_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.hbaez.user_auth_presentation.components.BasicButton
import com.hbaez.user_auth_presentation.user_auth_overview.UserAuthEvent

@ExperimentalCoilApi
@Composable
fun AppSettingsOverviewScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    if (state.hasAccount){
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Settings placeholder", textAlign = TextAlign.Center)
        }
        TODO()
    } else {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
    }
}