package com.leishmaniapp.domain.entities

import android.os.Parcelable
import com.leishmaniapp.domain.types.AccessToken
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.ShaHash
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Store credentials for internal use
 */
@Parcelize
@Serializable
data class Credentials(

    /**
     * Email associated to the credentials
     */
    val email: Email,

    /**
     * Remote server authentication token
     */
    val token: AccessToken,

    /**
     * Hashed password for remembering session
     */
    val password: ShaHash,

    ) : Parcelable
