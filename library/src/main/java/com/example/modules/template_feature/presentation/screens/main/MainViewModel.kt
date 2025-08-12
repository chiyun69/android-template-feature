package com.example.modules.template_feature.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.usecases.GetTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SyncTemplateFeatureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTemplateFeatureDataUseCase: GetTemplateFeatureDataUseCase,
    private val saveTemplateFeatureDataUseCase: SaveTemplateFeatureDataUseCase,
    private val syncTemplateFeatureUseCase: SyncTemplateFeatureUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        loadTemplateFeatures()
    }

    fun loadTemplateFeatures() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val featuresFlow = if (_state.value.showOnlyActive) {
                    getTemplateFeatureDataUseCase.getActiveFeatures()
                } else {
                    getTemplateFeatureDataUseCase.getAllFeatures()
                }
                
                featuresFlow.catch { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }.collect { features ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        templateFeatures = features,
                        error = null
                    )
                }
            } catch (exception: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            
            syncTemplateFeatureUseCase.syncWithRemote()
                .onSuccess {
                    loadTemplateFeatures()
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        error = exception.message ?: "Sync failed"
                    )
                }
            
            _state.value = _state.value.copy(isRefreshing = false)
        }
    }

    fun searchFeatures(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        
        if (query.isBlank()) {
            loadTemplateFeatures()
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            getTemplateFeatureDataUseCase.searchFeatures(query)
                .onSuccess { features ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        templateFeatures = features,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Search failed"
                    )
                }
        }
    }

    fun toggleShowOnlyActive() {
        _state.value = _state.value.copy(
            showOnlyActive = !_state.value.showOnlyActive
        )
        loadTemplateFeatures()
    }

    fun deleteFeature(id: String) {
        viewModelScope.launch {
            saveTemplateFeatureDataUseCase.deleteFeature(id)
                .onSuccess {
                    // Refresh the list after deletion
                    loadTemplateFeatures()
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        error = exception.message ?: "Delete failed"
                    )
                }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}