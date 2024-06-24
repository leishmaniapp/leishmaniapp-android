package com.leishmaniapp.domain.exceptions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R

/***
 * Handle generic network exceptions
 */
class NetworkException(val exception: Exception) :
    LeishmaniappException(R.string.exception_network) {
    override val description: String
        @Composable get() = stringResource(id = descriptionResource, exception.message!!)
}