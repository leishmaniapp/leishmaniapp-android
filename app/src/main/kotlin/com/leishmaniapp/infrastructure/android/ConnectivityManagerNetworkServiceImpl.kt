package com.leishmaniapp.infrastructure.android

import android.content.Context
import android.net.ConnectivityManager
import com.leishmaniapp.domain.services.INetworkService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * [INetworkService] using Android SDK tools
 */
class ConnectivityManagerNetworkServiceImpl @Inject constructor(@ApplicationContext val context: Context) :
    INetworkService {

    /**
     * Check if internet is available using [ConnectivityManager]
     */
    override suspend fun checkIfInternetConnectionIsAvailable(): Boolean {
        // Get the connectivity manager
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the active network
        val networkInfo = connectivityManager.activeNetwork
        return networkInfo != null
    }
}