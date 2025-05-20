package com.example.zariya.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zariya.ui.SignupScreen
import com.example.zariya.ui.WelcomeScreen
import com.example.zariya.ui.recruiter.DashboardScreen

@Composable
fun AppNavigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("welcome") {
            WelcomeScreen(onGetStartedClick = {
                navController.navigate("home")
            })
        }
        composable("home") {
            SignupScreen()
        }
        composable("dashboard") {
            DashboardScreen()
        }
    }
}