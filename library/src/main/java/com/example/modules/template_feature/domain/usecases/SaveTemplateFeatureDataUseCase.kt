package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository

class SaveTemplateFeatureDataUseCase(
    private val repository: TemplateFeatureRepository
) {
    suspend fun createFeature(templateFeature: TemplateFeatureModel): Result<TemplateFeatureModel> {
        return if (validateTemplateFeature(templateFeature)) {
            repository.createTemplateFeature(templateFeature)
        } else {
            Result.failure(IllegalArgumentException("Invalid template feature data"))
        }
    }
    
    suspend fun updateFeature(templateFeature: TemplateFeatureModel): Result<TemplateFeatureModel> {
        return if (templateFeature.id.isNotBlank() && validateTemplateFeature(templateFeature)) {
            repository.updateTemplateFeature(templateFeature)
        } else {
            Result.failure(IllegalArgumentException("Invalid template feature data or missing ID"))
        }
    }
    
    suspend fun deleteFeature(id: String): Result<Unit> {
        return if (id.isNotBlank()) {
            repository.deleteTemplateFeature(id)
        } else {
            Result.failure(IllegalArgumentException("Feature ID cannot be empty"))
        }
    }
    
    private fun validateTemplateFeature(templateFeature: TemplateFeatureModel): Boolean {
        return templateFeature.title.isNotBlank() && 
               templateFeature.description.isNotBlank()
    }
}