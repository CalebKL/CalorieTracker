package com.example.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calorietracker.navigation.navigate
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.example.core.navigation.Route
import com.example.onboarding_presentation.gender.GenderScreen
import com.example.onboarding_presentation.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination =Route.WELCOME
                ){
                    composable(Route.WELCOME){
                        WelcomeScreen(onNavigate = navController::navigate)
                    }
                    composable(Route.AGE){

                    }
                    composable(Route.GENDER){
                        GenderScreen(onNavigate = navController::navigate)
                    }
                    composable(Route.HEIGHT){

                    }
                    composable(Route.WEIGHT){

                    }
                    composable(Route.NUTRIENT_GOAL){

                    }
                    composable(Route.ACTIVITY){

                    }
                    composable(Route.GOAL){

                    }
                    composable(Route.TRACKER_OVERVIEW){

                    }
                    composable(Route.SEARCH){

                    }
                }
            }
        }
    }
}
