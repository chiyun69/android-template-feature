package com.example.modules.template_feature.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object TemplateFeatureModule