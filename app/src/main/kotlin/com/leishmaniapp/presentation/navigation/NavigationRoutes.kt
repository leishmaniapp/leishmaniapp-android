package com.leishmaniapp.presentation.navigation

/**
 * Navigation routes
 */
sealed class NavigationRoutes(val route: String) {
    data object StartRoute : NavigationRoutes("/start") {
        data object GreetingsScreen : NavigationRoutes(this.route + "/greetings")
        data object AuthenticationRoute : NavigationRoutes(this.route + "/authentication")
    }

    data object MenuRoute : NavigationRoutes("/menu") {
        data object DiseasesRoute : NavigationRoutes(this.route + "/diseases")
        data object MainMenuRoute : NavigationRoutes(this.route + "/main")
        data object SettingsRoute : NavigationRoutes(this.route + "/settings")
    }

    data object PatientsRoute : NavigationRoutes("/patients") {
        data object PatientList : NavigationRoutes(this.route + "/list")
        data object SelectPatient : NavigationRoutes(this.route + "/select")
        data object AddPatient : NavigationRoutes(this.route + "/add")
        data object PatientDiagnosisHistory : NavigationRoutes(this.route + "/history")
    }

    data object DiagnosisRoute : NavigationRoutes("/diagnosis") {
        data object InitializeDiagnosis : NavigationRoutes(this.route + "/init")
        data object DiagnosisCamera : NavigationRoutes(this.route + "/camera")
        data object DiagnosisAndAnalysis : NavigationRoutes(this.route + "/diagnosisAndAnalysis")
        data object DiagnosisImageGrid : NavigationRoutes(this.route + "/diagnosisImageGrid")
        data object DiagnosisTable : NavigationRoutes(this.route + "/diagnosistable")
        data object DiagnosticImageEdit : NavigationRoutes(this.route + "/diagnosticImageEdit")
        data object AwaitingDiagnosis : NavigationRoutes(this.route + "/awaiting")
        data object DiagnosticRemarks : NavigationRoutes(this.route + "/remarks")
    }
}
