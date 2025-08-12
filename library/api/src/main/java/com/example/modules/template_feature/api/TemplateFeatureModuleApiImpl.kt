package com.example.modules.template_feature.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modules.template_feature.api.navigation.TemplateFeatureNavigation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateFeatureModuleApiImpl @Inject constructor() : TemplateFeatureModuleApi {

    @Composable
    override fun TemplateFeatureNavigation(navController: NavHostController?) {
        TemplateFeatureNavigation(navController = navController ?: rememberNavController())
    }

    override fun isInitialized(): Boolean {
        return true // Module is always ready when dependency injected
    }
}