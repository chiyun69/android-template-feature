package com.example.modules.template_feature.domain.repositories

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import kotlinx.coroutines.flow.Flow

interface TemplateFeatureRepository {
    
    suspend fun getAllTemplateFeatures(): Flow<List<TemplateFeatureModel>>
    
    suspend fun getTemplateFeatureById(id: String): TemplateFeatureModel?
    
    suspend fun getActiveTemplateFeatures(): Flow<List<TemplateFeatureModel>>
    
    suspend fun createTemplateFeature(templateFeature: TemplateFeatureModel): Result<TemplateFeatureModel>
    
    suspend fun updateTemplateFeature(templateFeature: TemplateFeatureModel): Result<TemplateFeatureModel>
    
    suspend fun deleteTemplateFeature(id: String): Result<Unit>
    
    suspend fun syncWithRemote(): Result<Unit>
    
    suspend fun searchTemplateFeatures(query: String): Result<List<TemplateFeatureModel>>
}