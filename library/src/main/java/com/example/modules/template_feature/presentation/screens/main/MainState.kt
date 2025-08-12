package com.example.modules.template_feature.presentation.screens.main

import com.example.modules.template_feature.domain.models.TemplateFeatureModel

data class MainState(
    val isLoading: Boolean = false,
    val templateFeatures: List<TemplateFeatureModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val showOnlyActive: Boolean = true
) {
    val hasData: Boolean get() = templateFeatures.isNotEmpty()
    val hasError: Boolean get() = error != null
    val isEmpty: Boolean get() = !isLoading && !hasError && templateFeatures.isEmpty()
}