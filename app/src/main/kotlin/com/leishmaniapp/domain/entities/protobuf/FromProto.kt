package com.leishmaniapp.domain.entities.protobuf

/**
 * Transform into application entity
 */
fun interface FromProto<ApplicationEntity, ProtoEntity> {
    fun fromProto(from: ProtoEntity): ApplicationEntity
}