package com.leishmaniapp.presentation.views

sealed class NavigationDestinations(val route: String) {
    sealed class StartNavigationDestinations(route: String) :
        NavigationDestinations("start/$route") {
        data object Greeting : StartNavigationDestinations("greetings")
        data object Authentication : StartNavigationDestinations("authentication")
        data object DiseaseSelection : StartNavigationDestinations("disease")
    }
}