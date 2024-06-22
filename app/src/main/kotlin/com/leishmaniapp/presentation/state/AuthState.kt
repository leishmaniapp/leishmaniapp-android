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
    data object None : AuthState()

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
}