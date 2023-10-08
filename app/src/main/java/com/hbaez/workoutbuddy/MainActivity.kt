package com.hbaez.workoutbuddy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.remote.interactions.RemoteActivityHelper
import coil.annotation.ExperimentalCoilApi
import com.example.chatbot_presentation.chat_overview.ChatScreen
import com.example.workout_logger_presentation.create_exercise.CreateExerciseScreen
import com.example.workout_logger_presentation.create_workout.CreateWorkoutScreen
import com.example.workout_logger_presentation.search_exercise.SearchExerciseScreen
import com.example.workout_logger_presentation.start_exercise.StartExerciseScreen
import com.example.workout_logger_presentation.start_workout.StartWorkoutScreen
import com.example.workout_logger_presentation.workout_logger_overview.WorkoutLoggerOverviewScreen
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityClient.OnCapabilityChangedListener
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.hbaez.analyzer_presentation.analyzer_presentation.AnalyzerOverviewScreen
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.onboarding_presentation.activity.ActivityScreen
import com.hbaez.onboarding_presentation.age.AgeScreen
import com.hbaez.onboarding_presentation.gender.GenderScreen
import com.hbaez.onboarding_presentation.goal.GoalScreen
import com.hbaez.onboarding_presentation.height.HeightScreen
import com.hbaez.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.hbaez.onboarding_presentation.weight.WeightScreen
import com.hbaez.onboarding_presentation.welcome.WelcomeScreen
import com.hbaez.settings_presentation.settings_overview.AppSettingsOverviewScreen
import com.hbaez.tracker_presentation.search.SearchScreen
import com.hbaez.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.hbaez.user_auth_presentation.user_auth_overview.UserAuthLoginScreen
import com.hbaez.user_auth_presentation.user_auth_overview.UserAuthSignupScreen
import com.hbaez.wear_presentation.wear_overview.WearOverviewScreen
import com.hbaez.workoutbuddy.navigation.Route
import com.hbaez.workoutbuddy.ui.theme.BottomNavigationBar
import com.hbaez.workoutbuddy.ui.theme.WorkoutBuddyTheme
import com.hbaez.user_auth_presentation.user_auth.SplashScreen
import com.hbaez.user_auth_presentation.user_auth_overview.UserAuthWelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.hbaez.core.R
import java.time.LocalDate

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(), OnCapabilityChangedListener {

    @Inject
    lateinit var preferences: Preferences
    private lateinit var navController: NavHostController

    private lateinit var capabilityClient: CapabilityClient
    private lateinit var nodeClient: NodeClient
    private lateinit var remoteActivityHelper: RemoteActivityHelper

    private var wearNodesWithApp: Set<Node>? = null
    private var allConnectedNodes: List<Node>? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnBoarding = preferences.loadShouldShowOnboarding()

        capabilityClient = Wearable.getCapabilityClient(this)
        nodeClient = Wearable.getNodeClient(this)
        remoteActivityHelper = RemoteActivityHelper(this)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                launch{
                    findWearDevicesWithApp()
                }
                launch{
                    findAllWearDevices()
                }
            }
        }

        setContent {
            WorkoutBuddyTheme {
                navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        if( currentRoute?.startsWith(Route.WORKOUT_OVERVIEW) == true || currentRoute == Route.TRACKER_OVERVIEW
                            || currentRoute == Route.APP_SETTINGS || currentRoute == Route.ANALYZER_OVERVIEW || currentRoute == Route.CHAT_BOT){
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Workout",
                                        route = Route.WORKOUT_OVERVIEW + "/${LocalDate.now()}",
                                        icon = Icons.Default.Notifications
                                    ),
                                    BottomNavItem(
                                        name = "Tracker",
                                        route = Route.TRACKER_OVERVIEW,
                                        icon = Icons.Default.Home,
//                                        badgeCount = 23
                                    ),
                                    BottomNavItem(
                                        name = "Analyze",
                                        route = Route.ANALYZER_OVERVIEW,
                                        icon = Icons.Default.Person
                                    ),
//                                    BottomNavItem(
//                                        name = "Chat",
//                                        route = Route.CHAT_BOT,
//                                        icon = Icons.Default.Face
//                                    ),
                                    BottomNavItem(
                                        name = "Settings",
                                        route = Route.APP_SETTINGS,
                                        icon = Icons.Default.Settings,
//                                        badgeCount = 214
                                    ),
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route) {
                                        launchSingleTop = true
                                        popUpTo(it.route) { inclusive = false }
                                    }
                                }
                            )
                        }
                    }
                ){
                    NavHost(
                        navController = navController,
                        startDestination = Route.SPLASH
                    ){
                        composable(Route.SPLASH){
                            SplashScreen(
                                openAndPopUp = { route, popup ->
                                    navController.navigate(Route.WORKOUT_OVERVIEW + "/${LocalDate.now()}") {
                                        launchSingleTop = true
                                        popUpTo(popup) { inclusive = true }
                                    }
                                },
                            )
                        }
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNextClick = {
                                navController.navigate(Route.GENDER)
                            })
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                snackBarHost = snackbarHostState,
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                }
                            )
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                snackBarHost = snackbarHostState,
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                }
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                snackBarHost = snackbarHostState,
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                }
                            )
                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(onNextClick = { navController.navigate(Route.GOAL) })
                        }
                        composable(Route.GOAL) {
                            GoalScreen(onNextClick = {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            })
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                snackBarHost = snackbarHostState,
                                onNextClick = {
                                    navController.navigate(Route.TRACKER_OVERVIEW)
                                }
                            )
                        }
                        composable(Route.TRACKER_OVERVIEW) {
                            Column(modifier = Modifier.padding(bottom = 58.dp)){
                                TrackerOverviewScreen(onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        Route.SEARCH + "/$mealName" +
                                                "/$day" +
                                                "/$month" +
                                                "/$year"
                                    )
                                })
                            }
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
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

                                )
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                snackBarHost = snackbarHostState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(
                            route = Route.WORKOUT_OVERVIEW +
                                    "/{date}",
                            arguments = listOf(
                                navArgument("date") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            Column(modifier = Modifier.padding(bottom = 58.dp)){
                                WorkoutLoggerOverviewScreen(
                                    onNavigateToCreateWorkout = {
                                        val createWorkout = true
                                        val workoutName = null
                                        val workoutIds = null
                                        navController.navigate(
                                            Route.WORKOUT_CREATE +
                                                    "/$createWorkout" +
                                                    "/$workoutName" +
                                                    "/$workoutIds"
                                        )
                                    },
                                    onNavigateToEditWorkout = { workoutName, workoutIds ->
                                        val createWorkout = false
                                        navController.navigate(
                                            Route.WORKOUT_CREATE +
                                                    "/$createWorkout" +
                                                    "/$workoutName" +
                                                    "/$workoutIds"
                                        )
                                    },
                                    onNavigateToStartWorkout = { workoutName, day, month, year, workoutIds ->
                                        navController.navigate(
                                            Route.WORKOUT_START +
                                                    "/$workoutName" +
                                                    "/$day" +
                                                    "/$month" +
                                                    "/$year" +
                                                    "/$workoutIds"
                                        )
                                    },
                                    onNavigateToCreateExercise = {
                                        val createExercise = true
                                        val exerciseName = null
                                        val description = null
                                        val primaryMuscles = null
                                        val secondaryMuscles = null
                                        val imageURL = null
                                        navController.navigate(
                                            Route.EXERCISE_CREATE +
                                                    "/$createExercise" +
                                                    "/$exerciseName" +
                                                    "/$description" +
                                                    "/$primaryMuscles" +
                                                    "/$secondaryMuscles" +
                                                    "/$imageURL"
                                        )
                                    },
                                    onNavigateToEditExercise = { exerciseName, description, primaryMuscles, secondaryMuscles, imageURL ->
                                        val createExercise = false
                                        navController.navigate(
                                            Route.EXERCISE_CREATE +
                                                    "/$createExercise" +
                                                    "/$exerciseName" +
                                                    "/$description" +
                                                    "/${if(!primaryMuscles.isNullOrEmpty()) primaryMuscles else null}" +
                                                    "/${if(!secondaryMuscles.isNullOrEmpty()) secondaryMuscles else null}" +
                                                    "/${if(imageURL.isNotEmpty()) Uri.encode(imageURL.joinToString(",")) else null}"
                                        )
                                    },
                                    onNavigateToStartExercise = { exerciseName, day, month, year ->
                                        navController.navigate(
                                            Route.EXERCISE_START +
                                                    "/$exerciseName" +
                                                    "/$day" +
                                                    "/$month" +
                                                    "/$year"
                                        )
                                    }
                                )
                            }
                        }
                        composable(
                            route = Route.WORKOUT_START +
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
//                            val workoutIds = it.arguments?.getString("workoutIds")!!
                            StartWorkoutScreen(
                                workoutName = workoutName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }


                        composable(
                            route = Route.WORKOUT_CREATE +
                                    "/{createWorkout}" +
                                    "/{workoutName}" +
                                    "/{workoutIds}",
                            arguments = listOf(
                                navArgument("createWorkout") {
                                    type = NavType.BoolType
                                },
                                navArgument("workoutName") {
                                    type = NavType.StringType
                                },
                                navArgument("workoutIds") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val createWorkout = it.arguments?.getBoolean("createWorkout")!!
                            CreateWorkoutScreen(
                                snackBarHost = snackbarHostState,
                                createWorkout = createWorkout,
                                onNavigateToSearchExercise = { page ->
                                    navController.navigate(Route.WORKOUT_SEARCH + "/$page")
                                },
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable(
                            route = Route.WORKOUT_SEARCH + "/{rowId}",
                            arguments = listOf(
                                navArgument("rowId"){
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val rowId = it.arguments?.getInt("rowId") ?: 0
                            SearchExerciseScreen(
                                snackBarHost = snackbarHostState,
                                rowId = rowId,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable(
                            route = Route.EXERCISE_CREATE +
                                    "/{createExercise}" +
                                    "/{exerciseName}" +
                                    "/{description}" +
                                    "/{primaryMuscles}" +
                                    "/{secondaryMuscles}" +
                                    "/{imageURL}",
                            arguments = listOf(
                                navArgument("createExercise") {
                                    type = NavType.BoolType
                                },
                                navArgument("exerciseName") {
                                    type = NavType.StringType
                                },
                                navArgument("description") {
                                    type = NavType.StringType
                                },
                                navArgument("primaryMuscles") {
                                    type = NavType.StringType
                                },
                                navArgument("secondaryMuscles") {
                                    type = NavType.StringType
                                },
                                navArgument("imageURL") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val createExercise = it.arguments?.getBoolean("createExercise")!!
                            CreateExerciseScreen(
                                snackBarHost = snackbarHostState,
                                createExercise = createExercise,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable(
                            route = Route.EXERCISE_START +
                                    "/{exerciseName}" +
                                    "/{dayOfMonth}" +
                                    "/{month}" +
                                    "/{year}",
                            arguments = listOf(
                                navArgument("exerciseName") {
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
                                }
                            )
                        ) {
                            val exerciseName = it.arguments?.getString("exerciseName") ?: ""
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            StartExerciseScreen(
                                exerciseName = exerciseName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable(Route.APP_SETTINGS) {
                            AppSettingsOverviewScreen(
                                snackBarHost = snackbarHostState,
                                onNavigateToSignUp = {
                                    navController.navigate(Route.USER_AUTH_SIGNUP)
                                },
                                onNavigateToLogin = {
                                    navController.navigate(Route.USER_AUTH_LOGIN)
                                },
                                onNavigateToWelcome = {
                                    navController.navigate(Route.WELCOME)
                                },
                                onNavigateToUserAuthWelcome= {
                                    navController.navigate(Route.USER_AUTH_WELCOME)
                                },
                                onNavigateToWear = {
                                    navController.navigate(Route.WEAR_OVERVIEW)
                                },
                                deleteMyAccount = {
                                    navController.navigate(Route.SPLASH) {
                                        launchSingleTop = true
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Route.ANALYZER_OVERVIEW) {
                            AnalyzerOverviewScreen(
                                onNavigateToWorkoutOverview = { date ->
                                    navController.navigate(Route.WORKOUT_OVERVIEW + "/${date}")
                                }
                            )
                        }
                        composable(Route.WEAR_OVERVIEW) {
                            WearOverviewScreen(
                                wearNodesWithApp = wearNodesWithApp,
                                allConnectedNodes = allConnectedNodes,
                                openPlayStore = {
                                    openPlayStoreOnWearDevicesWithoutApp()
                                }
                            )
                        }
                        composable(Route.USER_AUTH_LOGIN) {
                            UserAuthLoginScreen(
                                snackBarHost = snackbarHostState,
                                onNavigateToSignUp = {
                                    navController.navigate(Route.USER_AUTH_SIGNUP)
                                },
                                onNavigateToHome = {
                                    navController.navigate(Route.WORKOUT_OVERVIEW + LocalDate.now().toString())
                                },
                                openAndPopUp = { route, popup ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                        popUpTo(popup) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Route.USER_AUTH_SIGNUP) {
                            UserAuthSignupScreen(
                                snackBarHost = snackbarHostState,
                                openAndPopUp = { route, popup ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                        popUpTo(popup) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Route.USER_AUTH_WELCOME) {
                            UserAuthWelcomeScreen(
                                onNavigateToSignUp = {
                                                     navController.navigate(Route.USER_AUTH_SIGNUP)
                                                     },
                                onNavigateToLogin = {
                                                    navController.navigate(Route.USER_AUTH_LOGIN)
                                                    },
                                onNavigateToHome = {
                                                   navController.navigate(Route.WELCOME)
                                                   },
                                openAndPopUp = { route, popup ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                        popUpTo(popup) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Route.CHAT_BOT) {
                            ChatScreen()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
        capabilityClient.removeListener(this, CAPABILITY_WEAR_APP)
    }

    override fun onResume() {
        Log.d(TAG, "onResume()")
        super.onResume()
        capabilityClient.addListener(this, CAPABILITY_WEAR_APP)
    }

    private suspend fun findWearDevicesWithApp() {
        Log.d(TAG, "findWearDevicesWithApp()")

        try {
            val capabilityInfo = capabilityClient
                .getCapability(CAPABILITY_WEAR_APP, CapabilityClient.FILTER_ALL)
                .await()

            withContext(Dispatchers.Main) {
                Log.d(TAG, "Capability request succeeded.")
                wearNodesWithApp = capabilityInfo.nodes
                Log.d(TAG, "Capable Nodes: $wearNodesWithApp")
//                updateUI()
            }
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
            throw cancellationException
        } catch (throwable: Throwable) {
            Log.d(TAG, "Capability request failed to return any results.")
        }
    }

    private suspend fun findAllWearDevices() {
        Log.d(TAG, "findAllWearDevices()")

        try {
            val connectedNodes = nodeClient.connectedNodes.await()

            withContext(Dispatchers.Main) {
                allConnectedNodes = connectedNodes
//                updateUI()
            }
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
        } catch (throwable: Throwable) {
            Log.d(TAG, "Node request failed to return any results.")
        }
    }

    private fun openPlayStoreOnWearDevicesWithoutApp() {
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
            lifecycleScope.launch {
                try {
                    remoteActivityHelper
                        .startRemoteActivity(
                            targetIntent = intent,
                            targetNodeId = node.id
                        )
                        .await()

                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.store_request_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (cancellationException: CancellationException) {
                    // Request was cancelled normally
                } catch (throwable: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.store_request_unsuccessful),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        Log.d(TAG, "onCapabilityChanged(): $capabilityInfo")
        wearNodesWithApp = capabilityInfo.nodes

        lifecycleScope.launch {
            // Because we have an updated list of devices with/without our app, we need to also update
            // our list of active Wear devices.
            findAllWearDevices()
        }
    }

    companion object {
        private const val TAG = "MainActivity"

        // Name of capability listed in Wear app's wear.xml.
        // IMPORTANT NOTE: This should be named differently than your Phone app's capability.
        private const val CAPABILITY_WEAR_APP = "workout_buddy_wear_app"

        // Links to Wear app (Play Store).
        // TODO: Replace with your links/packages.
        private const val PLAY_STORE_APP_URI =
            "market://details?id=com.hbaez.wearable"
    }
}