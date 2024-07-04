package com.leishmaniapp.presentation.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.ui.dialogs.BusyAlertDialog
import com.leishmaniapp.presentation.viewmodel.state.AuthState
import com.leishmaniapp.presentation.ui.dialogs.ErrorAlertDialog
import com.leishmaniapp.presentation.ui.layout.BusyScreen
import com.leishmaniapp.presentation.ui.views.start.AuthenticationOfflineScreen
import com.leishmaniapp.presentation.ui.views.start.AuthenticationOnlineScreen
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

            // Signout for old sessions
            LaunchedEffect(key1 = true) {
                sessionViewModel.logout()
            }
        }

        composable(route = NavigationRoutes.StartRoute.AuthenticationRoute.route) {

            // Grab the authentication state
            val authState by sessionViewModel.state.observeAsState(initial = AuthState.Busy)
            val rememberedCredentials by sessionViewModel.rememberedCredentials.collectAsStateWithLifecycle()

            // If authenticated, then exit this screen
            if (authState is AuthState.Authenticated) {
                navHostController.navigateToDiseasesMenu()
            }

            // Show auth screens
            when (authState) {
                AuthState.None(AuthState.None.AuthConnectionState.OFFLINE) ->
                    AuthenticationOfflineScreen(emails = rememberedCredentials) { email, password ->
                        // Show the normal offline screen
                        sessionViewModel.authenticateOffline(email, password)
                    }

                AuthState.None(AuthState.None.AuthConnectionState.ONLINE) ->
                    // Show the normal authentication screen
                    AuthenticationOnlineScreen(onAuthenticate = { email, password ->
                        sessionViewModel.authenticateOnline(email, password)
                    })

                else -> {}
            }

            // Show alert dialogs
            when (authState) {
                is AuthState.Busy -> BusyAlertDialog()
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

