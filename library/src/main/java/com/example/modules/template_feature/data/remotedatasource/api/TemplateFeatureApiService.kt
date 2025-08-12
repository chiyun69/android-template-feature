package com.example.modules.template_feature.data.remotedatasource.api

import com.example.modules.template_feature.data.dto.TemplateFeatureRequestDto
import com.example.modules.template_feature.data.dto.TemplateFeatureResponseDto
import retrofit2.http.*

interface TemplateFeatureApiService {
    
    @GET("template-features")
    suspend fun getAllTemplateFeatures(): List<TemplateFeatureResponseDto>
    
    @GET("template-features/{id}")
    suspend fun getTemplateFeatureById(@Path("id") id: String): TemplateFeatureResponseDto
    
    @GET("template-features")
    suspend fun getActiveTemplateFeatures(
        @Query("active") isActive: Boolean = true
    ): List<TemplateFeatureResponseDto>
    
    @POST("template-features")
    suspend fun createTemplateFeature(
        @Body request: TemplateFeatureRequestDto
    ): TemplateFeatureResponseDto
    
    @PUT("template-features/{id}")
    suspend fun updateTemplateFeature(
        @Path("id") id: String,
        @Body request: TemplateFeatureRequestDto
    ): TemplateFeatureResponseDto
    
    @DELETE("template-features/{id}")
    suspend fun deleteTemplateFeature(@Path("id") id: String)
    
    @GET("template-features/search")
    suspend fun searchTemplateFeatures(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): List<TemplateFeatureResponseDto>
}