package com.leishmaniapp.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.viewmodel.state.AuthState
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.DisconnectedAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.ErrorAlertDialog
import com.leishmaniapp.presentation.ui.views.start.AuthenticationScreen
import com.leishmaniapp.presentation.ui.views.start.GreetingsScreen
import com.leishmaniapp.presentation.viewmodel.SessionViewModel

fun NavGraphBuilder.startNavGraph(
    navHostController: NavHostController,
    sessionViewModel: SessionViewModel,
) {
    navigation(
        route = NavigationRoutes.StartRoute.route,
        startDestination = NavigationRoutes.StartRoute.GreetingsScreen.route,
    ) {

        composable(route = NavigationRoutes.StartRoute.GreetingsScreen.route) {
            GreetingsScreen(onContinue = {
                navHostController.navigateToAuthentication()
            })
        }

        composable(route = NavigationRoutes.StartRoute.AuthenticationRoute.route) {

            // Grab the authentication state
            val authState by sessionViewModel.authState.observeAsState(initial = AuthState.Busy)

            // If authenticated, then exit this screen
            if (authState is AuthState.Authenticated) {
                navHostController.navigateToDiseasesMenu()
            }

            // Show the authentication screen
            AuthenticationScreen(onAuthenticate = { email, password ->
                sessionViewModel.authenticate(email, password)
            })

            when (authState) {
                is AuthState.Busy -> BusyAlertDialog()
                AuthState.None(AuthState.None.AuthConnectionState.OFFLINE) -> DisconnectedAlertDialog()
                is AuthState.Error -> ErrorAlertDialog(
                    error = (authState as AuthState.Error).e
                ) { sessionViewModel.dismiss() }

                else -> {}
            }
        }
    }
}

internal fun NavController.navigateToAuthentication() {
    this.navigate(NavigationRoutes.StartRoute.AuthenticationRoute.route) {
        popUpTo(0)
    }
}

