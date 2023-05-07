package com.hbaez.wear_presentation.wear_overview

import android.util.Log
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
import com.google.android.gms.wearable.Node
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.wear_presentation.wear_overview.components.AddButton

@ExperimentalCoilApi
@Composable
fun WearOverviewScreen(
//    viewModel: WearViewModel = hiltViewModel()
    wearNodesWithApp:Set<Node>?,
    allConnectedNodes:List<Node>?,
    openPlayStore: () -> Unit
){
    val spacing = LocalSpacing.current
//    val state = viewModel.state
    val context = LocalContext.current

    val textInfo: Pair<String, Boolean>

    when {
        wearNodesWithApp == null || allConnectedNodes == null -> {
            Log.d("wear composable", "Waiting on Results for both connected nodes and nodes with app")
            textInfo = Pair(stringResource(R.string.message_checking), false)
//                                            binding.informationTextView.text = getString(R.string.message_checking)
//                                            binding.remoteOpenButton.isInvisible = true
        }
        allConnectedNodes.isEmpty() -> {
            Log.d("wear composable", "No devices")
            textInfo = Pair(stringResource(R.string.message_checking), false)
//                                            binding.informationTextView.text = getString(R.string.message_checking)
//                                            binding.remoteOpenButton.isInvisible = true
        }
        wearNodesWithApp.isEmpty() -> {
            Log.d("wear composable", "Missing on all devices")
            textInfo = Pair(stringResource(R.string.message_missing_all), true)
//                                            binding.informationTextView.text = getString(R.string.message_missing_all)
//                                            binding.remoteOpenButton.isVisible = true
        }
        wearNodesWithApp.size < allConnectedNodes.size -> {
            // TODO: Add your code to communicate with the wear app(s) via Wear APIs
            //       (MessageClient, DataClient, etc.)
            Log.d("wear composable", "Installed on some devices")
            textInfo = Pair(stringResource(R.string.message_some_installed, wearNodesWithApp.toString()), true)
//                                            binding.informationTextView.text =
//                                                getString(R.string.message_some_installed, wearNodesWithApp.toString())
//                                            binding.remoteOpenButton.isVisible = true
        }
        else -> {
            // TODO: Add your code to communicate with the wear app(s) via Wear APIs
            //       (MessageClient, DataClient, etc.)
            Log.d("wear composable", "Installed on all devices")
            textInfo = Pair(stringResource(R.string.message_all_installed, wearNodesWithApp.toString()), false)
//                                            binding.informationTextView.text = getString(R.string.message_all_installed, wearNodesWithApp.toString())
//                                            binding.remoteOpenButton.isInvisible = true
        }
    }

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
                    text = textInfo.first,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                )
                if(textInfo.second){
                    Spacer(modifier = Modifier.height(spacing.spaceLarge))
                    AddButton(
                        text = stringResource(id = R.string.install_app),
                        onClick = {
                            openPlayStore()
//                        viewModel.openPlayStoreOnWearDevicesWithoutApp()
                        }
                    )
                }
            }
        }
    )
}