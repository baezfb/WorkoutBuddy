package com.hbaez.user_auth_presentation.user_auth_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.components.BasicButton
import com.hbaez.user_auth_presentation.components.BasicTextButton
import com.hbaez.user_auth_presentation.components.EmailField
import com.hbaez.user_auth_presentation.components.PasswordField
import kotlinx.coroutines.flow.collect

@ExperimentalCoilApi
@Composable
fun UserAuthSignupScreen(
    scaffoldState: ScaffoldState,
    openAndPopUp: (String, String) -> Unit,
    viewModel: UserAuthViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                else -> Unit
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
        Spacer(modifier = Modifier.height(spacing.spaceExtraLarge))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp
                    )
                )
                .background(MaterialTheme.colors.primary)
                .padding(
                    horizontal = spacing.spaceSmall
                )
        ){
            Spacer(modifier = Modifier.height(spacing.spaceExtraLarge))
            Text(
                stringResource(id = R.string.sign_up),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            EmailField(
                value = state.email,
                onNewValue = {
                    viewModel.onEvent(UserAuthEvent.OnEmailFieldChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
            )
            Column(
                Modifier.fillMaxWidth()
            ) {
                PasswordField(
                    value = state.password,
                    onNewValue = {
                        viewModel.onEvent(UserAuthEvent.OnPasswordFieldChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp),
                    placeholder = R.string.password
                )
                PasswordField(
                    value = state.passwordRetyped,
                    onNewValue = {
                        viewModel.onEvent(UserAuthEvent.OnPasswordRetypeFieldChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp),
                    placeholder = R.string.retype_password
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Column(
                Modifier.fillMaxWidth()
            ) {
                BasicButton(
                    R.string.sign_up,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                ) {
                    viewModel.onEvent(UserAuthEvent.OnSignupClick(state.email, state.password, state.passwordRetyped, openAndPopUp))
                }
            }
        }
    }
}