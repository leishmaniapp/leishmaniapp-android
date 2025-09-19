package com.leishmaniapp.presentation.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.presentation.navigation.NavigationRoutes
import com.leishmaniapp.presentation.viewmodel.state.AuthState
import com.leishmaniapp.presentation.ui.dialogs.ProfileAlertDialog
import com.leishmaniapp.presentation.ui.dialogs.RecoverOngoingDiagnosisAlertDialog
import com.leishmaniapp.presentation.ui.layout.BusyScreen
import com.leishmaniapp.presentation.ui.views.menu.DiseasesMenuScreen
import com.leishmaniapp.presentation.ui.views.menu.MainMenuScreen
import com.leishmaniapp.presentation.viewmodel.SessionViewModel
import com.leishmaniapp.presentation.viewmodel.DiagnosisViewModel

fun NavGraphBuilder.menuNavGraph(
    navHostController: NavHostController,
    sessionViewModel: SessionViewModel,
    diagnosisViewModel: DiagnosisViewModel,
) {
    navigation(
        route = NavigationRoutes.MenuRoute.route,
        startDestination = NavigationRoutes.MenuRoute.DiseasesRoute.route,
    ) {

        composable(route = NavigationRoutes.MenuRoute.DiseasesRoute.route) {

            // Grab the authentication state
            val authState: AuthState.Authenticated? by sessionViewModel.state
                .map { it.authenticatedOrNull() }
                .observeAsState()

            if (authState == null) {
                LaunchedEffect(Unit) {
                    navHostController.navigateToAuthentication()
                }
                return@composable
            }

            // Get current diagnosis
            val diagnosis by diagnosisViewModel.diagnosis.collectAsStateWithLifecycle()

            // Get the network state
            val isOnline by sessionViewModel.isOnlineAuthenticationAvailable.collectAsStateWithLifecycle()

            // Show the profile alert
            var showProfileAlert by rememberSaveable {
                mutableStateOf(false)
            }

            DiseasesMenuScreen(
                online = isOnline,
                diseases = authState!!.s.diseases,
                onProfileSelection = {
                    showProfileAlert = true
                },
                onDiseaseSelection = {
                    diagnosisViewModel.setDisease(it)
                    navHostController.navigateToMenu()
                },
            )

            if (showProfileAlert) {
                ProfileAlertDialog(
                    specialist = authState!!.s,
                    onLogout = {
                        diagnosisViewModel.dismissDisease()
                        sessionViewModel.logout()
                    },
                    onDismiss = {
                        showProfileAlert = false
                    },
                )
            }

            if (diagnosis != null) {

                // Do not recover finalized or background diagnoses
                if (diagnosis!!.finalized || diagnosis!!.background) {
                    diagnosisViewModel.dismiss()
                    return@composable
                }

                // Show the recovery dialog
                RecoverOngoingDiagnosisAlertDialog(
                    onRecover = {
                        diagnosisViewModel.setDisease(disease = diagnosis!!.disease)
                        navHostController.navigateToDiagnosisRoute()
                    },
                    onDiscard = {
                        diagnosisViewModel.discard()
                    },
                )
            }
        }

        composable(NavigationRoutes.MenuRoute.MainMenuRoute.route) {

            // Grab the authentication state
            val authState: AuthState.Authenticated? by sessionViewModel.state
                .map { it.authenticatedOrNull() }
                .observeAsState()
            if (authState == null) {
                LaunchedEffect(Unit) {
                    navHostController.navigateToAuthentication()
                }
                return@composable
            }

            // Get the current disease
            val disease by diagnosisViewModel.disease.observeAsState()
            if (disease == null) {
                BusyScreen()
                return@composable
            }

            // Show the profile alert
            var showProfileAlert by rememberSaveable {
                mutableStateOf(false)
            }

            MainMenuScreen(
                disease = disease!!,
                onBackButton = {
                    // Delete the current disease
                    diagnosisViewModel.dismissDisease()
                    navHostController.popBackStack()
                },
                onProfileSelect = {
                    // Show the specialist profile card
                    showProfileAlert = true
                },
                onStartDiagnosis = { navHostController.navigateToDiagnosisRoute() },
                onPatientList = { navHostController.navigateToPatientsRoute() },
                onAwaitingDiagnoses = { navHostController.navigateToAwaitingDiagnoses() },
                onDatabase = {},
            )

            if (showProfileAlert) {
                ProfileAlertDialog(
                    specialist = (authState as AuthState.Authenticated).s,
                    onLogout = {
                        diagnosisViewModel.dismissDisease()
                        sessionViewModel.logout()
                    },
                    onDismiss = {
                        showProfileAlert = false
                    },
                )
            }
        }
    }
}

internal fun NavController.navigateToDiseasesMenu(
    builder: NavOptionsBuilder.() -> Unit = {
        popUpTo(0)
    }
) {
    this.navigate(NavigationRoutes.MenuRoute.DiseasesRoute.route, builder)
}

private fun NavController.navigateToMenu(
    builder: NavOptionsBuilder.() -> Unit = {
    }
) {
    this.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route, builder)
}

fun NavController.navigateReturnToMenu() {
    this.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
        popUpTo(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
            inclusive = true
        }
    }
}
