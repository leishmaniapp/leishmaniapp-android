package com.leishmaniapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.cloud.auth.AuthRequest
import com.leishmaniapp.cloud.auth.TokenRequest
import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.exceptions.BadAuthenticationException
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import com.leishmaniapp.domain.exceptions.NetworkException
import com.leishmaniapp.domain.protobuf.fromProto
import com.leishmaniapp.domain.repository.ICredentialsRepository
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.domain.services.IAuthService
import com.leishmaniapp.domain.services.INetworkService
import com.leishmaniapp.domain.services.IQueuingService
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Password
import com.leishmaniapp.infrastructure.security.hash
import com.leishmaniapp.presentation.viewmodel.state.AuthState
import com.leishmaniapp.utilities.extensions.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Handle authentication state and all the [Specialist] related data
 */
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class SessionViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    // Services
    private val authService: IAuthService,
    private val authorizationService: IAuthorizationService,
    private val queuingService: IQueuingService,
    networkService: INetworkService,

    // Repositories
    private val credentialsRepository: ICredentialsRepository,
    private val specialistRepository: ISpecialistsRepository,

    ) : ViewModel(), DismissableState {

    companion object {
        /**
         * TAG for using with [Log] and [SavedStateHandle]
         */
        val TAG: String = SessionViewModel::class.simpleName!!
    }

    private val _state: MutableLiveData<AuthState> = savedStateHandle.getLiveData(
        "authState", AuthState.Busy,
    )

    /**
     * Authentication status representation
     */
    val state: LiveData<AuthState> = _state

    /**
     * Get the current network state and set the addecuate [AuthState.None] value is present
     */
    val networkState: StateFlow<INetworkService.NetworkState> =
        networkService.state()
            // Run the flow on IO
            .flowOn(Dispatchers.IO)
            // Update the connection state
            .onEach { status ->
                if (state.value is AuthState.None) {
                    _state.value = AuthState.None.getConnectionState(status)
                }
            }
            .catch { e -> Log.e(TAG, "Failed to get network status", e) }
            // Transform into StateFlow
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                INetworkService.NetworkState.OFFLINE,
            )

    /**
     * Create a new [AuthState.None] state gathering the network state from a [INetworkService]
     */
    private fun AuthState.None.Companion.getConnectionState(
        state: INetworkService.NetworkState = networkState.value,
    ): AuthState.None = AuthState.None(
        if (state == INetworkService.NetworkState.OFFLINE) {
            AuthState.None.AuthConnectionState.OFFLINE
        } else {
            AuthState.None.AuthConnectionState.ONLINE
        }
    )

    /**
     * Dismiss the current [AuthState] to [AuthState.None], useful for getting rid of exceptions
     */
    override fun dismiss() {
        viewModelScope.launch {
            _state.value = AuthState.None.getConnectionState()
        }
    }

    /**
     * Get the currently authorized credentials
     */
    private val credentials: StateFlow<Credentials?> =
        authorizationService.credentials.flowOn(Dispatchers.IO)
            .flowOn(Dispatchers.IO)
            .onEach { cr ->
                if (cr != null) {
                    Log.i(TAG, "Started remote results synchronization")
                    queuingService.startSync()
                } else {
                    Log.w(TAG, "Canceled remote results synchronization")
                    queuingService.cancelSync()
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Get the currently logged in specialist
     */
    val specialist: StateFlow<Specialist?> =
        credentials
            .flatMapMerge {
                it?.let { c -> specialistRepository.specialistByEmail(c.email) }
                    ?: flowOf(null)
            }
            .onEach { s ->
                // Set the authentication value
                _state.value =
                    s?.let { AuthState.Authenticated(s) } ?: AuthState.None.getConnectionState()

                Log.i(TAG, "Authentication status changed for specialist: ${_state.value}")
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Get a list with all the remembered credentials
     */
    val rememberedCredentials: StateFlow<List<Email>> =
        credentialsRepository.getAllEmailsFromCredentials()
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    /**
     * Try to authenticate user in remote server
     */
    fun authenticateOnline(email: Email, password: Password) {
        _state.value = AuthState.Busy
        viewModelScope.launch {
            try {
                // Get the authentication token
                val token: AccessToken = withContext(Dispatchers.IO) {
                    authService.authenticate(AuthRequest(email, password)).getOrThrow()
                        .run { status!!.code.getOrThrow { token!! } }
                }

                Log.i(TAG, "New authentication token (${token.length} bytes) successfully gathered")

                // Get the specialist
                withContext(Dispatchers.IO) {
                    authService.decodeToken(TokenRequest(token)).getOrThrow().let { payload ->

                        Log.d(
                            TAG,
                            "Token contents decoded for specialist (StatusCode: ${payload.status?.code})"
                        )

                        // Get the specialist and insert it
                        payload.status!!.code.getOrThrow { payload.payload!! }.specialist!!.let { specialist ->
                            specialistRepository.upsertSpecialist(Specialist.fromProto(specialist))
                        }
                    }
                }

                // Create the specialist credentials
                Credentials(
                    email = email, token = token, password = password.hash()
                ).let { credentials ->
                    withContext(Dispatchers.IO) {
                        // Store the token and the credentials
                        credentialsRepository.upsertCredentials(credentials)
                        // Authorize the user
                        authorizationService.authorize(credentials)
                    }
                }

            } catch (e: LeishmaniappException) {

                // Catch application exceptions
                Log.e(TAG, e.toString(), e)
                _state.value = AuthState.Error(e)

            } catch (e: Exception) {

                // Catch gRPC exceptions
                Log.e(TAG, e.toString(), e)
                _state.value = AuthState.Error(NetworkException(e))
            }
        }
    }

    /**
     * Try to authenticate user using internal credentials
     */
    fun authenticateOffline(email: Email, password: Password) {
        _state.value = AuthState.Busy
        viewModelScope.launch {
            try {
                // Get the stored credentials
                credentialsRepository.credentialsByEmailAndHash(email, password.hash()).first()
                    // Authorize the user
                    ?.let { credentials -> authorizationService.authorize(credentials) }
                // If credentials were not found, throw authentication exception
                    ?: throw BadAuthenticationException()

            } catch (e: LeishmaniappException) {

                // Catch application exceptions
                Log.e(TAG, e.toString(), e)
                _state.value = AuthState.Error(e)

            } catch (e: Exception) {

                // Catch gRPC exceptions
                Log.e(TAG, e.toString(), e)
                _state.value = AuthState.Error(NetworkException(e))
            }
        }
    }

    /**
     * Forget authentication and [dismiss] state
     */
    fun forget() {
        credentials.value?.let { c ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    // Delete the user credentials
                    credentialsRepository.deleteCredentials(c)
                    // Remove user authorization
                    authorizationService.forget()
                }
            }
        }
    }

    /**
     * Unauthorize (keeping credentials) and [dismiss] state for switching users
     */
    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Remove user authorization
                authorizationService.forget()
            }
        }
    }
}