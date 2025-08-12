package com.example.modules.template_feature.di

import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDao
import com.example.modules.template_feature.data.localdatasource.preferences.TemplateFeaturePreferences
import com.example.modules.template_feature.data.remotedatasource.api.TemplateFeatureApiService
import com.example.modules.template_feature.data.repositories.TemplateFeatureRepositoryImpl
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideTemplateFeatureRepository(
        apiService: TemplateFeatureApiService,
        dao: TemplateFeatureDao,
        preferences: TemplateFeaturePreferences
    ): TemplateFeatureRepository {
        return TemplateFeatureRepositoryImpl(
            apiService = apiService,
            dao = dao,
            preferences = preferences
        )
    }
}