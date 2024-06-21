package com.leishmaniapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * Root of the navigation graph for the application
 */
@Composable
fun RootNavigation(
    navigationController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navigationController,
        startDestination = NavigationRoutes.StartRoute.route,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start) }
    )
    {
        startNavGraph(navController = navigationController)
    }
}