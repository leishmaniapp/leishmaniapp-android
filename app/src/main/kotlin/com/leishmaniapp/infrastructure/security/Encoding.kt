package com.leishmaniapp.infrastructure.security

import com.leishmaniapp.domain.types.ShaHash
import java.util.Base64

/**
 * Encode a [ShaHash] into a base64 [String]
 */
fun ShaHash.base64(): String = Base64.getEncoder().encodeToString(toByteArray())