package com.leishmaniapp.domain.parceler

import android.os.Parcel
import com.leishmaniapp.utilities.time.fromUnixToLocalDateTime
import com.leishmaniapp.utilities.time.toUnixTime
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parceler

/**
 * Parcelize [LocalDateTime] into Unix time
 */
object LocalDateTimeParceler : Parceler<LocalDateTime> {
    override fun create(parcel: Parcel): LocalDateTime =
        parcel.readLong().fromUnixToLocalDateTime()

    override fun LocalDateTime.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(toUnixTime())
    }
}