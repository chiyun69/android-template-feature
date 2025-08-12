package com.example.modules.template_feature.data.localdatasource.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [TemplateFeatureEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TemplateFeatureDatabase : RoomDatabase() {
    
    abstract fun templateFeatureDao(): TemplateFeatureDao
    
    companion object {
        const val DATABASE_NAME = "template_feature_database"
    }
}