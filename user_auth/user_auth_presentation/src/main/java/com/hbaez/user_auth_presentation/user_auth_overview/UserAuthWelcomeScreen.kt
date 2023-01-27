package com.hbaez.user_auth_presentation.user_auth_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.components.BasicButton
import com.hbaez.user_auth_presentation.components.BasicTextButton

@ExperimentalCoilApi
@Composable
fun UserAuthWelcomeScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserAuthViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(spacing.spaceExtraExtraLarge))
        Spacer(modifier = Modifier.height(spacing.spaceExtraLarge))
        Column(
            modifier = modifier
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
        ) {
            Spacer(modifier = Modifier.height(spacing.spaceExtraLarge))
            Text(
                stringResource(id = R.string.workout_buddy),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            BasicButton(
                R.string.login,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                onNavigateToLogin()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            BasicButton(
                R.string.sign_up,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp)
            ) {
                onNavigateToSignUp()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            BasicTextButton(
                R.string.continue_guest,
                Modifier
//                            .align(Alignment.CenterVertically)
                    .padding(16.dp, 4.dp, 16.dp, 0.dp)
            ) {
                onNavigateToHome()
            }
        }
    }
}