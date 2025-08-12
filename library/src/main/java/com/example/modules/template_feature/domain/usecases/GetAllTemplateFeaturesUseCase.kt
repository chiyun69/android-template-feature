package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTemplateFeaturesUseCase @Inject constructor(
    private val repository: TemplateFeatureRepository
) {
    suspend operator fun invoke(): Flow<List<TemplateFeatureModel>> {
        return repository.getAllTemplateFeatures()
    }
}