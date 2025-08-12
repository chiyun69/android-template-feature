package com.example.modules.template_feature.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TemplateFeatureResponseDto(
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: String
)