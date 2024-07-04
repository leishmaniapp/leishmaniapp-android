package com.leishmaniapp.domain.services

import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.types.AccessToken
import kotlinx.coroutines.flow.Flow

/**
 * Manipulate current authorization [Credentials], only one set of [Credentials] can be used at the time
 */
interface IAuthorizationService {

    /**
     * currently stored [Credentials]
     */
    val credentials: Flow<Credentials?>

    /**
     * Store [Credentials] in application memory
     */
    suspend fun authorize(crendentials: Credentials): Result<Unit>

    /**
     * Forget the authorization [Credentials]
     */
    suspend fun forget(): Result<Unit>
}