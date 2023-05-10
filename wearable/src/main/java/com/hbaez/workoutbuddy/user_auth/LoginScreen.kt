package com.hbaez.workoutbuddy.user_auth

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.compose.material.scrollAway
import androidx.wear.input.wearableExtender
import com.hbaez.workoutbuddy.R
import com.hbaez.workoutbuddy.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Login
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import com.hbaez.core_ui.LocalSpacing

private const val SPLASH_TIMEOUT = 1000L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier
){
    val spacing = LocalSpacing.current
    val state =  viewModel.state

    val inputTextKey1 = "input_email"
    val inputTextKey2 = "input_password"

    val remoteInputs: List<RemoteInput> = listOf(
        RemoteInput.Builder(inputTextKey1)
            .setLabel(stringResource(R.string.email))
            .wearableExtender {
                setEmojisAllowed(false)
                setInputActionType(EditorInfo.IME_ACTION_NEXT)
            }.build(),
        RemoteInput.Builder(inputTextKey2)
            .setLabel(stringResource(R.string.password))
            .wearableExtender {
                setEmojisAllowed(false)
                setInputActionType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                setInputActionType(EditorInfo.IME_ACTION_GO)
            }
            .build(),
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.let { data ->
            val results: Bundle = RemoteInput.getResultsFromIntent(data)
            val emailText: CharSequence = results.getCharSequence(inputTextKey1) ?: ""
            val passwordText: CharSequence = results.getCharSequence(inputTextKey2) ?: ""
            viewModel.onEvent(LoginEvent.OnEmailFieldChange(emailText.toString()))
            viewModel.onEvent(LoginEvent.OnPasswordFieldChange(passwordText.toString()))
        }
    }

    val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
    RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                launcher.launch(intent)
                viewModel.onEvent(
                    LoginEvent.OnLoginClick(
                        state.email,
                        state.password,
                        openAndPopUp
                    )
                )
                      },
            colors = ButtonDefaults.secondaryButtonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.width(spacing.spaceLarge))
                Icon(
                    imageVector = Icons.Filled.Login,
                    contentDescription = stringResource(id = R.string.login)
                )
            }
        }
    }
}