package com.example.modules.template_feature.presentation.navigation

import androidx.navigation.NavController

class TemplateFeatureNavigator(
    private val navController: NavController
) {
    fun navigateToMain() {
        navController.navigate(TemplateFeatureDestinations.MAIN_ROUTE) {
            popUpTo(TemplateFeatureDestinations.MAIN_ROUTE) {
                inclusive = true
            }
        }
    }
    
    fun navigateToDetail(featureId: String) {
        navController.navigate(TemplateFeatureDestinations.detailRoute(featureId))
    }
    
    fun navigateToCreate() {
        navController.navigate(TemplateFeatureDestinations.CREATE_ROUTE)
    }
    
    fun navigateBack() {
        navController.popBackStack()
    }
    
    fun canNavigateBack(): Boolean {
        return navController.previousBackStackEntry != null
    }
}