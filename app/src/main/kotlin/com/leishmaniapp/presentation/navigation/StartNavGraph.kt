package com.leishmaniapp.presentation.navigation

import android.app.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.state.AuthState
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.DisconnectedAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.ErrorAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.ProfileAlertDialog
import com.leishmaniapp.presentation.ui.views.start.AuthenticationScreen
import com.leishmaniapp.presentation.ui.views.start.DiseasesMenuScreen
import com.leishmaniapp.presentation.ui.views.start.GreetingsScreen
import com.leishmaniapp.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.startNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
) {
    navigation(
        route = NavigationRoutes.StartRoute.route,
        startDestination = NavigationRoutes.StartRoute.GreetingsScreen.route,
    ) {
        composable(route = NavigationRoutes.StartRoute.GreetingsScreen.route) {
            GreetingsScreen(onContinue = {
                navController.navigateToAuthentication()
            })
        }

        composable(route = NavigationRoutes.StartRoute.AuthenticationRoute.route) {

            // Grab the authentication state
            val authState by authViewModel.authState.observeAsState(initial = AuthState.Busy)

            // If authenticated, then exit this screen
            if (authState is AuthState.Authenticated) {
                navController.navigateToDiseasesMenu()
            }

            // Show the authentication screen
            AuthenticationScreen(onAuthenticate = { email, password ->
                authViewModel.authenticate(email, password)
            })

            when (authState) {
                is AuthState.Busy -> BusyAlertDialog()
                AuthState.None(AuthState.None.AuthConnectionState.OFFLINE) -> DisconnectedAlertDialog()
                is AuthState.Error -> ErrorAlertDialog(
                    error = (authState as AuthState.Error).e
                ) { authViewModel.dismiss() }

                else -> {}
            }
        }

        composable(route = NavigationRoutes.StartRoute.DiseasesRoute.route) {

            // Grab the authentication state
            val authState by authViewModel.authState.observeAsState(initial = AuthState.Busy)

            // Show the profile alert
            var showProfileAlert by rememberSaveable {
                mutableStateOf(false)
            }

            DiseasesMenuScreen(
                online = false, /* TODO */
                onProfileSelection = {
                    showProfileAlert = true
                },
                onDiseaseSelection = { disease ->
                    /* TODO: Select a disease */
                },
            )

            if (showProfileAlert && authState is AuthState.Authenticated) {
                ProfileAlertDialog(
                    specialist = (authState as AuthState.Authenticated).s,
                    onLogout = {
                        authViewModel.logout()
                    },
                    onDismiss = {
                        showProfileAlert = false
                    })
            } else if (authState is AuthState.Busy) {
                BusyAlertDialog()
            }
        }
    }
}

internal fun NavController.navigateToAuthentication() {
    this.navigate(NavigationRoutes.StartRoute.AuthenticationRoute.route) {
        popUpTo(0)
    }
}

internal fun NavController.navigateToDiseasesMenu() {
    this.navigate(NavigationRoutes.StartRoute.DiseasesRoute.route) {
        popUpTo(0)
    }
}