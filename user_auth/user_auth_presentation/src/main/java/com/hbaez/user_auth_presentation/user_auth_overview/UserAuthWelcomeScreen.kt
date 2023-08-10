package com.hbaez.user_auth_presentation.user_auth_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.hbaez.user_auth_presentation.components.Button
import com.hbaez.user_auth_presentation.components.FlatButton
import com.hbaez.user_auth_presentation.components.OutlineButton

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
    /**
     * TODO: Remove unused variables
     */
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(spacing.spaceMedium),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            stringResource(id = R.string.welcome_screen_title),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 20.dp, bottom = 20.dp)
        )
        Text(
            stringResource(id = R.string.welcome_screen_subtitle),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(start = 20.dp, bottom = 5.dp)
        )

        Text(
            stringResource(id = R.string.welcome_screen_subtitle2),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
        )

        OutlineButton(
            R.string.sign_in_with_google,
            Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        ) {
            // TODO: Google sign in implementation
        }

        FlatButton(
            R.string.sign_up,
            Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            onNavigateToSignUp()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Already have an account?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp, end = 10.dp, top = 30.dp)
            )
            Text(
                stringResource(id = R.string.sign_in),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onNavigateToLogin()
                    }
                    .padding(start = 10.dp, end = 20.dp, top = 30.dp),
            )
        }

        Text(
            stringResource(id = R.string.continue_guest),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 30.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onNavigateToHome()
                }
        )
    }
}