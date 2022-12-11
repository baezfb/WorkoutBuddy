package com.hbaez.workoutbuddy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.create_workout.CreateWorkoutScreen
import com.example.workout_logger_presentation.search_exercise.SearchExerciseScreen
import com.example.workout_logger_presentation.start_workout.StartWorkoutScreen
import com.example.workout_logger_presentation.workout_logger_overview.WorkoutLoggerOverviewScreen
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnBoarding = preferences.loadShouldShowOnboarding()
        val isLoggedIn = !preferences.loadLoginInfo().username.isNullOrEmpty()
        setContent {
            WorkoutBuddyTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        if( currentRoute == Route.WORKOUT_OVERVIEW || currentRoute == Route.TRACKER_OVERVIEW
                            || currentRoute == Route.APP_SETTINGS || currentRoute == Route.ANALYZER_OVERVIEW
                            || currentRoute == Route.WEAR_OVERVIEW){
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Workout",
                                        route = Route.WORKOUT_OVERVIEW,
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
                                        icon = Icons.Default.ShoppingCart
                                    ),
                                    BottomNavItem(
                                        name = "Wear",
                                        route = Route.WEAR_OVERVIEW,
                                        icon = Icons.Default.Person
                                    ),
                                    BottomNavItem(
                                        name = "Settings",
                                        route = Route.APP_SETTINGS,
                                        icon = Icons.Default.Settings,
//                                        badgeCount = 214
                                    ),
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    }
                ){
                    NavHost(
                        navController = navController,
                        startDestination = if(shouldShowOnBoarding && isLoggedIn){
                            Route.WELCOME
                        } else if(!shouldShowOnBoarding && isLoggedIn){
                            Route.WORKOUT_OVERVIEW
                        } else Route.USER_AUTH_LOGIN
                    ){
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNextClick = {
                                navController.navigate(Route.GENDER)
                            })
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                }
                            )
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                }
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                }
                            )
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.TRACKER_OVERVIEW)
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
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(Route.WORKOUT_OVERVIEW) {
                            Column(modifier = Modifier.padding(bottom = 58.dp)){
                                WorkoutLoggerOverviewScreen(
                                    onNavigateToCreate = {
                                        navController.navigate(
                                            Route.WORKOUT_CREATE
                                        )
                                    },
                                    onNavigateToWorkout = { workoutName, workoutId, day, month, year ->
                                        navController.navigate(
                                            Route.WORKOUT_START +
                                                    "/$workoutName" +
                                                    "/$workoutId" +
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
                                    "/{workoutId}" +
                                    "/{dayOfMonth}" +
                                    "/{month}" +
                                    "/{year}",
                            arguments = listOf(
                                navArgument("workoutName") {
                                    type = NavType.StringType
                                },
                                navArgument("workoutId") {
                                    type = NavType.IntType
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
                            val workoutName = it.arguments?.getString("workoutName") ?: ""
                            val workoutId = it.arguments?.getInt("workoutId") ?: -1
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            StartWorkoutScreen(
                                workoutName = workoutName,
                                workoutId = workoutId,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }


                        composable(Route.WORKOUT_CREATE) {
                            CreateWorkoutScreen(
                                scaffoldState = scaffoldState,
                                onNavigateToSearchExercise = { rowId ->
                                    navController.navigate(Route.WORKOUT_SEARCH + "/$rowId")
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
                                scaffoldState = scaffoldState,
                                rowId = rowId,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(Route.APP_SETTINGS) {
                            AppSettingsOverviewScreen(
                                /*TODO*/
                            )
                        }
                        composable(Route.ANALYZER_OVERVIEW) {
                            AnalyzerOverviewScreen(
                                /*TODO*/
                            )
                        }
                        composable(Route.WEAR_OVERVIEW) {
                            WearOverviewScreen(
                                /*TODO*/
                            )
                        }
                        composable(Route.USER_AUTH_LOGIN) {
                            UserAuthLoginScreen(
                                scaffoldState = scaffoldState,
                                onNavigateToSignUp = {
                                    navController.navigate(Route.USER_AUTH_SIGNUP)
                                }
                            )
                        }
                        composable(Route.USER_AUTH_SIGNUP) {
                            UserAuthSignupScreen(
                                scaffoldState = scaffoldState,
                                /*TODO*/
                            )
                        }
                    }
                }
            }
        }
    }
}