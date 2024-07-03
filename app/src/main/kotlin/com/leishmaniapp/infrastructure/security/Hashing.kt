package com.leishmaniapp.infrastructure.security

import com.leishmaniapp.domain.types.ShaHash
import java.security.MessageDigest

/**
 * Transform a [String] into a [ShaHash] using SHA3-512
 */
fun String.hash(): ShaHash =
    MessageDigest.getInstance("SHA3-512").digest(toByteArray(Charsets.UTF_8)).toList()