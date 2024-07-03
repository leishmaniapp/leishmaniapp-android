package com.leishmaniapp.infrastructure.android.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.leishmaniapp.domain.services.INetworkService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * [INetworkService] using Android SDK tools
 */
class ConnectivityManagerNetworkServiceImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : INetworkService {

    /**
     * Get the system connectivity manager
     */
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

    /**
     * List of [NetworkCapabilities] required for a successfull connection
     */
    private val networkCapabilities: List<Int> = listOf(
        NetworkCapabilities.NET_CAPABILITY_INTERNET,
        NetworkCapabilities.NET_CAPABILITY_VALIDATED,
    )

    /**
     * [NetworkRequest] containg all the required [NetworkCapabilities]
     */
    private val networkRequest = NetworkRequest.Builder().apply {
        networkCapabilities.forEach { c ->
            addCapability(c)
        }
    }.build()

    /**
     * Get the [Network]'s [NetworkCapabilities] and map them using [NetworkCapabilities.toState]
     */
    private fun Network?.toState(): INetworkService.NetworkState =
        connectivityManager.getNetworkCapabilities(this).toState()

    /**
     * Map current [NetworkCapabilities] to a [INetworkService.NetworkState]
     */
    private fun NetworkCapabilities?.toState(): INetworkService.NetworkState {
        // Get the current network capabilities, and check if they match required capabilities
        val capabilities: NetworkCapabilities? = this?.takeIf { capabilities ->
            // Match all the capabilities
            networkCapabilities.all { c -> capabilities.hasCapability(c) }
        }

        // Return the corresponding state
        return when {
            capabilities == null -> INetworkService.NetworkState.OFFLINE
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) -> INetworkService.NetworkState.ONLINE
            else -> INetworkService.NetworkState.METERED
        }
    }

    override fun networkState(): Flow<INetworkService.NetworkState> = callbackFlow {
        // Create the network callback
        val callback = object : ConnectivityManager.NetworkCallback() {

            // Callback when a new connection arrives
            override fun onAvailable(network: Network) {
                trySend(network.toState())
                super.onAvailable(network)
            }

            // Callback when a connection is lost
            override fun onLost(network: Network) {
                trySend(INetworkService.NetworkState.OFFLINE)
                super.onLost(network)
            }

            // Change network state when a capability changes
            override fun onCapabilitiesChanged(
                network: Network, networkCapabilities: NetworkCapabilities
            ) {
                trySend(networkCapabilities.toState())
                super.onCapabilitiesChanged(network, networkCapabilities)
            }

        }

        // Get the initial state
        trySend(connectivityManager.activeNetwork.toState())
        // Register the callback on the request
        connectivityManager.registerNetworkCallback(networkRequest, callback)

        // Await close service
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}