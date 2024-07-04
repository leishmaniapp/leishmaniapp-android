package com.leishmaniapp.infrastructure.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leishmaniapp.domain.entities.Credentials
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.ShaHash

/**
 * Database representation of [Credentials]
 */
@Entity(tableName = "Credentials")
data class RoomCredentialsEntity(
    @PrimaryKey val email: Email,
    @ColumnInfo val token: AccessToken = "",
    @ColumnInfo val password: ShaHash = listOf(),
) {
    constructor(credentials: Credentials) : this(
        email = credentials.email,
        token = credentials.token,
        password = credentials.password,
    )

    fun toCredentials(): Credentials = Credentials(
        email = email,
        token = token,
        password = password,
    )
}