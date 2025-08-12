package com.example.modules.template_feature.api.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modules.template_feature.presentation.navigation.TemplateFeatureNavigation as LibraryTemplateFeatureNavigation

/**
 * Public API entry point for Template Feature navigation.
 * Delegates to the internal library implementation to provide full functionality.
 */
@Composable
fun TemplateFeatureNavigation(
    navController: NavHostController = rememberNavController()
) {
    LibraryTemplateFeatureNavigation(navController = navController)
}