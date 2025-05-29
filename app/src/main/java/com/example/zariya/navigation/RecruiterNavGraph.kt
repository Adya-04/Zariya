package com.example.zariya.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.zariya.ui.recruiter.CandidatesManagementScreen
import com.example.zariya.ui.recruiter.JobDetailsScreen
import com.example.zariya.ui.recruiter.RecruiterDashboardScreen
import com.example.zariya.viewmodel.RecruiterViewModel

fun NavGraphBuilder.recruiterNavGraph(
    navHostController: NavHostController,
    recruiterViewModel: RecruiterViewModel
) {
    navigation(
        route = NavRoutes.Recruiter.route,
        startDestination = Recruiter.RecruiterDashboard.route
    ) {
        composable(route = Recruiter.RecruiterDashboard.route) {
            RecruiterDashboardScreen(
                navController = navHostController,
                viewModel = recruiterViewModel
            )
        }

        composable(route = Recruiter.JobDetail.route) {
            JobDetailsScreen(
                navController = navHostController,
                viewModel = recruiterViewModel
            )
        }

        composable(
            route = Recruiter.CandidateManagement.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType }))
            { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            CandidatesManagementScreen(jobId = jobId, navController = navHostController, viewModel = recruiterViewModel)
        }
    }
}

sealed class Recruiter(val route: String){
    data object RecruiterDashboard: Recruiter("recruiter_dashboard")
    data object JobDetail: Recruiter("jobDetail")
    data object CandidateManagement: Recruiter("candidatesManagement/{jobId}") {
        fun createRoute(jobId: String) = "candidatesManagement/$jobId"
    }
}