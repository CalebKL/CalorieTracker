package com.example.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.example.core.domain.preferences.Preferences
import com.example.calorietracker.navigation.Route
import com.example.onboarding_presentation.activity.ActivityScreen
import com.example.onboarding_presentation.age.AgeScreen
import com.example.onboarding_presentation.gender.GenderScreen
import com.example.onboarding_presentation.goal.GoalScreen
import com.example.onboarding_presentation.height.HeightScreen
import com.example.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.example.onboarding_presentation.weight.WeightScreen
import com.example.onboarding_presentation.welcome.WelcomeScreen
import com.example.tracker_presentation.search.SearchScreen
import com.example.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnBoarding = preferences.loadShouldShowOnboarding()
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ){
                    val unUsedPadding = it
                    NavHost(
                        navController = navController,
                        startDestination =if (shouldShowOnBoarding) {
                            Route.WELCOME
                        }else Route.TRACKER_OVERVIEW
                    ){
                        composable(Route.WELCOME){
                            WelcomeScreen(onNextClick = {
                                navController.navigate(Route.GENDER)
                            }
                            )
                        }
                        composable(Route.AGE){
                            AgeScreen(
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.GENDER){
                            GenderScreen(onNextClick ={
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.HEIGHT){
                            HeightScreen(
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.WEIGHT){
                            WeightScreen(
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.NUTRIENT_GOAL){
                            NutrientGoalScreen(
                                onNextClick = {
                                    navController.navigate(Route.TRACKER_OVERVIEW)
                                },
                                scaffoldState = scaffoldState
                            )
                        }
                        composable(Route.ACTIVITY){
                            ActivityScreen(onNextClick = {
                                navController.navigate(Route.GOAL)
                            })
                        }
                        composable(Route.GOAL){
                            GoalScreen(onNextClick = {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            })
                        }
                        composable(Route.TRACKER_OVERVIEW){
                            TrackerOverviewScreen(onNavigateToSearch = {mealName, day, month, year ->
                                navController.navigate(
                                    Route.SEARCH + "/$mealName" + "/$day" + "/$month" + "/$year"
                                )
                            })
                        }
                        composable(
                           route=  Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName"){
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth"){
                                    type = NavType.IntType
                                },
                                navArgument("month"){
                                    type = NavType.IntType
                                },
                                navArgument("year"){
                                    type = NavType.IntType
                                },
                            )
                        ){navBackStackEntry->
                            val mealName = navBackStackEntry.arguments?.getString("mealName")!!
                            val dayOfMonth = navBackStackEntry.arguments?.getInt("dayOfMonth")!!
                            val month = navBackStackEntry.arguments?.getInt("month")!!
                            val year = navBackStackEntry.arguments?.getInt("year")!!

                            SearchScreen(
                                scaffoldState = scaffoldState,
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                mealName =mealName ,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year
                            )
                        }
                    }
                }
            }
        }
    }
}
