package com.leishmaniapp.domain.exceptions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leishmaniapp.R
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.superclasses

/**
 * Root cause of exception for an application specific error
 */
sealed class LeishmaniappException(
    /**
     * Error message description
     */
    open val descriptionResource: Int = R.string.exception_root
) : Exception() {

    /**
     * Show the exception tree
     */
    override fun toString(): String =
        this::class.allSuperclasses
            .filter { LeishmaniappException::class.isSuperclassOf(it) }
            .map { it.simpleName!! }
            .fold(this::class.simpleName!! + "\n")
            { acc, ktype -> "$acc\t└ $ktype\n\t" }
            .trim()

    /**
     * Show the error description
     */
    open val description: String
        @Composable get() = stringResource(id = descriptionResource)
}