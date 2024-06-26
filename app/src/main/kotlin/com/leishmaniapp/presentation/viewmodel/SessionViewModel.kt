package com.leishmaniapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.cloud.auth.AuthRequest
import com.leishmaniapp.cloud.auth.TokenRequest
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.exceptions.BadAuthenticationException
import com.leishmaniapp.domain.exceptions.GenericException
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import com.leishmaniapp.domain.exceptions.NetworkException
import com.leishmaniapp.domain.protobuf.fromProto
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.repository.ITokenRepository
import com.leishmaniapp.domain.services.IAuthService
import com.leishmaniapp.domain.services.INetworkService
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Password
import com.leishmaniapp.presentation.viewmodel.state.AuthState
import com.leishmaniapp.utilities.extensions.throwOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
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
    networkService: INetworkService,

    // Repositories
    private val tokenRepository: ITokenRepository,
    private val specialistRepository: ISpecialistsRepository,

    ) : ViewModel() {

    companion object {
        /**
         * TAG for using with [Log] and [SavedStateHandle]
         */
        val TAG: String = SessionViewModel::class.simpleName!!
    }

    private val _authState: MutableLiveData<AuthState> = savedStateHandle.getLiveData(
        "authState", AuthState.Busy,
    )

    /**
     * Authentication status representation
     */
    val authState: LiveData<AuthState> = _authState

    /**
     * Get the current network state
     */
    private val networkState: StateFlow<INetworkService.NetworkState> =
        networkService.networkState()
            // Run the flow on IO
            .flowOn(Dispatchers.IO)
            // Update the connection state
            .onEach { status ->
                if (authState.value is AuthState.None) {
                    _authState.value = AuthState.None.getConnectionState(status)
                }
            }
            // Transform into StateFlow
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                INetworkService.NetworkState.OFFLINE,
            )

    /**
     * [AccessToken] flow, automatically stores the new [Specialist] information associated to the
     * token contents in the datase
     */
    val token: StateFlow<AccessToken?> = tokenRepository.accessToken
        // Run the flow on IO
        .flowOn(Dispatchers.IO)
        // For each new token, decode the contents and update the specialist metadata
        .onEach { token ->
            // Change the busy status
            if (authState.value is AuthState.Busy) {
                _authState.value = AuthState.None.getConnectionState()
            }
            // Update the specialist from the remote server if token was provided
            if (token != null && networkState.value != INetworkService.NetworkState.OFFLINE) {
                // Decode the token contents
                val payload = withContext(Dispatchers.IO) {
                    authService.decodeToken(TokenRequest(token = token)).getOrThrow()
                        .run { status!!.code.throwOrElse { payload!! } }
                }
                // Get the specialist
                val specialist = Specialist.fromProto(payload.specialist!!, token)
                // Update the specialist in the database
                specialistRepository.upsertSpecialist(specialist)
            }
        }
        // Catch errors within the call, can only be network errors
        .catch { err ->
            Log.e(TAG, "Exception during AccessToken Flow", err)
            _authState.value = AuthState.Error(NetworkException(err))
        }
        // Transform to a state flow
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * [Specialist] flow, automatically sets the authentication status when a new [AccessToken] arrives
     */
    val specialist: StateFlow<Specialist?> = token
        // Transform the latest token flow into a specialist
        .flatMapLatest { token ->
            if (token != null) {
                specialistRepository.specialistByToken(token)
            } else {
                flowOf(null)
            }
        }
        // Run the flow on IO
        .flowOn(Dispatchers.IO)
        // For each new specialist, set the authentication state
        .onEach { specialist ->
            _authState.value = when {
                specialist != null -> AuthState.Authenticated(specialist)
                authState.value is AuthState.Busy -> AuthState.Busy
                else -> AuthState.None.getConnectionState()
            }
        }
        // Catch errors within the call
        .catch { err ->
            Log.e(TAG, "Exception during Specialist Flow", err)
            _authState.value = AuthState.Error(GenericException(err))
        }
        // Transform to a state flow
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    /**
     * Create a new [AuthState.None] state gathering the network state from a [INetworkService]
     */
    private fun AuthState.None.Companion.getConnectionState(
        state: INetworkService.NetworkState = networkState.value,
    ): AuthState.None =
        AuthState.None(
            if (state == INetworkService.NetworkState.OFFLINE) {
                AuthState.None.AuthConnectionState.OFFLINE
            } else {
                AuthState.None.AuthConnectionState.ONLINE
            }
        )

    /**
     * Dismiss the current [AuthState] to [AuthState.None], useful for getting rid of exceptions
     */
    fun dismiss() {
        viewModelScope.launch {
            _authState.value = AuthState.None.getConnectionState()
        }
    }

    /**
     * Try to authenticate user in remote server
     */
    fun authenticate(email: Email, password: Password) {
        _authState.value = AuthState.Busy;
        viewModelScope.launch {
            try {
                // Get the authentication token
                val token: AccessToken = withContext(Dispatchers.IO) {
                    authService.authenticate(AuthRequest(email, password)).getOrThrow()
                        .run { status!!.code.throwOrElse { token!! } }
                }

                // Store the token and the specialist
                // Should automatically login as init is listening on token changes
                tokenRepository.storeAuthenticationToken(token).getOrThrow()

            } catch (e: LeishmaniappException) {

                // Catch application exceptions
                Log.e(TAG, e.toString(), e)
                _authState.value = AuthState.Error(e)

            } catch (e: Exception) {

                // Catch gRPC exceptions
                Log.e(TAG, e.toString(), e)
                _authState.value = AuthState.Error(NetworkException(e))
            }
        }
    }

    /**
     * Forget authentication and [dismiss] state
     */
    fun logout() {
        // Get the specialist (Required authentication)
        val s = (_authState.value as? AuthState.Authenticated ?: return).s
        // Change state to busy
        _authState.value = AuthState.Busy
        // Change specialist token to empty
        viewModelScope.launch(Dispatchers.IO) {
            // Remove token from specialist
            specialistRepository.upsertSpecialist(s.copy(token = null))
            // Forget token
            tokenRepository.forgetAuthenticationToken().onFailure { e ->
                Log.e(TAG, "Failed logout", e)
                _authState.value = AuthState.Error(BadAuthenticationException())
            }
        }
    }
}