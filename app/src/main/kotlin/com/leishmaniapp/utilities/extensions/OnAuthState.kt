package com.leishmaniapp.utilities.extensions

import com.leishmaniapp.domain.services.INetworkService
import com.leishmaniapp.presentation.state.AuthState

/**
 * Create a new [AuthState.None] state gathering the network state from a [INetworkService]
 */
suspend fun AuthState.None.Companion.getConnectionStateFromService(service: INetworkService): AuthState.None =
    AuthState.None(
        if (service.checkIfInternetConnectionIsAvailable()) {
            AuthState.None.AuthConnectionState.ONLINE
        } else {
            AuthState.None.AuthConnectionState.OFFLINE
        }
    )
