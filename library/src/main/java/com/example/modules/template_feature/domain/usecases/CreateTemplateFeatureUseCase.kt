package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import javax.inject.Inject

class CreateTemplateFeatureUseCase @Inject constructor(
    private val repository: TemplateFeatureRepository
) {
    suspend operator fun invoke(feature: TemplateFeatureModel): Result<TemplateFeatureModel> {
        return repository.createTemplateFeature(feature)
    }
}