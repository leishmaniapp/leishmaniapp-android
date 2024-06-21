package com.leishmaniapp.domain.services

/**
 * Check if internet connection is present
 */
fun interface INetworkService {
    suspend fun checkIfInternetConnectionIsAvailable(): Boolean
}