package com.leishmaniapp.presentation.viewmodel

/**
 * Allow complete state restart on ViewModels
 */
fun interface DismissableState {

    /**
     * Restart the state, reset all variables
     */
    fun dismiss()

}