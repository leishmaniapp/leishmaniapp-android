package com.leishmaniapp.infrastructure.service.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.services.IOngoingDiagnosisService
import com.leishmaniapp.infrastructure.di.InjectScopeWithIODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject


/**
 * Create a [DataStore] to persist ongoing diagnosis
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreOngoingDiagnosisServiceImpl.TAG)

/**
 * [DataStore] implementation for the [IOngoingDiagnosisService]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreOngoingDiagnosisServiceImpl @Inject constructor(

    /**
     * Android context to access [Context.dataStore]
     */
    @ApplicationContext context: Context,

    /**
     * credentials [StateFlow] coroutine scope
     */
    @InjectScopeWithIODispatcher
    coroutineScope: CoroutineScope,

    ) : IOngoingDiagnosisService {

    companion object {
        /**
         * TAG for using with [DataStore]
         */
        val TAG: String = DataStoreOngoingDiagnosisServiceImpl::class.simpleName!!
    }

    /**
     * Definition for the [DataStore] keys
     */
    private object PreferencesKeys {

        /**
         * Key for accessing a [UUID] for the ongoing diagnosis
         */
        val credentialsKey = stringPreferencesKey("${TAG}_diagnosis")
    }

    /**
     * Grab a reference to the context's [DataStore]
     */
    private val dataStore = context.dataStore

    override suspend fun setOngoingDiagnosis(uuid: UUID): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences[PreferencesKeys.credentialsKey] = uuid.toString()
                }
                return@withContext Result.success(Unit)
            } catch (e: Throwable) {
                return@withContext Result.failure(e)
            }
        }

    override val ongoingDiagnosis: StateFlow<UUID?> =
        // Fetch the data from DataStore
        dataStore.data
            .flowOn(Dispatchers.IO)
            // Get the JSON data decoded
            .mapLatest<Preferences, UUID?> { preferences ->
                preferences[PreferencesKeys.credentialsKey]?.let {
                    UUID.fromString(it)
                }
            }
            // Log the diagnosis
            .onEach { c ->
                Log.i(
                    DataStoreAuthorizationServiceImpl.TAG,
                    "Global ongoing diagnosis (${c})"
                )
            }
            // Use within StateFlow
            .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override suspend fun removeOngoingDiagnosis() =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences.remove(PreferencesKeys.credentialsKey)
                }
                return@withContext Result.success(Unit)
            } catch (e: Throwable) {
                return@withContext Result.failure(e)
            }
        }
}