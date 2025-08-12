package com.example.modules.template_feature.presentation.screens.detail

import com.example.modules.template_feature.domain.models.TemplateFeatureModel

data class DetailState(
    val isLoading: Boolean = false,
    val templateFeature: TemplateFeatureModel? = null,
    val error: String? = null,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false
) {
    val hasData: Boolean get() = templateFeature != null
    val hasError: Boolean get() = error != null
    val canEdit: Boolean get() = hasData && !isLoading && !isSaving
    val canSave: Boolean get() = isEditing && !isSaving && hasValidData
    
    private val hasValidData: Boolean get() = 
        templateFeature?.title?.isNotBlank() == true &&
        templateFeature.description.isNotBlank()
}