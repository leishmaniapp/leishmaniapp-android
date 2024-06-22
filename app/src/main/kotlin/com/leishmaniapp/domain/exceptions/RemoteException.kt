package com.leishmaniapp.domain.exceptions

import com.leishmaniapp.R

/**
 * Exception associated to a remote request
 */
sealed class RemoteException(
    descriptionResource: Int = R.string.exception_remote,
) : LeishmaniappException(descriptionResource)