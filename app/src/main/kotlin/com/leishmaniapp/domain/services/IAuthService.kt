package com.leishmaniapp.domain.services

import com.leishmaniapp.cloud.auth.AuthRequest
import com.leishmaniapp.cloud.auth.AuthResponse
import com.leishmaniapp.cloud.auth.DecodeResponse
import com.leishmaniapp.cloud.auth.TokenPayload
import com.leishmaniapp.cloud.auth.TokenRequest
import com.leishmaniapp.cloud.types.StatusResponse
import com.leishmaniapp.domain.entities.Specialist


/**
 * Service for manipulating authenticating on a remote server,
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
     * Obtain token contents
     */
    suspend fun decodeToken(request: TokenRequest): Result<DecodeResponse>
}