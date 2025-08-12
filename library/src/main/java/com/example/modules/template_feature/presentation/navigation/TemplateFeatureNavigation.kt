package com.example.modules.template_feature.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modules.template_feature.presentation.screens.detail.DetailScreen
import com.example.modules.template_feature.presentation.screens.main.MainScreen

object TemplateFeatureDestinations {
    const val MAIN_ROUTE = "template_feature_main"
    const val DETAIL_ROUTE = "template_feature_detail"
    const val CREATE_ROUTE = "template_feature_create"
    
    fun detailRoute(featureId: String) = "$DETAIL_ROUTE/$featureId"
}

@Composable
fun TemplateFeatureNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = TemplateFeatureDestinations.MAIN_ROUTE
    ) {
        composable(TemplateFeatureDestinations.MAIN_ROUTE) {
            MainScreen(
                onNavigateToDetail = { featureId ->
                    navController.navigate(TemplateFeatureDestinations.detailRoute(featureId))
                },
                onNavigateToCreate = {
                    navController.navigate(TemplateFeatureDestinations.CREATE_ROUTE)
                }
            )
        }
        
        composable("${TemplateFeatureDestinations.DETAIL_ROUTE}/{featureId}") { backStackEntry ->
            val featureId = backStackEntry.arguments?.getString("featureId")
            DetailScreen(
                featureId = featureId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(TemplateFeatureDestinations.CREATE_ROUTE) {
            DetailScreen(
                featureId = null, // This will trigger create mode
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}