package com.leishmaniapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leishmaniapp.cloud.auth.AuthRequest
import com.leishmaniapp.cloud.auth.TokenRequest
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.exceptions.BadAuthenticationException
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import com.leishmaniapp.domain.exceptions.NetworkException
import com.leishmaniapp.domain.protobuf.fromProto
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.domain.repository.ITokenRepository
import com.leishmaniapp.domain.services.IAuthService
import com.leishmaniapp.domain.services.INetworkService
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Password
import com.leishmaniapp.presentation.state.AuthState
import com.leishmaniapp.utilities.extensions.throwOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log

/**
 * Handle authentication state
 */
@HiltViewModel
class AuthViewModel @Inject constructor(

    /**
     * Keep state accross configuration changes
     */
    savedStateHandle: SavedStateHandle,

    // Services
    private val networkService: INetworkService,
    private val authService: IAuthService,

    // Repositories
    private val tokenRepository: ITokenRepository,
    private val specialistRepository: ISpecialistsRepository

) : ViewModel() {

    companion object {
        /**
         * TAG for using with [Log] and [SavedStateHandle]
         */
        val TAG: String = AuthViewModel::class.simpleName!!
    }

    private val _authState: MutableLiveData<AuthState> =
        savedStateHandle.getLiveData(
            "$TAG:authState", AuthState.Busy,
        )

    init {
        // Change authentication status based on token
        viewModelScope.launch {
            tokenRepository.accessToken.collectLatest { token ->
                // If authentication token is valid
                if (token != null) {
                    // Get the specialist
                    specialistRepository.specialistByToken(token).collect { specialist ->
                        // Specialist found
                        if (specialist != null) {
                            _authState.value = AuthState.Authenticated(specialist)
                        } else {
                            // No specialist, unauthenticated
                            dismiss()
                        }
                    }
                } else {
                    // No access token, not authenticated
                    dismiss()
                }
            }
        }

        // Store incoming authentication tokens
        viewModelScope.launch {
            tokenRepository.accessToken.filterNotNull().collect { token ->
                // Get the specialist from the remote server
                if (networkService.checkIfInternetConnectionIsAvailable()) {
                    // Decode the token contents
                    val payload = try {
                        withContext(Dispatchers.IO) {
                            authService.decodeToken(TokenRequest(token = token))
                                .getOrThrow()
                                .run { status!!.code.throwOrElse { payload!! } }
                        }
                    } catch (e: Exception) {
                        // Catch network errors
                        _authState.value = AuthState.Error(NetworkException(e))
                        // Forget the authentication token
                        tokenRepository.forgetAuthenticationToken()
                        return@collect
                    }

                    // Get the specialist
                    val specialist = Specialist.fromProto(payload.specialist!!, token)
                    // Update the specialist in the database
                    specialistRepository.upsertSpecialist(specialist)
                }
            }
        }
    }

    /**
     * Authentication status representation
     */
    val authState: LiveData<AuthState> = _authState

    /**
     * Try to authenticate user in remote server
     */
    fun authenticate(email: Email, password: Password) {
        _authState.value = AuthState.Busy;
        viewModelScope.launch {
            try {
                // Get the authentication token
                val token: AccessToken = withContext(Dispatchers.IO) {
                    authService.authenticate(AuthRequest(email, password))
                        .getOrThrow().run { status!!.code.throwOrElse { token!! } }
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
     * Dismiss the current [AuthState] to [AuthState.None], useful for getting rid of exceptions
     */
    fun dismiss() {
        viewModelScope.launch {
            _authState.value =
                AuthState.None(
                    if (networkService.checkIfInternetConnectionIsAvailable()) {
                        AuthState.None.AuthConnectionState.ONLINE
                    } else {
                        AuthState.None.AuthConnectionState.OFFLINE
                    }
                )
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