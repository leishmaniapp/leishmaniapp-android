package com.leishmaniapp.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.entities.Username
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.persistance.database.ApplicationDatabase
import com.leishmaniapp.usecases.IAuthenticationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
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

    fun exportDatabase(context: Context) {

        val databasePath = context.getDatabasePath("database").absolutePath
        val databaseFile = File(databasePath)

        val file = File(context.cacheDir, "database_backup.sqlite")
        if (file.exists()) {
            file.delete()
        }

        databaseFile.copyTo(file)
        Log.d("DatabaseShare", "Stored in file: ${file.absolutePath}")

        val uri = FileProvider.getUriForFile(context, "com.leishmaniapp", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/x-sqlite3"
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        context.startActivity(Intent.createChooser(intent, "Share via"))
    }

    fun importDatabaseBegin(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        val intent = Intent().apply {
            type = "*/*"
            action = Intent.ACTION_GET_CONTENT
        }

        launcher.launch(intent)
    }

    fun importDatabaseComplete(context: Context, uri: Uri) {
        Log.d("ImportDatabase", uri.toString())

        val databasePath = context.getDatabasePath("database").absolutePath
        val databaseFile = File(databasePath)

        databaseFile.delete()
        databaseFile.createNewFile()

        context.contentResolver.openInputStream(uri).use { inputStream ->
            databaseFile.outputStream().use { outputStream ->
                outputStream.write(inputStream!!.readBytes())
            }
        }
    }
}