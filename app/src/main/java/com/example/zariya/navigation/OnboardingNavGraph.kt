package com.example.zariya.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.zariya.ui.onboarding.CompanyRegisterScreen
import com.example.zariya.ui.onboarding.LoginScreen
import com.example.zariya.ui.onboarding.RoleSelectScreen
import com.example.zariya.ui.onboarding.SignUpScreen
import com.example.zariya.ui.onboarding.WelcomeScreen
import com.example.zariya.viewmodel.AuthViewModel

fun NavGraphBuilder.onboardingNavGraph(
    navHostController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        route = NavRoutes.Onboarding.route,
        startDestination = Auth.WelcomeScreen.route
    ) {
        composable(route = Auth.WelcomeScreen.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navHostController.navigate(Auth.RoleSelect.route)
                }
            )
        }

        composable(route = Auth.RoleSelect.route) {
            RoleSelectScreen(
                onRoleSelected = { role ->
                    navHostController.navigate(Auth.Register.createRoute(role))
                }
            )
        }

        composable(
            route = Auth.Register.route,
            arguments = listOf(
                navArgument("role") {
                    type = NavType.StringType
                    defaultValue = "Candidate"
                }
            )) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""
            SignUpScreen(
                role = role,
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    when (role) {
                        "recruiter" -> {
                            navHostController.navigate(NavRoutes.Recruiter.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        }

                        else -> {
                            navHostController.navigate(NavRoutes.Helper.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                },
                onLoginClick = {
                    navHostController.navigate(Auth.Login.createRoute(role))
                }
            )
        }

        composable(
            route = Auth.Login.route,
            arguments = listOf(
                navArgument("role") {
                    type = NavType.StringType
                    defaultValue = "Candidate"
                }
            )
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""
            LoginScreen(
                role = role,
                authViewModel = authViewModel,
                onLoginSuccess = {
                    when (role) {
                        "recruiter" -> {
                            navHostController.navigate(NavRoutes.Recruiter.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        }

                        else -> {
                            navHostController.navigate(NavRoutes.Helper.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(route = Auth.CompanyRegister.route) {
            CompanyRegisterScreen(
                authViewModel = authViewModel
            ){
                navHostController.navigate(NavRoutes.Recruiter.route){
                    popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                }
            }
        }
    }
}

sealed class Auth(val route: String) {
    data object WelcomeScreen : Auth("welcome_screen")
    data object Login : Auth("login?role={role}") {
        fun createRoute(role: String) = "login?role=$role"
    }

    data object Register : Auth("register?role={role}") {
        fun createRoute(role: String) = "register?role=$role"
    }

    data object RoleSelect : Auth("role_select")
    data object CompanyRegister : Auth("company_register")
}