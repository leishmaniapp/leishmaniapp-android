package com.leishmaniapp.infrastructure.service.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leishmaniapp.domain.services.IOngoingDiagnosisService
import com.leishmaniapp.domain.types.Email
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
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

    ) : IOngoingDiagnosisService {

    companion object {
        /**
         * TAG for using with [DataStore]
         */
        val TAG: String = DataStoreOngoingDiagnosisServiceImpl::class.simpleName!!
    }

    /**
     * Grab a reference to the context's [DataStore]
     */
    private val dataStore = context.dataStore

    override suspend fun setOngoingDiagnosis(specialist: Email, uuid: UUID): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(specialist)] = uuid.toString()
                }
                return@withContext Result.success(Unit)
            } catch (e: Throwable) {
                return@withContext Result.failure(e)
            }
        }

    override suspend fun getOngoingDiagnosis(specialist: Email): Flow<UUID?> =
        coroutineScope {
            // Fetch the data from DataStore
            dataStore.data
                .flowOn(Dispatchers.IO)
                // Get the JSON data decoded
                .mapLatest { preferences ->
                    preferences[stringPreferencesKey(specialist)]?.let {
                        UUID.fromString(it)
                    }
                }
        }

    override suspend fun removeOngoingDiagnosis(specialist: Email) =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences.remove(stringPreferencesKey(specialist))
                }
                return@withContext Result.success(Unit)
            } catch (e: Throwable) {
                return@withContext Result.failure(e)
            }
        }
}