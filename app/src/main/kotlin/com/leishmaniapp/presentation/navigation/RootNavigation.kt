package com.leishmaniapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leishmaniapp.presentation.navigation.graphs.diagnosisNavGraph
import com.leishmaniapp.presentation.navigation.graphs.menuNavGraph
import com.leishmaniapp.presentation.navigation.graphs.navigateToAuthentication
import com.leishmaniapp.presentation.navigation.graphs.patientsNavGraph
import com.leishmaniapp.presentation.navigation.graphs.startNavGraph
import com.leishmaniapp.presentation.viewmodel.CameraViewModel
import com.leishmaniapp.presentation.viewmodel.state.AuthState
import com.leishmaniapp.presentation.viewmodel.SessionViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel
import com.leishmaniapp.presentation.viewmodel.PatientViewModel

/**
 * Root of the navigation graph for the application
 */
@Composable
fun RootNavigation(
    navigationController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = viewModel(),
    diagnosisViewModel: DiagnosisViewModel = viewModel(),
    patientViewModel: PatientViewModel = viewModel(),
    cameraViewModel: CameraViewModel = viewModel(),
) {

    // Navigate to authentication if unauthenticated
    val authState by sessionViewModel.state.observeAsState(initial = AuthState.Busy)
    LaunchedEffect(key1 = authState, key2 = navigationController.currentDestination) {
        if ((authState is AuthState.None) &&
            (navigationController.currentDestination!!.route != NavigationRoutes.StartRoute.AuthenticationRoute.route) &&
            (navigationController.currentDestination!!.route != NavigationRoutes.StartRoute.GreetingsScreen.route)
        ) {
            navigationController.navigateToAuthentication()
        }
    }

    NavHost(
        navController = navigationController,
        startDestination = NavigationRoutes.StartRoute.route,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End) }
    )
    {
        startNavGraph(
            navHostController = navigationController,
            sessionViewModel = sessionViewModel,
        )

        menuNavGraph(
            navHostController = navigationController,
            sessionViewModel = sessionViewModel,
            diagnosisViewModel = diagnosisViewModel,
        )

        patientsNavGraph(
            navHostController = navigationController,
            patientViewModel = patientViewModel,
        )

        diagnosisNavGraph(
            navHostController = navigationController,
            diagnosisViewModel = diagnosisViewModel,
            cameraViewModel = cameraViewModel,
            sessionViewModel = sessionViewModel,
            patientViewModel = patientViewModel,
        )
    }
}