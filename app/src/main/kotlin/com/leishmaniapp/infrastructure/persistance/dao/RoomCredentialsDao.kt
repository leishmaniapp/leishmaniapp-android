package com.leishmaniapp.infrastructure.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.ShaHash
import com.leishmaniapp.infrastructure.persistance.entities.RoomCredentialsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomCredentialsDao {

    @Upsert
    suspend fun upsertCredentials(credentials: RoomCredentialsEntity?)

    @Delete
    suspend fun deleteCredentials(email: Email)

    @Query("SELECT * FROM Credentials WHERE email = :email AND password = :hash")
    fun credentialsByEmailAndHash(email: Email, hash: ShaHash): Flow<RoomCredentialsEntity?>
}