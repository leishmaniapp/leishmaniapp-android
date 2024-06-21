package com.leishmaniapp.domain.services

import com.leishmaniapp.cloud.auth.AuthRequest
import com.leishmaniapp.cloud.auth.AuthResponse
import com.leishmaniapp.cloud.auth.TokenPayload
import com.leishmaniapp.cloud.auth.TokenRequest
import com.leishmaniapp.cloud.types.StatusResponse
import com.leishmaniapp.domain.entities.Specialist


/**
 * Service for manipulating authentication data,
 * This service is based upon the LeishmaniappCloudServicesv2 definition
 * [visit protobuf_schema for more information](https://github.com/leishmaniapp/protobuf_schema)
 */
interface IAuthService {

    /**
     * Authenticate an user and return its token
     */
    suspend fun authenticate(request: AuthRequest): Result<AuthResponse>

    /**
     * Verify a token signature
     */
    suspend fun verifyToken(request: TokenRequest): Result<StatusResponse>

    /**
     * Obtain token contents, must be a local call
     */
    suspend fun decodeToken(request: TokenRequest): Result<TokenPayload>
}