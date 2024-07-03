package com.leishmaniapp.infrastructure.persistance.repository

import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.repository.ICredentialsRepository
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.ShaHash
import com.leishmaniapp.infrastructure.persistance.dao.RoomCredentialsDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomCredentialsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [ICredentialsRepository] implementation for the Room database
 */
class RoomCredentialsRepositoryImpl @Inject constructor(

    private val dao: RoomCredentialsDao,

    ) : ICredentialsRepository {
    override suspend fun upsertCredentials(credentials: Credentials) =
        dao.upsertCredentials(RoomCredentialsEntity(credentials))

    override suspend fun deleteCredentials(email: Email) = dao.deleteCredentials(email)

    override fun credentialsByEmailAndHash(email: Email, hash: ShaHash): Flow<Credentials?> =
        dao.credentialsByEmailAndHash(email, hash).map { it?.toCredentials() }
}