package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetActiveTemplateFeaturesUseCase @Inject constructor(
    private val repository: TemplateFeatureRepository
) {
    suspend operator fun invoke(): Flow<List<TemplateFeatureModel>> {
        return repository.getAllTemplateFeatures().map { features ->
            features.filter { it.isActive }
        }
    }
}