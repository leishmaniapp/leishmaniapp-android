package com.leishmaniapp.domain.repository

import com.leishmaniapp.domain.types.AccessToken
import kotlinx.coroutines.flow.Flow

/**
 * Manipulate [AccessToken] stored in memory, used for keeping application session without new login,
 * only one [AccessToken] can be stored at the time
 */
interface ITokenRepository {

    /**
     * Get the currently stored [AccessToken] as a [Flow]
     */
    val accessToken: Flow<AccessToken?>

    /**
     * Check if an authentication token is present in storage
     * If set, it means that a user is logged in
     */
    val isTokenStored: Flow<Boolean>

    /**
     * Store a new [AccessToken] to keep user session alive
     */
    suspend fun storeAuthenticationToken(token: AccessToken): Result<Unit>

    /**
     * Forget about the current [AccessToken], session is revoked
     */
    suspend fun forgetAuthenticationToken(): Result<Unit>
}