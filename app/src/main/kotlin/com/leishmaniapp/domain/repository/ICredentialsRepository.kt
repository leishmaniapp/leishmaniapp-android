package com.leishmaniapp.domain.repository

import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.ShaHash
import kotlinx.coroutines.flow.Flow

/**
 * Manipulate [Credentials] in database
 */
interface ICredentialsRepository {
    /**
     * Insert new [Credentials] (or Update existing ones) into database
     */
    suspend fun upsertCredentials(credentials: Credentials)

    /**
     * Delete [Credentials] given their primary key
     */
    suspend fun deleteCredentials(email: Email)

    /**
     * Delete [Credentials]
     */
    suspend fun deleteCredentials(credentials: Credentials)

    /**
     * Get all the [Email] fields from all the stored [Credentials]
     */
    fun getAllEmailsFromCredentials(): Flow<List<Email>>

    /**
     * Get credentials given their [Email] and [ShaHash]
     */
    fun credentialsByEmailAndHash(email: Email, hash: ShaHash): Flow<Credentials?>
}