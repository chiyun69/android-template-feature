package com.example.modules.template_feature.api.di

import com.example.modules.template_feature.api.TemplateFeatureModuleApi
import com.example.modules.template_feature.api.TemplateFeatureModuleApiImpl
import com.example.modules.template_feature.di.TemplateFeatureModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        TemplateFeatureModule::class
    ]
)
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    @Singleton
    abstract fun bindTemplateFeatureModuleApi(
        implementation: TemplateFeatureModuleApiImpl
    ): TemplateFeatureModuleApi
}