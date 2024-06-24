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
import com.leishmaniapp.presentation.navigation.graphs.menuNavGraph
import com.leishmaniapp.presentation.navigation.graphs.navigateToAuthentication
import com.leishmaniapp.presentation.navigation.graphs.startNavGraph
import com.leishmaniapp.presentation.state.AuthState
import com.leishmaniapp.presentation.viewmodel.AuthViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel

/**
 * Root of the navigation graph for the application
 */
@Composable
fun RootNavigation(
    navigationController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    diagnosisViewModel: DiagnosisViewModel = viewModel(),
) {

    // Navigate to authentication if unauthenticated
    val authState by authViewModel.authState.observeAsState(initial = AuthState.Busy)
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
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start) }
    )
    {
        startNavGraph(
            navHostController = navigationController,
            authViewModel = authViewModel,
        )

        menuNavGraph(
            navHostController = navigationController,
            authViewModel = authViewModel,
            diagnosisViewModel = diagnosisViewModel
        )
    }
}