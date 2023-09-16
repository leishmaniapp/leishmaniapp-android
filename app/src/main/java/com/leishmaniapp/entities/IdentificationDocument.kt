package com.leishmaniapp.entities

import kotlinx.serialization.Serializable
import java.util.Locale

class IdentificationDocument(value: String) {
    @Serializable val value: String = value.uppercase(Locale.ROOT)
}