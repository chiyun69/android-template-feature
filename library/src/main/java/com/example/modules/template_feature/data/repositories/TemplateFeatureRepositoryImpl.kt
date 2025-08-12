package com.example.modules.template_feature.data.repositories

import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDao
import com.example.modules.template_feature.data.localdatasource.preferences.TemplateFeaturePreferences
import com.example.modules.template_feature.data.mappers.toDomainModel
import com.example.modules.template_feature.data.mappers.toDomainModelListFromEntity
import com.example.modules.template_feature.data.mappers.toDomainModelListFromDto
import com.example.modules.template_feature.data.mappers.toEntity
import com.example.modules.template_feature.data.mappers.toEntityListFromDto
import com.example.modules.template_feature.data.mappers.toRequestDto
import com.example.modules.template_feature.data.remotedatasource.api.TemplateFeatureApiService
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TemplateFeatureRepositoryImpl(
    private val apiService: TemplateFeatureApiService,
    private val dao: TemplateFeatureDao,
    private val preferences: TemplateFeaturePreferences
) : TemplateFeatureRepository {

    override suspend fun getAllTemplateFeatures(): Flow<List<TemplateFeatureModel>> {
        return dao.getAllTemplateFeatures().map { entities ->
            entities.toDomainModelListFromEntity()
        }
    }

    override suspend fun getTemplateFeatureById(id: String): TemplateFeatureModel? {
        return dao.getTemplateFeatureById(id)?.toDomainModel()
    }

    override suspend fun getActiveTemplateFeatures(): Flow<List<TemplateFeatureModel>> {
        return dao.getActiveTemplateFeatures().map { entities ->
            entities.toDomainModelListFromEntity()
        }
    }

    override suspend fun createTemplateFeature(templateFeature: TemplateFeatureModel): Result<TemplateFeatureModel> {
        return try {
            val requestDto = templateFeature.toRequestDto()
            val responseDto = apiService.createTemplateFeature(requestDto)
            val domainModel = responseDto.toDomainModel()
            
            // Save to local database
            dao.insertTemplateFeature(responseDto.toEntity())
            
            Result.success(domainModel)
        } catch (e: Exception) {
            // If remote fails, try to save locally with generated ID
            try {
                val localEntity = templateFeature.copy(
                    id = if (templateFeature.id.isEmpty()) generateLocalId() else templateFeature.id
                ).toEntity()
                dao.insertTemplateFeature(localEntity)
                Result.success(localEntity.toDomainModel())
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }

    override suspend fun updateTemplateFeature(templateFeature: TemplateFeatureModel): Result<TemplateFeatureModel> {
        return try {
            val requestDto = templateFeature.toRequestDto()
            val responseDto = apiService.updateTemplateFeature(templateFeature.id, requestDto)
            val domainModel = responseDto.toDomainModel()
            
            // Update local database
            dao.updateTemplateFeature(responseDto.toEntity())
            
            Result.success(domainModel)
        } catch (e: Exception) {
            // If remote fails, try to update locally
            try {
                dao.updateTemplateFeature(templateFeature.toEntity())
                Result.success(templateFeature)
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }

    override suspend fun deleteTemplateFeature(id: String): Result<Unit> {
        return try {
            apiService.deleteTemplateFeature(id)
            dao.deleteTemplateFeatureById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            // If remote fails, try to delete locally
            try {
                dao.deleteTemplateFeatureById(id)
                Result.success(Unit)
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }

    override suspend fun syncWithRemote(): Result<Unit> {
        return try {
            val remoteFeatures = apiService.getAllTemplateFeatures()
            val entities = remoteFeatures.toEntityListFromDto()
            
            // Clear and insert all remote data
            dao.deleteAllTemplateFeatures()
            dao.insertTemplateFeatures(entities)
            
            preferences.setLastSyncTime(System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchTemplateFeatures(query: String): Result<List<TemplateFeatureModel>> {
        return try {
            val remoteResults = apiService.searchTemplateFeatures(query)
            Result.success(remoteResults.toDomainModelListFromDto())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateLocalId(): String {
        return "local_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}