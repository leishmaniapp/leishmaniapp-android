package com.leishmaniapp.infrastructure.cloud

import com.leishmaniapp.cloud.auth.AuthRequest
import com.leishmaniapp.cloud.auth.AuthResponse
import com.leishmaniapp.cloud.auth.AuthServiceClient
import com.leishmaniapp.cloud.auth.DecodeResponse
import com.leishmaniapp.cloud.auth.TokenPayload
import com.leishmaniapp.cloud.auth.TokenRequest
import com.leishmaniapp.cloud.types.StatusResponse
import com.leishmaniapp.domain.services.IAuthService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Use gRPC (LeishmaniappCloudServicesV2) to provide [IAuthService]
 */
class GrpcAuthServiceImpl @Inject constructor(

    /**
     * gRPC configuration
     */
    configuration: GrpcServiceConfiguration,

    ) : IAuthService {

    /**
     * Protobuf definition of a [AuthServiceClient]
     */
    private val client: AuthServiceClient = configuration.client.create(AuthServiceClient::class)

    override suspend fun authenticate(request: AuthRequest): Result<AuthResponse> =
        try {
            Result.success(client.Authenticate().execute(request))
        } catch (e: Throwable) {
            Result.failure(e)
        }

    override suspend fun verifyToken(request: TokenRequest): Result<StatusResponse> =
        try {
            Result.success(client.VerifyToken().execute(request))
        } catch (e: Throwable) {
            Result.failure(e)
        }

    override suspend fun decodeToken(request: TokenRequest): Result<DecodeResponse> =
        try {
            Result.success(client.DecodeToken().execute(request))
        } catch (e: Throwable) {
            Result.failure(e)
        }
}