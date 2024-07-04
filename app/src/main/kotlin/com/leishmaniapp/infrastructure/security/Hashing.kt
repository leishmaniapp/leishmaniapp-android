package com.leishmaniapp.infrastructure.security

import com.leishmaniapp.BuildConfig
import com.leishmaniapp.domain.types.ShaHash
import java.security.MessageDigest

/**
 * Which hashing algorithm will be used
 */
const val HASHING_ALGORITHM: String = BuildConfig.HASH_ALGORITHM

/**
 * Transform a [String] into a [ShaHash] using SHA3-512
 */
fun String.hash(): ShaHash =
    MessageDigest.getInstance(HASHING_ALGORITHM).digest(toByteArray(Charsets.UTF_8)).toList()