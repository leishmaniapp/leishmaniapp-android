package com.leishmaniapp.domain.exceptions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import com.leishmaniapp.cloud.types.StatusCode

/**
 * RPC call exception with [StatusCode], not a network failure, but a request error
 */
open class ProcedureExceptionWithStatusCode(val status: StatusCode) :
    RemoteException(R.string.exception_remote_status) {
    override val description: String
        @Composable get() = stringResource(id = descriptionResource, status.toString())
}