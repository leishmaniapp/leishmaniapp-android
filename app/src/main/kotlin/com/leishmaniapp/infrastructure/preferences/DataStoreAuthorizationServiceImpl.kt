package com.leishmaniapp.infrastructure.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.infrastructure.di.InjectScopeWithIODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration
import javax.inject.Inject

/**
 * Create a [DataStore] to persist authentication
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreAuthorizationServiceImpl.TAG)

/**
 * [DataStore] implementation for the [IAuthorizationService]
 */
class DataStoreAuthorizationServiceImpl @Inject constructor(

    /**
     * Android context to access [Context.dataStore]
     */
    @ApplicationContext context: Context,

    /**
     * credentials [StateFlow] coroutine scope
     */
    @InjectScopeWithIODispatcher
    coroutineScope: CoroutineScope,

    ) : IAuthorizationService {

    companion object {
        /**
         * TAG for using with [DataStore]
         */
        val TAG: String = DataStoreAuthorizationServiceImpl::class.simpleName!!
    }

    /**
     * Definition for the [DataStore] keys
     */
    private object PreferencesKeys {

        /**
         * Key for accessing a [AccessToken] stored in [DataStore]
         */
        val credentialsKey = stringPreferencesKey("${TAG}_credentials")
    }

    /**
     * Grab a reference to the context's [DataStore]
     */
    private val dataStore = context.dataStore

    /**
     * Currently stored [Credentials]
     */
    @OptIn(FlowPreview::class)
    override val credentials: StateFlow<Credentials?> =
        // Fetch the data from DataStore
        dataStore.data
            .flowOn(Dispatchers.IO)
            // Get the JSON data decoded
            .mapLatest<Preferences, Credentials?> { preferences ->
                preferences[PreferencesKeys.credentialsKey]?.let { Json.decodeFromString(it) }
            }
            // Log the credentials
            .onEach { c -> Log.i(TAG, "Global credentials set for '${c?.email.toString()}'") }
            // Use within StateFlow
            .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override suspend fun authorize(crendentials: Credentials): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences[PreferencesKeys.credentialsKey] =
                        Json.encodeToString(crendentials)
                }
                return@withContext Result.success(Unit)
            } catch (e: Throwable) {
                return@withContext Result.failure(e)
            }
        }

    override suspend fun forget() =
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