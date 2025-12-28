package com.example.tetanggaku.presentation.viewmodels

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<JobItem> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)
