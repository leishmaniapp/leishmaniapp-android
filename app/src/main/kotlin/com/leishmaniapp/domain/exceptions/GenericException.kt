package com.leishmaniapp.domain.exceptions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R

data class GenericException(val exception: Throwable) :
    LeishmaniappException(R.string.exception_generic) {
    override val description: String
        @Composable get() = stringResource(id = descriptionResource, exception.toString())
}