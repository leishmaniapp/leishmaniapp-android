package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IAuthenticationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Main Application [ViewModel], provides current selected disease and specialist logged in
 */
@HiltViewModel
class ApplicationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val applicationDatabase: ApplicationDatabase,
    val authenticationProvider: IAuthenticationProvider
) : ViewModel() {

    /**
     * Currently selected disease
     */
    var disease: Disease? = savedStateHandle["disease"]
        set(value) {
            // Store the saved state handle
            this.savedStateHandle["disease"] = value
            field = value
        }

    /**
     * Currently logged in specialist
     */
    var specialist: Specialist? = savedStateHandle["specialist"]
        set(value) {
            // Store the saved state handle
            this.savedStateHandle["specialist"] = value
            field = value
        }

    /**
     * Authenticate specialist
     * Look for specialist in [ApplicationDatabase],
     * if not present look for it in Cloud
     */
    suspend fun authenticate(username: Username, password: Password): Boolean {
        // Get the specialist
        specialist = authenticationProvider.authenticateSpecialist(username, password)
        return specialist != null
    }
}