package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import kotlinx.coroutines.flow.Flow

class GetTemplateFeatureDataUseCase(
    private val repository: TemplateFeatureRepository
) {
    suspend fun getAllFeatures(): Flow<List<TemplateFeatureModel>> {
        return repository.getAllTemplateFeatures()
    }
    
    suspend fun getActiveFeatures(): Flow<List<TemplateFeatureModel>> {
        return repository.getActiveTemplateFeatures()
    }
    
    suspend fun getFeatureById(id: String): TemplateFeatureModel? {
        return repository.getTemplateFeatureById(id)
    }
    
    suspend fun searchFeatures(query: String): Result<List<TemplateFeatureModel>> {
        return if (query.isBlank()) {
            Result.failure(IllegalArgumentException("Search query cannot be empty"))
        } else {
            repository.searchTemplateFeatures(query.trim())
        }
    }
}