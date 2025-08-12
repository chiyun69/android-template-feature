package com.example.modules.template_feature.di

import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import com.example.modules.template_feature.domain.usecases.GetTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SyncTemplateFeatureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    fun provideGetTemplateFeatureDataUseCase(
        repository: TemplateFeatureRepository
    ): GetTemplateFeatureDataUseCase {
        return GetTemplateFeatureDataUseCase(repository)
    }
    
    @Provides
    fun provideSaveTemplateFeatureDataUseCase(
        repository: TemplateFeatureRepository
    ): SaveTemplateFeatureDataUseCase {
        return SaveTemplateFeatureDataUseCase(repository)
    }
    
    @Provides
    fun provideSyncTemplateFeatureUseCase(
        repository: TemplateFeatureRepository
    ): SyncTemplateFeatureUseCase {
        return SyncTemplateFeatureUseCase(repository)
    }
}