package com.leishmaniapp.presentation.navigation

/**
 * Navigation routes
 */
sealed class NavigationRoutes(val route: String) {
    data object StartRoute : NavigationRoutes("/start") {
        data object GreetingsScreen : NavigationRoutes(this.route + "/greetings")
        data object AuthenticationRoute : NavigationRoutes(this.route + "/authentication")
        data object DiseasesRoute : NavigationRoutes(this.route + "/diseases")
    }

    data object MenuRoute : NavigationRoutes("/menu") {
        data object MainMenuRoute : NavigationRoutes(this.route + "/main")
        data object DatabaseRoute : NavigationRoutes(this.route + "/database")
    }

    data object PatientsRoute : NavigationRoutes("/patients"){

        data object AddPatient : NavigationRoutes(this.route + "/addPatient")
        data object PatientDiagnosisHistory: NavigationRoutes(this.route + "/patientDiagnosisHistory")
        data object  PatientList: NavigationRoutes(this.route + "/patientList")
    }
    data object DiagnosisRoute : NavigationRoutes("/diagnosis"){
        data object  DiagnosisAndAnalysis: NavigationRoutes(this.route + "/diagnosisAndAnalysis")
        data object  DiagnosisImageGrid: NavigationRoutes(this.route + "/diagnosisImageGrid")
        data object  DiagnosisTable: NavigationRoutes(this.route + "/diagnosistable")
        data object  DiagnosticImageEdit: NavigationRoutes(this.route + "/diagnosticImageEdit")
        data object  DiagnosticImageSection: NavigationRoutes(this.route + "/diagnosticImageEdit")
    }



}
