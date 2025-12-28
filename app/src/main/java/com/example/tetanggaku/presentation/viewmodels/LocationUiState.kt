package com.example.tetanggaku.presentation.viewmodels

data class LocationUiState(
    val currentLocation: String = "San Antonio, TX",
    val suggestedLocations: List<String> = emptyList(),
    val recentLocations: List<String> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
