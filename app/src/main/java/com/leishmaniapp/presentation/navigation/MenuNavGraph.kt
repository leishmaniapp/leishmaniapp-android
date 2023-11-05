package com.leishmaniapp.presentation.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leishmaniapp.R
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import com.leishmaniapp.presentation.views.menu.DatabaseScreen
import com.leishmaniapp.presentation.views.menu.MainMenuScreen

fun NavGraphBuilder.menuNavGraph(
    navController: NavHostController,
    applicationViewModel: ApplicationViewModel
) {
    navigation(
        route = NavigationRoutes.MenuRoute.route,
        startDestination = NavigationRoutes.MenuRoute.MainMenuRoute.route
    ) {
        composable(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
            // Show the main menu screen
            MainMenuScreen(
                disease = applicationViewModel.disease!!,
                onStartDiagnosis = { navController.navigateToInitializeDiagnosis() },
                onPatientList = { navController.navigateToPatientsNavGraph() },
                onAwaitingDiagnoses = { navController.navigateToAwaitingDiagnosis() },
                onDatabase = { navController.navigateToDatabase() },
            )
        }

        composable(NavigationRoutes.MenuRoute.DatabaseRoute.route) {
            val context = LocalContext.current
            var showSuccessAlert by remember {
                mutableStateOf(false)
            }

            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                    if (activityResult.resultCode == Activity.RESULT_OK &&
                        activityResult.data?.data != null
                    ) {
                        applicationViewModel.importDatabaseComplete(
                            context,
                            activityResult.data!!.data!!
                        )

                        showSuccessAlert = true
                    }
                }

            DatabaseScreen(
                onExit = { navController.popBackStack() },
                onImport = { applicationViewModel.importDatabaseBegin(launcher) },
                onExport = { applicationViewModel.exportDatabase(context) })

            if (showSuccessAlert) {
                AlertDialog(onDismissRequest = {
                    showSuccessAlert = false
                }) {
                    Card {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.alert_import_success)
                        )
                    }
                }
            }
        }
    }
}

fun NavHostController.navigateToMenu() {
    this.navigate(NavigationRoutes.MenuRoute.MainMenuRoute.route) {
        popUpTo(0) // Pop every other route
    }
}

private fun NavHostController.navigateToDatabase() {
    this.navigate(NavigationRoutes.MenuRoute.DatabaseRoute.route)
}