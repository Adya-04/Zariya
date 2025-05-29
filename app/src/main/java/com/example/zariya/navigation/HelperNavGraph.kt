package com.example.zariya.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.zariya.ui.helper.CandidateDetailsScreenforHelper
import com.example.zariya.ui.helper.HelperDashboardScreen
import com.example.zariya.viewmodel.HelperViewModel

fun NavGraphBuilder.helperNavGraph(
    navHostController: NavHostController,
    helperViewModel: HelperViewModel
) {
    navigation(
        route = NavRoutes.Helper.route,
        startDestination = Helper.HelperDashboard.route
    ) {
        composable(route = Helper.HelperDashboard.route) {
            HelperDashboardScreen(
                navController = navHostController,
                helperViewModel = helperViewModel
            )
        }
        composable(
            route = Helper.CandidateDetails.routeWithArg,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            CandidateDetailsScreenforHelper(
                navController = navHostController,
                jobId = jobId,
                helperViewModel = helperViewModel
            )
        }
    }
}

sealed class Helper(val route: String) {
    data object HelperDashboard : Helper("helper_dashboard")
    data object CandidateDetails : Helper("candidateDetails/{jobId}") {
        const val routeWithArg = "candidateDetails/{jobId}"
        fun createRoute(jobId: String) = "candidateDetails/$jobId"
    }
}
