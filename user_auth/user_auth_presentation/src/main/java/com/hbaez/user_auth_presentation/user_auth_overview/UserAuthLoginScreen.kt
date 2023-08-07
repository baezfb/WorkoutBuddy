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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.hbaez.user_auth_presentation.components.BasicTextButton
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.user_auth_presentation.components.BasicButton
import com.hbaez.user_auth_presentation.components.Button
import com.hbaez.user_auth_presentation.components.EmailField
import com.hbaez.user_auth_presentation.components.PasswordField
import com.hbaez.user_auth_presentation.components.FlatButton


@ExperimentalCoilApi
@Composable
fun UserAuthLoginScreen(
    scaffoldState: ScaffoldState,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserAuthViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .verticalScroll(rememberScrollState())
    ) {
//        Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
//        Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        //TODO: add back button
        Text(
            stringResource(id = R.string.sign_in),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .padding(16.dp, 4.dp)
        )
        Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp
                    )
                )
                .background(Color.Black)
                .padding(
                    horizontal = spacing.spaceMedium
                )
        ) {
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceSmall)
            ) {
                Text(
                    stringResource(id = R.string.login_header),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h2,
                )
                Text(
                    stringResource(id = R.string.login_subheader),
                    color = Color.Gray,
                    style = MaterialTheme.typography.h4,
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

                Spacer(modifier = Modifier.height(spacing.spaceMedium))

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

                BasicTextButton(
                    R.string.forgot_password,
                    Modifier
                        .padding(top = spacing.spaceSmall)
                ) {
                    viewModel.onEvent(UserAuthEvent.OnForgotPasswordClick(state.email))
                }

                Spacer(modifier = Modifier.height(spacing.spaceMedium))

                Column(
                    Modifier.fillMaxWidth()
                ) {
                    BasicButton(
                        R.string.login,
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp)
                    ) {
                        viewModel.onEvent(
                            UserAuthEvent.OnLoginClick(
                                state.email,
                                state.password,
                                openAndPopUp
                            )
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = spacing.spaceSmall),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_account),
                            modifier = Modifier
//                            .align(Alignment.CenterVertically)
                        )
                        BasicTextButton(
                            R.string.sign_up,
                            Modifier
//                            .align(Alignment.CenterVertically)
                                .padding(16.dp, 4.dp, 16.dp, 0.dp)
                        ) {
                            viewModel.onEvent(UserAuthEvent.OnEmailFieldChange(""))
                            viewModel.onEvent(UserAuthEvent.OnPasswordFieldChange(""))
                            onNavigateToSignUp()
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))

        }
    }
}