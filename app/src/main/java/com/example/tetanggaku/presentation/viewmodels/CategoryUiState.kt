package com.example.tetanggaku.presentation.viewmodels

enum class FilterType {
    NONE,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    RATING
}

data class CategoryUiState(
    val category: ServiceCategory? = null,
    val services: List<JobItem> = emptyList(),
    val isLoading: Boolean = false,
    val selectedFilter: FilterType = FilterType.NONE,
    val error: String? = null
)
