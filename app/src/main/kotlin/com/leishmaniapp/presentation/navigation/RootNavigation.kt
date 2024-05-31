package com.leishmaniapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.viewmodel.PatientsViewModel

@Composable
fun RootNavigation(
    navigationController: NavHostController = rememberNavController(),
    applicationViewModel: ApplicationViewModel = hiltViewModel(),
    patientsViewModel: PatientsViewModel = hiltViewModel(),
    diagnosisViewModel: DiagnosisViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navigationController,
        startDestination = NavigationRoutes.StartRoute.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
            )
        })
    {

        this.startNavGraph(navigationController, applicationViewModel)
        this.menuNavGraph(navigationController, applicationViewModel)
        this.patientsNavGraph(
            navigationController,
            applicationViewModel,
            patientsViewModel,
            diagnosisViewModel
        )
        this.diagnosisNavGraph(navigationController, applicationViewModel, diagnosisViewModel)
    }
}