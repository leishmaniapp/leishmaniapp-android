package com.leishmaniapp.infrastructure.security

import com.leishmaniapp.domain.types.ShaHash
import java.util.Base64

/**
 * Encode a [ShaHash] into a base64 [String]
 */
fun ShaHash.encodeBase64(): String = Base64.getEncoder().encodeToString(toByteArray())

/**
 * Decode a [ShaHash] from a base64 [String]
 */
fun String.decodeBase64(): ShaHash = Base64.getDecoder().decode(this).toList()