package com.leishmaniapp.utilities.extensions

import com.leishmaniapp.cloud.types.StatusCode
import com.leishmaniapp.domain.exceptions.BadAnalysisException
import com.leishmaniapp.domain.exceptions.BadAuthenticationException
import com.leishmaniapp.domain.exceptions.ProcedureExceptionWithStatusCode

/**
 * Return a [ProcedureExceptionWithStatusCode] or a corresponding [com.leishmaniapp.domain.exceptions.LeishmaniappException]
 * when an error code is detected [Result.failure] is returned, [Result.success] with the value of [f] returned otherwise
 */
fun <T> StatusCode.asResult(f: () -> T): Result<T> =
    when (this) {
        StatusCode.OK -> Result.success(f.invoke())

        StatusCode.FORBIDDEN,
        StatusCode.INVALID_TOKEN,
        StatusCode.UNAUTHENTICATED -> Result.failure(BadAuthenticationException())

        StatusCode.UNPROCESSABLE_CONTENT -> Result.failure(BadAnalysisException())

        else -> Result.failure(ProcedureExceptionWithStatusCode(this))
    }

/**
 * Run [asResult] and throw on [Result.failure]
 */
fun <T> StatusCode.getOrThrow(f: () -> T): T =
    this.asResult(f).getOrThrow()
