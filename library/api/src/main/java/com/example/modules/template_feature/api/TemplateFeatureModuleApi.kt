package com.example.modules.template_feature.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface TemplateFeatureModuleApi {
    
    /**
     * Get the main navigation composable for the template feature module
     * This provides a complete navigation experience with screens for listing,
     * creating, and editing template features.
     */
    @Composable
    fun TemplateFeatureNavigation(navController: NavHostController? = null)

    /**
     * Check if the module is initialized and ready to use
     */
    fun isInitialized(): Boolean
}