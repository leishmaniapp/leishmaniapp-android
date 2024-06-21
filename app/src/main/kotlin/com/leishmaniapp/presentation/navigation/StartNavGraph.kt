package com.leishmaniapp.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.ui.views.start.GreetingsScreen

fun NavGraphBuilder.startNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = NavigationRoutes.StartRoute.route,
        startDestination = NavigationRoutes.StartRoute.GreetingsScreen.route,
    ) {
        composable(route = NavigationRoutes.StartRoute.GreetingsScreen.route) {
            GreetingsScreen(onContinue = { navController.navigateToAuthentication() })
        }
    }
}

//fun NavGraphBuilder.startNavGraph(
//    navController: NavHostController,
//    applicationViewModel: ApplicationViewModel,
//) {
//
//    navigation(
//        route = NavigationRoutes.StartRoute.route,
//        startDestination = NavigationRoutes.StartRoute.GreetingsScreen.route
//    ) {
//        composable(route = NavigationRoutes.StartRoute.GreetingsScreen.route) {
//            GreetingsScreen(onContinue = { navController.navigateToAuthentication() })
//        }
//
//        composable(route = NavigationRoutes.StartRoute.AuthenticationRoute.route) {
//            val coroutineScope = rememberCoroutineScope()
//            val context = LocalContext.current
//
//            var authenticationInProgress by remember {
//                mutableStateOf(false)
//            }
//
//            AuthenticationScreen(
//                authenticationInProgress,
//                onAuthenticate = { username, password ->
//                    authenticationInProgress = true
//                    coroutineScope.launch {
//                        withContext(Dispatchers.IO) {
//                            // Authenticate specialist
//                            val authState = applicationViewModel.authenticate(username, password)
//
//                            withContext(Dispatchers.Main) {
//                                authenticationInProgress = false
//                                // Navigate to next screen
//                                if (authState) {
//                                    navController.navigateToDiseasesMenu()
//                                } else {
//                                    // Show authentication failure toast
//                                    Toast.makeText(
//                                        context,
//                                        R.string.authentication_failure,
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        }
//                    }
//                })
//        }
//
//        composable(route = NavigationRoutes.StartRoute.DiseasesRoute.route) {
//            DiseasesMenuScreen(
//                diseases = applicationViewModel.specialist!!.diseases,
//                onDiseaseSelection = { disease ->
//                // Select the disease and navigate to the main menu
//                Log.d("DiseasesMenuScreen", "Selected disease: $disease")
//                applicationViewModel.disease = disease
//                navController.navigateToMenu()
//            })
//        }
//    }
//}

internal fun NavController.navigateToAuthentication() {
    this.navigate(NavigationRoutes.StartRoute.AuthenticationRoute.route)
}

internal fun NavController.navigateToDiseasesMenu() {
    this.navigate(NavigationRoutes.StartRoute.DiseasesRoute.route) {
        this.popUpTo(NavigationRoutes.StartRoute.GreetingsScreen.route) {
            inclusive = true
        }
    }
}