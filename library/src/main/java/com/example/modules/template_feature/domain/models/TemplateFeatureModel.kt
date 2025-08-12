package com.example.modules.template_feature.domain.models

data class TemplateFeatureModel(
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: String
) {
    companion object {
        fun empty() = TemplateFeatureModel(
            id = "",
            title = "",
            description = "",
            isActive = false,
            createdAt = ""
        )
    }
}