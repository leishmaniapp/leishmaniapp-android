package com.leishmaniapp.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.views.NavigationDestinations
import com.leishmaniapp.presentation.views.start.AuthenticationScreen
import com.leishmaniapp.presentation.views.start.DiseasesMenuScreen
import com.leishmaniapp.presentation.views.start.GreetingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeishmaniappTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = NavigationDestinations.StartNavigationDestinations.Greeting.route
                ) {
                    /* # -- StartNavigationDestinations -- # */

                    composable(NavigationDestinations.StartNavigationDestinations.Greeting.route) {
                        GreetingsScreen {
                            /*TODO: Navigate to authentication or disease selection menu*/
                            navController.navigate(NavigationDestinations.StartNavigationDestinations.Authentication.route)
                        }
                    }

                    composable(NavigationDestinations.StartNavigationDestinations.Authentication.route) {
                        AuthenticationScreen { username, password ->
                            /*TODO: Authenticate and go to disease selection */
                            navController.navigate(NavigationDestinations.StartNavigationDestinations.DiseaseSelection.route) {
                                this.popUpTo(NavigationDestinations.StartNavigationDestinations.Greeting.route)
                            }
                        }
                    }

                    composable(NavigationDestinations.StartNavigationDestinations.DiseaseSelection.route) {
                        DiseasesMenuScreen { disease ->
                            /* TODO: Initialize diagnosis module for given disease */
                        }
                    }
                }
            }
        }
    }
}


