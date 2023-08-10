package com.hbaez.user_auth_presentation.user_auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hbaez.user_auth_presentation.common.composable.BasicButton
import kotlinx.coroutines.delay
import com.hbaez.core.R

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.showError.value) {
            Text(text = stringResource(R.string.error_something_went_wrong))

            BasicButton(R.string.try_again, Modifier.fillMaxWidth().padding(16.dp, 8.dp)) { viewModel.onAppStart(openAndPopUp) }
        } else {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onAppStart(openAndPopUp)
    }
}