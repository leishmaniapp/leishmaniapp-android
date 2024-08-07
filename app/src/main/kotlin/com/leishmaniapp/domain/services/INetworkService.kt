package com.leishmaniapp.domain.services

import kotlinx.coroutines.flow.Flow

/**
 * Check if internet connection is present
 * For application internal use only
 */
fun interface INetworkService {

    /**
     * Current connection state and properties
     */
    enum class NetworkState {
        /**
         * Connected to internet
         */
        ONLINE,

        /**
         * Connected to a metered network
         */
        METERED,

        /**
         * Not connected to internet
         */
        OFFLINE
    }

    /**
     * [Flow] with the current network connection status
     */
    fun state(): Flow<NetworkState>
}