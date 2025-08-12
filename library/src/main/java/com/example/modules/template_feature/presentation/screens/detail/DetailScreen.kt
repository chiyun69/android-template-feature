package com.example.modules.template_feature.presentation.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    featureId: String?,
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(featureId) {
        if (featureId != null && featureId.isNotEmpty()) {
            viewModel.loadTemplateFeature(featureId)
        } else {
            viewModel.createNewFeature()
        }
    }
    
    // Handle successful deletion
    LaunchedEffect(state.templateFeature) {
        if (state.templateFeature == null && !state.isLoading && !state.hasError) {
            //onNavigateBack()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with title
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
                
                if (state.hasData && !state.isEditing) {
                    Row {
                        IconButton(
                            onClick = viewModel::startEditing,
                            enabled = state.canEdit
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit feature"
                            )
                        }
                        
                        IconButton(
                            onClick = viewModel::deleteFeature,
                            enabled = state.canEdit && !state.isDeleting
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete feature",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            
            // Screen title
            if (state.hasData || state.isLoading) {
                val title = when {
                    state.isLoading -> "Loading..."
                    state.templateFeature?.id?.isEmpty() == true -> "Create New Feature"
                    state.isEditing -> "Edit Feature"
                    else -> state.templateFeature?.title ?: "Feature Details"
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            state.hasError -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error ?: "Unknown error",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Go Back")
                    }
                }
            }
            
            state.hasData -> {
                DetailContent(
                    state = state,
                    onTitleChanged = viewModel::updateTitle,
                    onDescriptionChanged = viewModel::updateDescription,
                    onActiveStatusChanged = viewModel::updateActiveStatus,
                    onSave = viewModel::saveChanges,
                    onCancelEdit = viewModel::cancelEditing,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
    
    // Handle loading states
    if (state.isSaving || state.isDeleting) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DetailContent(
    state: DetailState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onActiveStatusChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancelEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val feature = state.templateFeature ?: return
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.isEditing) {
            // Edit mode
            OutlinedTextField(
                value = feature.title,
                onValueChange = onTitleChanged,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving
            )
            
            OutlinedTextField(
                value = feature.description,
                onValueChange = onDescriptionChanged,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                enabled = !state.isSaving
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Active")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = feature.isActive,
                    onCheckedChange = onActiveStatusChanged,
                    enabled = !state.isSaving
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onCancelEdit,
                    enabled = !state.isSaving
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSave,
                    enabled = state.canSave
                ) {
                    Text("Save")
                }
            }
        } else {
            // Display mode
            Text(
                text = feature.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (feature.isActive) "Active" else "Inactive",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (feature.isActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
            
            if (feature.createdAt.isNotEmpty()) {
                Text(
                    text = "Created: ${feature.createdAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}