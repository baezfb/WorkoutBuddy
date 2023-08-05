package com.hbaez.user_auth_presentation.user_auth_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core.R
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.user_auth_presentation.components.BasicButton
import com.hbaez.user_auth_presentation.components.BasicTextButton
import com.hbaez.user_auth_presentation.components.Button

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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .background(MaterialTheme.colors.primary)
                .padding(
                    horizontal = spacing.spaceSmall,
                    vertical = spacing.spaceSmall
                ), verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                "Welcome",
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 20.dp)
            )
            Text(
                "Manage your workouts and your progress",
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 5.dp)
            )
            Text(
                "seamlessly and intuitively",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
            )
//            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            Button(
                R.string.sign_in_with_google,
                Modifier
                    .fillMaxWidth(100f)
            ) {
                onNavigateToLogin()
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Button(
                R.string.sign_up,
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colors.onPrimary, RoundedCornerShape(10.dp))
            ) {
                onNavigateToSignUp()
            }
            Text(
                "Already have an account?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            )
            BasicTextButton(
                R.string.continue_guest,
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 30.dp)
            ) {
                onNavigateToHome()
            }
        }
    }
}