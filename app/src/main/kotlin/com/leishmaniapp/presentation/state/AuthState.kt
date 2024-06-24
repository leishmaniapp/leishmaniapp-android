package com.leishmaniapp.presentation.state

import android.os.Parcelable
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.exceptions.LeishmaniappException
import kotlinx.parcelize.Parcelize

/**
 * Current authentication status
 */
@Parcelize
sealed class AuthState : Parcelable {

    /**
     * User is not authenticated
     */
    @Parcelize
    data class None(val connection: AuthConnectionState) :
        AuthState() {

        companion object;

        enum class AuthConnectionState {
            OFFLINE,
            ONLINE
        }
    }

    /**
     * Authentication is in progress
     */
    @Parcelize
    data object Busy : AuthState()

    /**
     * Authentication failed with a [LeishmaniappException]
     */
    @Parcelize
    data class Error(val e: LeishmaniappException) : AuthState()

    /**
     * A [Specialist] was successfully authenticated
     */
    @Parcelize
    data class Authenticated(val s: Specialist) : AuthState()

    /**
     * Get the [AuthState] as [Authenticated] or get null
     */
    fun authenticatedOrNull(): Authenticated? =
        run { if (this is Authenticated) this as Authenticated else null }
}