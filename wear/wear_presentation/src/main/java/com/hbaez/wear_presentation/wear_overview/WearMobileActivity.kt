package com.hbaez.wear_presentation.wear_overview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.wear.remote.interactions.RemoteActivityHelper
import coil.annotation.ExperimentalCoilApi
import com.google.android.gms.wearable.CapabilityClient.OnCapabilityChangedListener
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import com.hbaez.wear_presentation.wear_overview.ui.theme.WorkoutBuddyTheme
import kotlinx.coroutines.launch

@ExperimentalCoilApi
class WearMobileActivity: ComponentActivity(), OnCapabilityChangedListener {

    private lateinit var viewModel: WearViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[WearViewModel::class.java]

        viewModel.capabilityClient = Wearable.getCapabilityClient(this)
        viewModel.nodeClient = Wearable.getNodeClient(this)
        viewModel.remoteActivityHelper = RemoteActivityHelper(this)

        setContent {
            WorkoutBuddyTheme{
                WearOverviewScreen()
            }
        }

        // Perform the initial update of the UI
        viewModel.updateUI()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    // Initial request for devices with our capability, aka, our Wear app installed.
                    viewModel.findWearDevicesWithApp()
                }
                launch {
                    // Initial request for all Wear devices connected (with or without our capability).
                    // Additional Note: Because there isn't a listener for ALL Nodes added/removed from network
                    // that isn't deprecated, we simply update the full list when the Google API Client is
                    // connected and when capability changes come through in the onCapabilityChanged() method.
                    viewModel.findAllWearDevices()
                }
            }
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
        viewModel.capabilityClient.removeListener(this, CAPABILITY_WEAR_APP)
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        viewModel.capabilityClient.addListener(this, CAPABILITY_WEAR_APP)
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        viewModel.wearNodesWithApp = p0.nodes

        lifecycleScope.launch {
            // Because we have an updated list of devices with/without our app, we need to also update
            // our list of active Wear devices.
            viewModel.findAllWearDevices()
        }
    }

    companion object {
        private const val TAG = "WearMobileActivity"

        // Name of capability listed in Wear app's wear.xml.
        // IMPORTANT NOTE: This should be named differently than your Phone app's capability.
        private const val CAPABILITY_WEAR_APP = "verify_remote_example_wear_app"

        // Links to Wear app (Play Store).
        // TODO: Replace with your links/packages.
        private const val PLAY_STORE_APP_URI =
            "market://details?id=com.example.android.wearable.wear.wearverifyremoteapp"
    }
}