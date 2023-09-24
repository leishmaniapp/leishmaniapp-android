package com.leishmaniapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Specialist representation
 * Algebraic product type with Username and Password which are inlined
 */
@Entity
data class SpecialistRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val username: String,
    @Transient val password: String? = null
)

@Serializable
data class Specialist(
    val name: String,
    val username: Username,
    @Transient val password: Password? = null
)
