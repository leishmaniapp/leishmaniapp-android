package com.leishmaniapp.domain.exceptions

import com.leishmaniapp.cloud.types.StatusCode

/**
 * Failed analysis exception
 */
class BadAnalysisException: ProcedureExceptionWithStatusCode(StatusCode.UNPROCESSABLE_CONTENT)