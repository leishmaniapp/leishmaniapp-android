package com.leishmaniapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel

@Composable
fun RootNavigation(
    navigationController: NavHostController = rememberNavController(),
    applicationViewModel: ApplicationViewModel = viewModel()
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
      //  this.diagnosisNavGraph(navController = navigationController, applicationViewModel = applicationViewModel )
       ///*TODO*/ this.diagnosisNavGraph(navController = navigationController, applicationViewModel = applicationViewModel)
    }
}