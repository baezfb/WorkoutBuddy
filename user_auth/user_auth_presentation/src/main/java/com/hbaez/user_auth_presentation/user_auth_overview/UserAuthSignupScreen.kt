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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core.R
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.components.BasicTextButton
import com.hbaez.user_auth_presentation.components.Button
import com.hbaez.user_auth_presentation.components.EmailField
import com.hbaez.user_auth_presentation.components.PasswordField

@ExperimentalCoilApi
@Composable
fun UserAuthSignupScreen(
    snackBarHost: SnackbarHostState,
    openAndPopUp: (String, String) -> Unit,
    viewModel: UserAuthViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackBarHost.showSnackbar(
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
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        //TODO: add back button
        Text(
            stringResource(id = R.string.sign_up),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(16.dp, 4.dp)
        )

        Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp
                    )
                )
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    horizontal = spacing.spaceSmall
                )
        ) {
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceSmall),
            ) {
                Text(
                    stringResource(id = R.string.sign_up_header),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayMedium,
                )
                Text(
                    stringResource(id = R.string.sign_up_subheader),
                    color = Color.Gray,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(spacing.spaceLarge))

                EmailField(
                    value = state.email,
                    onNewValue = {
                        viewModel.onEvent(UserAuthEvent.OnEmailFieldChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                PasswordField(
                    value = state.password,
                    onNewValue = {
                        viewModel.onEvent(UserAuthEvent.OnPasswordFieldChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = R.string.password
                )
                PasswordField(
                    value = state.passwordRetyped,
                    onNewValue = {
                        viewModel.onEvent(UserAuthEvent.OnPasswordRetypeFieldChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = R.string.retype_password
                )

                Spacer(modifier = Modifier.height(spacing.spaceMedium))

                Button(
                    R.string.sign_up,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                ) {
                    viewModel.onEvent(
                        UserAuthEvent.OnSignupClick(
                            state.email,
                            state.password,
                            state.passwordRetyped,
                            openAndPopUp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(spacing.spaceLarge))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                    )
                    
                    BasicTextButton(text = R.string.sign_in, modifier = Modifier.padding(start = spacing.spaceSmall)) {
                        //TODO: navigate to sign in screen
                    }
                }
            }
        }
    }
}