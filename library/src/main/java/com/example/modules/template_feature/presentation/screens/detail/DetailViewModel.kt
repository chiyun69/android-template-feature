package com.example.modules.template_feature.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.usecases.GetTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getTemplateFeatureDataUseCase: GetTemplateFeatureDataUseCase,
    private val saveTemplateFeatureDataUseCase: SaveTemplateFeatureDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun loadTemplateFeature(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val templateFeature = getTemplateFeatureDataUseCase.getFeatureById(id)
                _state.value = _state.value.copy(
                    isLoading = false,
                    templateFeature = templateFeature,
                    error = if (templateFeature == null) "Template feature not found" else null
                )
            } catch (exception: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun startEditing() {
        _state.value = _state.value.copy(isEditing = true)
    }

    fun cancelEditing() {
        _state.value = _state.value.copy(isEditing = false)
    }

    fun updateTitle(title: String) {
        _state.value.templateFeature?.let { feature ->
            _state.value = _state.value.copy(
                templateFeature = feature.copy(title = title)
            )
        }
    }

    fun updateDescription(description: String) {
        _state.value.templateFeature?.let { feature ->
            _state.value = _state.value.copy(
                templateFeature = feature.copy(description = description)
            )
        }
    }

    fun updateActiveStatus(isActive: Boolean) {
        _state.value.templateFeature?.let { feature ->
            _state.value = _state.value.copy(
                templateFeature = feature.copy(isActive = isActive)
            )
        }
    }

    fun saveChanges() {
        _state.value.templateFeature?.let { feature ->
            viewModelScope.launch {
                _state.value = _state.value.copy(isSaving = true, error = null)
                
                val result = if (feature.id.isEmpty()) {
                    saveTemplateFeatureDataUseCase.createFeature(feature)
                } else {
                    saveTemplateFeatureDataUseCase.updateFeature(feature)
                }
                
                result.onSuccess { updatedFeature ->
                    _state.value = _state.value.copy(
                        isSaving = false,
                        templateFeature = updatedFeature,
                        isEditing = false,
                        error = null
                    )
                }.onFailure { exception ->
                    _state.value = _state.value.copy(
                        isSaving = false,
                        error = exception.message ?: "Save failed"
                    )
                }
            }
        }
    }

    fun deleteFeature() {
        _state.value.templateFeature?.let { feature ->
            if (feature.id.isNotEmpty()) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isDeleting = true, error = null)
                    
                    saveTemplateFeatureDataUseCase.deleteFeature(feature.id)
                        .onSuccess {
                            // Navigation back should be handled by the UI
                            _state.value = _state.value.copy(
                                isDeleting = false,
                                templateFeature = null
                            )
                        }
                        .onFailure { exception ->
                            _state.value = _state.value.copy(
                                isDeleting = false,
                                error = exception.message ?: "Delete failed"
                            )
                        }
                }
            }
        }
    }

    fun createNewFeature() {
        _state.value = _state.value.copy(
            templateFeature = TemplateFeatureModel.empty(),
            isEditing = true
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}