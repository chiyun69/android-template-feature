package com.example.modules.template_feature.data.mappers

import com.example.modules.template_feature.data.dto.TemplateFeatureRequestDto
import com.example.modules.template_feature.data.dto.TemplateFeatureResponseDto
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureEntity
import com.example.modules.template_feature.domain.models.TemplateFeatureModel

// DTO to Domain Model
fun TemplateFeatureResponseDto.toDomainModel(): TemplateFeatureModel {
    return TemplateFeatureModel(
        id = this.id,
        title = this.title,
        description = this.description,
        isActive = this.isActive,
        createdAt = this.createdAt
    )
}

fun List<TemplateFeatureResponseDto>.toDomainModelListFromDto(): List<TemplateFeatureModel> {
    return this.map { it.toDomainModel() }
}

// Domain Model to Request DTO
fun TemplateFeatureModel.toRequestDto(): TemplateFeatureRequestDto {
    return TemplateFeatureRequestDto(
        title = this.title,
        description = this.description,
        isActive = this.isActive
    )
}

// Entity to Domain Model
fun TemplateFeatureEntity.toDomainModel(): TemplateFeatureModel {
    return TemplateFeatureModel(
        id = this.id,
        title = this.title,
        description = this.description,
        isActive = this.isActive,
        createdAt = this.createdAt
    )
}

fun List<TemplateFeatureEntity>.toDomainModelListFromEntity(): List<TemplateFeatureModel> {
    return this.map { it.toDomainModel() }
}

// Domain Model to Entity
fun TemplateFeatureModel.toEntity(): TemplateFeatureEntity {
    return TemplateFeatureEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        isActive = this.isActive,
        createdAt = this.createdAt
    )
}

fun List<TemplateFeatureModel>.toEntityListFromModel(): List<TemplateFeatureEntity> {
    return this.map { it.toEntity() }
}

// Response DTO to Entity
fun TemplateFeatureResponseDto.toEntity(): TemplateFeatureEntity {
    return TemplateFeatureEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        isActive = this.isActive,
        createdAt = this.createdAt
    )
}

fun List<TemplateFeatureResponseDto>.toEntityListFromDto(): List<TemplateFeatureEntity> {
    return this.map { it.toEntity() }
}