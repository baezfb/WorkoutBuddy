package com.hbaez.workoutbuddy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.remote.interactions.RemoteActivityHelper
import coil.annotation.ExperimentalCoilApi
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.workoutbuddy.navigation.Route
import com.hbaez.workoutbuddy.ui.theme.WorkoutBuddyWearableTheme
import com.hbaez.workoutbuddy.user_auth.LoginScreen
import com.hbaez.workoutbuddy.user_auth.SplashScreen
import com.hbaez.workoutbuddy.workout.WorkoutOverviewScreen
import com.hbaez.workoutbuddy.workout.start_workout.StartWorkoutScreen
import com.hbaez.workoutbuddy.workout.timer.TimerScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainWearableActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {

    @Inject
    lateinit var preferences: Preferences

    private lateinit var capabilityClient: CapabilityClient
    private lateinit var remoteActivityHelper: RemoteActivityHelper

    private var androidPhoneNodeWithApp: Node? = null

    private inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {
        // Override ambient mode lifecycle methods here
        // For example: onEnterAmbient(), onUpdateAmbient(), onExitAmbient(), etc.
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()
    private lateinit var ambientController: AmbientModeSupport.AmbientController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        capabilityClient = Wearable.getCapabilityClient(this)
        remoteActivityHelper = RemoteActivityHelper(this)

        ambientController = AmbientModeSupport.attach(this)



        setContent {
            WorkoutBuddyWearableTheme {
//                val listState = rememberScalingLazyListState()
//                val focusRequester = remember { FocusRequester() }
//                val coroutineScope = rememberCoroutineScope()
                val navController = rememberSwipeDismissableNavController()

//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentRoute = navBackStackEntry?.destination?.route
                SwipeDismissableNavHost(
                    navController = navController,
                    startDestination = Route.SPLASH
                ) {
                    composable(Route.SPLASH){
                        SplashScreen(
                            openAndPopUp = { route, popup ->
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    popUpTo(popup) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Route.LOGIN){
                        LoginScreen(
                            openAndPopUp = { route, popup ->
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    popUpTo(popup) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Route.HOME){
                        WorkoutOverviewScreen(
                            onNavigateToWorkout = { workoutName, day, month, year, workoutIds ->
                                navController.navigate(
                                    Route.START_WORKOUT +
                                        "/$workoutName" +
                                        "/$day" +
                                        "/$month" +
                                        "/$year" +
                                        "/$workoutIds"
                                )
                            }
                        )
                    }
                    composable(
                        route = Route.START_WORKOUT +
                                "/{workoutName}" +
                                "/{dayOfMonth}" +
                                "/{month}" +
                                "/{year}" +
                                "/{workoutIds}",
                        arguments = listOf(
                            navArgument("workoutName") {
                                type = NavType.StringType
                            },
                            navArgument("dayOfMonth") {
                                type = NavType.IntType
                            },
                            navArgument("month") {
                                type = NavType.IntType
                            },
                            navArgument("year") {
                                type = NavType.IntType
                            },
                            navArgument("workoutIds") {
                                type = NavType.StringType
                            }
                            )
                    ) {
                        val workoutName = it.arguments?.getString("workoutName") ?: ""
                        val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                        val month = it.arguments?.getInt("month")!!
                        val year = it.arguments?.getInt("year")!!
                        StartWorkoutScreen(
                            workoutName = workoutName,
                            dayOfMonth = dayOfMonth,
                            month = month,
                            year = year,
                            onNavigateToTimer = { seconds, exerciseName, currentSet, totalSet ->
                                navController.navigate(
                                    Route.TIMER +
                                            "/$seconds" +
                                            "/$exerciseName" +
                                            "/$currentSet" +
                                            "/$totalSet"
                                )
                            }
                        )
                    }
                    composable(
                        route = Route.TIMER +
                                "/{seconds}" +
                                "/{exerciseName}" +
                                "/{currentSet}" +
                                "/{totalSet}",
                        arguments = listOf(
                            navArgument("seconds") {
                                type = NavType.IntType
                            },
                            navArgument("exerciseName") {
                                type = NavType.StringType
                            },
                            navArgument("currentSet") {
                                type = NavType.IntType
                            },
                            navArgument("totalSet") {
                                type = NavType.IntType
                            }
                        )
                    ){
                        val seconds = it.arguments?.getInt("seconds")!!
                        val exerciseName = it.arguments?.getString("exerciseName")!!
                        val currentSet = it.arguments?.getInt("currentSet")!!
                        val totalSet = it.arguments?.getInt("totalSet")!!
                        TimerScreen(
                            seconds = seconds,
                            exerciseName = exerciseName,
                            currentSet = currentSet,
                            totalSet = totalSet
                        )
                    }
//                    composable(Route.VERIFY_MOBILE_APP){
//                        VerifyMobileApp()
//                    }
                }
            }
        }
    }

//    override fun onPause() {
//        super.onPause()
////        Wearable.getCapabilityClient(this).removeListener(this, CAPABILITY_PHONE_APP)
//    }

//    override fun onResume() {
//        super.onResume()
////        Wearable.getCapabilityClient(this).addListener(this, CAPABILITY_PHONE_APP)
////        lifecycleScope.launch {
////            checkIfPhoneHasApp()
////        }
//    }

//    @ExperimentalCoilApi
//    @Composable
//    fun VerifyMobileApp(){
//        val spacing = LocalSpacing.current
//        val androidPhoneNodeWithApp = androidPhoneNodeWithApp
//
//        val textInfo: Pair<String, Boolean> = if (androidPhoneNodeWithApp != null) {
//            // TODO: Add your code to communicate with the phone app via
//            //       Wear APIs (MessageClient, DataClient, etc.)
//            Log.d(TAG, "Installed")
//            Pair(getString(R.string.message_installed, androidPhoneNodeWithApp.displayName), false)
//    //            binding.informationTextView.text =
//    //                getString(R.string.message_installed, androidPhoneNodeWithApp.displayName)
//    //            binding.remoteOpenButton.isInvisible = true
//        } else {
//            Log.d(TAG, "Missing")
//            Pair(getString(R.string.message_missing), true)
//    //            binding.informationTextView.text = getString(R.string.message_missing)
//    //            binding.remoteOpenButton.isVisible = true
//        }
//
//
//        Column(
//            Modifier
//                .fillMaxSize()
//                .padding(10.dp)
//                .background(color = MaterialTheme.colors.background),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            Text(
//                text = textInfo.first,
//                textAlign = TextAlign.Center,
//                color = MaterialTheme.colors.onBackground
//            )
//            if(textInfo.second){
//                Row(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(100f))
//                        .clickable { openAppInStoreOnPhone() }
//                        .border(
//                            width = 1.dp,
//                            color = MaterialTheme.colors.primary,
//                            shape = RoundedCornerShape(100f)
//                        )
//                        .padding(spacing.spaceMedium),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "add icon",
//                        tint = MaterialTheme.colors.primary
//                    )
//                    Spacer(modifier = Modifier.width(spacing.spaceMedium))
//                    Text(
//                        text = stringResource(R.string.install_app),
//                        style = MaterialTheme.typography.button,
//                        color = MaterialTheme.colors.primary
//                    )
//                }
//            }
//        }
//    }

//    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
//        Log.d(TAG, "onCapabilityChanged(): $capabilityInfo")
//        // There should only ever be one phone in a node set (much less w/ the correct
//        // capability), so I am just grabbing the first one (which should be the only one).
////        androidPhoneNodeWithApp = capabilityInfo.nodes.firstOrNull()
////        updateUi()
//    }

//    private suspend fun checkIfPhoneHasApp() {
//        Log.d(TAG, "checkIfPhoneHasApp()")
//
//        try {
//            val capabilityInfo = capabilityClient
//                .getCapability(CAPABILITY_PHONE_APP, CapabilityClient.FILTER_ALL)
//                .await()
//
//            Log.d(TAG, "Capability request succeeded.")
//
//            withContext(Dispatchers.Main) {
//                // There should only ever be one phone in a node set (much less w/ the correct
//                // capability), so I am just grabbing the first one (which should be the only one).
//                androidPhoneNodeWithApp = capabilityInfo.nodes.firstOrNull()
////                updateUi()
//            }
//        } catch (cancellationException: CancellationException) {
//            // Request was cancelled normally
//        } catch (throwable: Throwable) {
//            Log.d(TAG, "Capability request failed to return any results.")
//        }
//    }

//    private fun openAppInStoreOnPhone() {
//        Log.d(TAG, "openAppInStoreOnPhone()")
//
//        val intent = when (PhoneTypeHelper.getPhoneDeviceType(applicationContext)) {
//            PhoneTypeHelper.DEVICE_TYPE_ANDROID -> {
//                Log.d(TAG, "\tDEVICE_TYPE_ANDROID")
//                // Create Remote Intent to open Play Store listing of app on remote device.
//                Intent(Intent.ACTION_VIEW)
//                    .addCategory(Intent.CATEGORY_BROWSABLE)
//                    .setData(Uri.parse(ANDROID_MARKET_APP_URI))
//            }
//
//            PhoneTypeHelper.DEVICE_TYPE_IOS -> {
//                Log.d(TAG, "\tDEVICE_TYPE_IOS")
//
//                // Create Remote Intent to open App Store listing of app on iPhone.
//                Intent(Intent.ACTION_VIEW)
//                    .addCategory(Intent.CATEGORY_BROWSABLE)
//                    .setData(Uri.parse(APP_STORE_APP_URI))
//            }
//
//            else -> {
//                Log.d(TAG, "\tDEVICE_TYPE_ERROR_UNKNOWN")
//                return
//            }
//        }
//
//        lifecycleScope.launch {
//            try {
//                remoteActivityHelper.startRemoteActivity(intent).await()
//
//                ConfirmationOverlay().showOn(this@MainWearableActivity)
//            } catch (cancellationException: CancellationException) {
//                // Request was cancelled normally
//                throw cancellationException
//            } catch (throwable: Throwable) {
//                ConfirmationOverlay()
//                    .setType(ConfirmationOverlay.FAILURE_ANIMATION)
//                    .showOn(this@MainWearableActivity)
//            }
//        }
//    }

    companion object {
        private const val TAG = "MainWearActivity"

        // Name of capability listed in Phone app's wear.xml.
        // IMPORTANT NOTE: This should be named differently than your Wear app's capability.
        private const val CAPABILITY_PHONE_APP = "workout_buddy_phone_app"

        // Links to install mobile app for both Android (Play Store) and iOS.
        // TODO: Replace with your links/packages.
        private const val ANDROID_MARKET_APP_URI =
            "market://details?id=com.hbaez.workoutbuddy"

        // TODO: Replace with your links/packages.
        private const val APP_STORE_APP_URI =
            "https://itunes.apple.com/us/app/android-wear/id986496028?mt=8"
    }
}