package com.hbaez.wear_presentation.wear_overview

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.android.gms.wearable.CapabilityClient.OnCapabilityChangedListener
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.hbaez.core.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class WearViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application), OnCapabilityChangedListener{

    lateinit var capabilityClient: CapabilityClient
    lateinit var nodeClient: NodeClient
    lateinit var remoteActivityHelper: RemoteActivityHelper

    var state by mutableStateOf(WearState())
        private set

    var wearNodesWithApp: Set<Node>? = null
    var allConnectedNodes: List<Node>? = null

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        wearNodesWithApp = p0.nodes


        viewModelScope.launch {
            findAllWearDevices()
        }
    }

    suspend fun findAllWearDevices() {
        Log.d(TAG, "findAllWearDevices()")

        try {
            nodeClient.connectedNodes.addOnCompleteListener {
                Log.d(TAG, "ConnectedNodes request succeeded.")
                Log.d(TAG, it.isSuccessful.toString())
                Log.d(TAG, it.isComplete.toString())
                allConnectedNodes = it.result
                updateUI()
            }
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
        } catch (throwable: Throwable) {
            Log.d(TAG, "Node request failed to return any results.")
        }
    }

    suspend fun findWearDevicesWithApp() {
        Log.d(TAG, "findWearDevicesWithApp()")

        try {
            capabilityClient
                .getCapability(CAPABILITY_WEAR_APP, CapabilityClient.FILTER_ALL)
                .addOnCompleteListener {
                    Log.d(TAG, "Capability request succeeded.")
                    wearNodesWithApp = it.result.nodes
                    Log.d(TAG, "Capable Nodes: $wearNodesWithApp")
                    updateUI()
                }
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
            throw cancellationException
        } catch (throwable: Throwable) {
            Log.d(TAG, "Capability request failed to return any results.")
        }
    }

    fun updateUI(){

        val wearNodesWithApp = wearNodesWithApp
        val allConnectedNodes = allConnectedNodes

        when {
            wearNodesWithApp == null || allConnectedNodes == null -> {
                updateUI(false, getApplication<Application>().resources.getString(R.string.message_checking))
            }
            allConnectedNodes.isEmpty() -> {
                updateUI(false, getApplication<Application>().resources.getString(R.string.message_checking))
            }
            wearNodesWithApp.isEmpty() -> {
                updateUI(true, getApplication<Application>().resources.getString(R.string.message_missing_all))
            }
            wearNodesWithApp.size < allConnectedNodes.size -> {
                // TODO: Add your code to communicate with the wear app(s) via Wear APIs
                //       (MessageClient, DataClient, etc.)
                updateUI(true, getApplication<Application>().resources.getString(R.string.message_some_installed, wearNodesWithApp.toString()))
            }
            else -> {
                // TODO: Add your code to communicate with the wear app(s) via Wear APIs
                //       (MessageClient, DataClient, etc.)
                updateUI(false, getApplication<Application>().resources.getString(R.string.message_all_installed, wearNodesWithApp.toString()))
            }
        }
    }

    fun updateUI(showConnectButton: Boolean, informationText: String){
        state = state.copy(
            showConnectButton = showConnectButton,
            informationText = informationText
        )
    }

    fun openPlayStoreOnWearDevicesWithoutApp() {
        Log.d(TAG, "openPlayStoreOnWearDevicesWithoutApp()")

        val wearNodesWithApp = wearNodesWithApp ?: return
        val allConnectedNodes = allConnectedNodes ?: return

        // Determine the list of nodes (wear devices) that don't have the app installed yet.
        val nodesWithoutApp = allConnectedNodes - wearNodesWithApp

        Log.d(TAG, "Number of nodes without app: " + nodesWithoutApp.size)
        val intent = Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse(PLAY_STORE_APP_URI))

        // In parallel, start remote activity requests for all wear devices that don't have the app installed yet.
        nodesWithoutApp.forEach { node ->
            viewModelScope.launch {
                try {
                    remoteActivityHelper
                        .startRemoteActivity(
                            targetIntent = intent,
                            targetNodeId = node.id
                        )
                        .await()

                    Toast.makeText(
                        getApplication(),
                        "Play Store Request to Wear device successful.",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (cancellationException: CancellationException) {
                    // Request was cancelled normally
                } catch (throwable: Throwable) {
                    Toast.makeText(
                        getApplication(),
                        "Play Store Request Failed. Wear device(s) may not support Play Store, that is, the Wear device may be version 1.0.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainMobileActivity"

        // Name of capability listed in Wear app's wear.xml.
        // IMPORTANT NOTE: This should be named differently than your Phone app's capability.
        private const val CAPABILITY_WEAR_APP = "verify_remote_example_wear_app"

        // Links to Wear app (Play Store).
        // TODO: Replace with your links/packages.
        private const val PLAY_STORE_APP_URI =
            "market://details?id=com.example.android.wearable.wear.wearverifyremoteapp"
    }
}