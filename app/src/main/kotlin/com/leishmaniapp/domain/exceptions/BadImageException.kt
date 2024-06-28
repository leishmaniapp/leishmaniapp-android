package com.leishmaniapp.domain.exceptions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R

/**
 * Bad picture take exception
 */
data class BadImageException(val exception: Throwable) :
    LeishmaniappException(R.string.exception_image) {
    override val description: String
        @Composable get() = stringResource(id = descriptionResource, exception.toString())
}