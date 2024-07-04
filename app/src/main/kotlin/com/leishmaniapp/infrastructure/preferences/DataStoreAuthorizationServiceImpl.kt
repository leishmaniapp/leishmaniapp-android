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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Create a [DataStore] to persist authentication
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreAuthorizationServiceImpl.TAG)

/**
 * [DataStore] implementation for the [IAuthorizationService]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreAuthorizationServiceImpl @Inject constructor(

    /**
     * Android context to access [Context.dataStore]
     */
    @ApplicationContext context: Context,

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
    override val credentials: Flow<Credentials?> = dataStore.data.mapLatest { preferences ->
        preferences[PreferencesKeys.credentialsKey]?.let { Json.decodeFromString(it) }
    }

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