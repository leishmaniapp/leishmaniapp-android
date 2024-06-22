package com.leishmaniapp.utilities.extensions

import com.leishmaniapp.cloud.types.StatusCode
import com.leishmaniapp.domain.exceptions.BadAuthenticationException
import com.leishmaniapp.domain.exceptions.ProcedureExceptionWithStatusCode

/**
 * Return a [ProcedureExceptionWithStatusCode] or a corresponding [com.leishmaniapp.domain.exceptions.LeishmaniappException]
 * when an error code is detected, run [f] otherwise
 */
fun <T> StatusCode.throwOrElse(f: () -> T): T =
    when (this) {
        StatusCode.OK -> f.invoke()
        StatusCode.FORBIDDEN,
        StatusCode.INVALID_TOKEN,
        StatusCode.UNAUTHENTICATED -> throw BadAuthenticationException()

        else -> throw ProcedureExceptionWithStatusCode(this)
    }
