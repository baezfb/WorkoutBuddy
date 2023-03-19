package com.hbaez.wear_presentation.wear_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.wear_presentation.wear_overview.components.AddButton

@ExperimentalCoilApi
@Composable
fun WearOverviewScreen(
    viewModel: WearViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Text(
                modifier = Modifier.padding(spacing.spaceMedium),
                text = stringResource(id = R.string.workout_buddy_mobile_wear_screen),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h2,
            )
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = state.informationText,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(spacing.spaceLarge))
                AddButton(
                    text = stringResource(id = R.string.install_app),
                    onClick = {
                        viewModel.openPlayStoreOnWearDevicesWithoutApp()
                    }
                )
            }
        }
    )
}