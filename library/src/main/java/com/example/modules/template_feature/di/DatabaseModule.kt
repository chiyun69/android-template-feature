package com.example.modules.template_feature.di

import android.content.Context
import androidx.room.Room
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDatabase
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDao
import com.example.modules.template_feature.data.localdatasource.preferences.TemplateFeaturePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TemplateFeatureDatabase {
        return Room.databaseBuilder(
            context,
            TemplateFeatureDatabase::class.java,
            TemplateFeatureDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideDao(database: TemplateFeatureDatabase): TemplateFeatureDao {
        return database.templateFeatureDao()
    }
    
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): TemplateFeaturePreferences {
        return TemplateFeaturePreferences(context)
    }
}