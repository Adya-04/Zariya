package com.example.zariya.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.zariya.ui.onboarding.SplashScreen
import com.example.zariya.viewmodel.AuthViewModel
import com.example.zariya.viewmodel.HelperViewModel
import com.example.zariya.viewmodel.RecruiterViewModel

@Composable
fun RootNavGraph(navController: NavHostController, activity: Activity) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val recruiterViewModel: RecruiterViewModel = hiltViewModel()
    val helperViewModel: HelperViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SplashScreen.route
    ) {
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(
                authViewModel = authViewModel,
                navigateToHome = { role ->
                    when (role) {
                        "recruiter" -> {
                            navController.navigate(NavRoutes.Recruiter.route) {
                                popUpTo(NavRoutes.SplashScreen.route) { inclusive = true }
                            }
                        }

                        else -> {
                            navController.navigate(NavRoutes.Helper.route) {
                                popUpTo(NavRoutes.SplashScreen.route) { inclusive = true }
                            }
                        }
                    }
                },
                navigateToWelcome = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        onboardingNavGraph(navController, authViewModel)
        recruiterNavGraph(navController, recruiterViewModel)
        helperNavGraph(navController, helperViewModel)
    }

}

sealed class NavRoutes(val route: String) {
    data object Onboarding : NavRoutes("onboarding_graph")
    data object Recruiter : NavRoutes("recruiter_graph")
    data object Helper : NavRoutes("helper_graph")
    data object SplashScreen : NavRoutes("splash_screen")
}