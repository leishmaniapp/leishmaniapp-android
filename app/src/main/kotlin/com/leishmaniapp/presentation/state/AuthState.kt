package com.leishmaniapp.presentation.state

import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.exceptions.LeishmaniappException

/**
 * Current authentication status
 */
sealed class AuthState {

    /**
     * User is not authenticated
     */
    data object None : AuthState()

    /**
     * Authentication is in progress
     */
    data object Busy: AuthState()

    /**
     * Authentication failed with a [LeishmaniappException]
     */
    data class Error(val e: LeishmaniappException) : AuthState()

    /**
     * A [Specialist] was successfully authenticated
     */
    data class Authenticated(val s: Specialist): AuthState()
}