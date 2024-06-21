package com.leishmaniapp.domain.entities.protobuf

/**
 * Transform entities from and into protobuf definitions
 */
interface ProtobufCompatibleEntity<ApplicationEntity, ProtoEntity> : ToProto<ProtoEntity>,
    FromProto<ApplicationEntity, ProtoEntity>