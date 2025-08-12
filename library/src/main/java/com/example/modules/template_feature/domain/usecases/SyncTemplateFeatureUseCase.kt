package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository

class SyncTemplateFeatureUseCase(
    private val repository: TemplateFeatureRepository
) {
    suspend fun syncWithRemote(): Result<Unit> {
        return repository.syncWithRemote()
    }
}