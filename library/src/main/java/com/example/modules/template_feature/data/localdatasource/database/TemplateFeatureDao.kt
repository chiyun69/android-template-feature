package com.example.modules.template_feature.data.localdatasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateFeatureDao {
    
    @Query("SELECT * FROM template_features ORDER BY lastUpdated DESC")
    fun getAllTemplateFeatures(): Flow<List<TemplateFeatureEntity>>
    
    @Query("SELECT * FROM template_features WHERE id = :id")
    suspend fun getTemplateFeatureById(id: String): TemplateFeatureEntity?
    
    @Query("SELECT * FROM template_features WHERE isActive = :isActive ORDER BY lastUpdated DESC")
    fun getActiveTemplateFeatures(isActive: Boolean = true): Flow<List<TemplateFeatureEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateFeature(templateFeature: TemplateFeatureEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateFeatures(templateFeatures: List<TemplateFeatureEntity>)
    
    @Update
    suspend fun updateTemplateFeature(templateFeature: TemplateFeatureEntity)
    
    @Delete
    suspend fun deleteTemplateFeature(templateFeature: TemplateFeatureEntity)
    
    @Query("DELETE FROM template_features WHERE id = :id")
    suspend fun deleteTemplateFeatureById(id: String)
    
    @Query("DELETE FROM template_features")
    suspend fun deleteAllTemplateFeatures()
}