package com.example.modules.template_feature.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.presentation.screens.main.components.MainHeader
import com.example.modules.template_feature.presentation.screens.main.components.MainContent
import com.example.modules.template_feature.presentation.sharedcomponents.cards.TemplateFeatureCard
import com.example.modules.template_feature.ui.theme.TemplatefeatureTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Show error snackbar if needed
    state.error?.let { error ->
        LaunchedEffect(error) {
            // You can show a snackbar here or handle error display
            // For now, we'll just clear it after showing
            viewModel.clearError()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MainHeader(
            searchQuery = state.searchQuery,
            showOnlyActive = state.showOnlyActive,
            onSearchQueryChanged = viewModel::searchFeatures,
            onToggleActiveFilter = viewModel::toggleShowOnlyActive,
            onRefresh = viewModel::refreshData,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.fillMaxSize()) {
            MainContent(
                state = state,
                onItemClick = onNavigateToDetail,
                onDeleteItem = viewModel::deleteFeature,
                onRetry = viewModel::loadTemplateFeatures,
                modifier = Modifier.fillMaxSize()
            )
            
            // FAB for creating new features
            FloatingActionButton(
                onClick = onNavigateToCreate,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create new template feature"
                )
            }
        }
    }
}

// Preview version of MainScreen without ViewModel dependency
@Composable
fun MainScreenPreview(
    state: MainState,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToCreate: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onToggleActiveFilter: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onDeleteItem: (String) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MainHeader(
            searchQuery = state.searchQuery,
            showOnlyActive = state.showOnlyActive,
            onSearchQueryChanged = onSearchQueryChanged,
            onToggleActiveFilter = onToggleActiveFilter,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.fillMaxSize()) {
            MainContent(
                state = state,
                onItemClick = onNavigateToDetail,
                onDeleteItem = onDeleteItem,
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize()
            )
            
            FloatingActionButton(
                onClick = onNavigateToCreate,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create new template feature"
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "MainScreen - Loading")
@Composable
fun MainScreenLoadingPreview() {
    TemplatefeatureTheme {
        MainScreenPreview(
            state = MainState(
                isLoading = true
            )
        )
    }
}

@Preview(showBackground = true, name = "MainScreen - With Data")
@Composable
fun MainScreenWithDataPreview() {
    TemplatefeatureTheme {
        MainScreenPreview(
            state = MainState(
                showOnlyActive = true
            )
        )
    }
}

@Preview(showBackground = true, name = "MainScreen - Empty State")
@Composable
fun MainScreenEmptyPreview() {
    TemplatefeatureTheme {
        MainScreenPreview(
            state = MainState(
                templateFeatures = emptyList()
            )
        )
    }
}

@Preview(showBackground = true, name = "MainScreen - Error State")
@Composable
fun MainScreenErrorPreview() {
    TemplatefeatureTheme {
        MainScreenPreview(
            state = MainState(
                error = "Network connection failed. Please check your internet connection and try again."
            )
        )
    }
}

@Preview(showBackground = true, name = "MainScreen - Search Results")
@Composable
fun MainScreenSearchPreview() {
    TemplatefeatureTheme {
        MainScreenPreview(
            state = MainState(
                templateFeatures = listOf(
                    TemplateFeatureModel(
                        id = "1",
                        title = "User Authentication",
                        description = "Complete user authentication system with login, registration, and password recovery features.",
                        isActive = true,
                        createdAt = "2024-01-15"
                    )
                ),
                searchQuery = "auth",
                showOnlyActive = false
            )
        )
    }
}

@Preview(showBackground = true, name = "MainScreen - Refreshing")
@Composable
fun MainScreenRefreshingPreview() {
    TemplatefeatureTheme {
        MainScreenPreview(
            state = MainState(
                templateFeatures = listOf(
                    TemplateFeatureModel(
                        id = "1",
                        title = "User Authentication",
                        description = "Complete user authentication system with login, registration, and password recovery features.",
                        isActive = true,
                        createdAt = "2024-01-15"
                    ),
                    TemplateFeatureModel(
                        id = "2",
                        title = "Data Analytics Dashboard",
                        description = "Comprehensive analytics dashboard with charts, graphs, and real-time data visualization.",
                        isActive = true,
                        createdAt = "2024-01-10"
                    )
                ),
                isRefreshing = true
            )
        )
    }
}