package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Entity
@Serializable
data class Specialist(
    val name: String,
    @PrimaryKey val username: Username,
    @Transient val password: Password? = null
)
