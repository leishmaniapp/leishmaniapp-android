package com.leishmaniapp.domain.entities.protobuf

/**
 * Transform an entity to protobuf definition
 */
fun interface ToProto<Out> {
    fun toProto(): Out
}