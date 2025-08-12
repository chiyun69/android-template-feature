package com.example.modules.template_feature.api

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateFeatureModuleBuilder @Inject constructor(
    private val apiImpl: TemplateFeatureModuleApi
) {
    
    private var initialized = false
    
    /**
     * Initialize the module
     */
    fun initialize(): TemplateFeatureModuleApi {
        initialized = true
        return apiImpl
    }
    
    /**
     * Get the API instance (automatically initialized)
     */
    fun getApi(): TemplateFeatureModuleApi {
        initialized = true
        return apiImpl
    }
    
    /**
     * Check if the module is initialized
     */
    fun isInitialized(): Boolean = initialized
    
    companion object {
        private var instance: TemplateFeatureModuleBuilder? = null
        
        /**
         * Get the singleton instance of the module builder
         * This should typically be injected via Hilt instead of using this companion object
         */
        fun getInstance(): TemplateFeatureModuleBuilder? = instance
        
        /**
         * Set the singleton instance (used internally by Hilt)
         */
        fun setInstance(builder: TemplateFeatureModuleBuilder) {
            instance = builder
        }
    }
}