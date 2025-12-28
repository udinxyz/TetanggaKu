package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class LocationViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()
    
    init {
        loadSuggestedLocations()
        loadRecentLocations()
    }
    
    private fun loadSuggestedLocations() {
        // Sample suggested locations
        val suggested = listOf(
            "San Antonio, TX",
            "Austin, TX",
            "Dallas, TX",
            "Houston, TX",
            "Jakarta, Indonesia",
            "Bandung, Indonesia",
            "Surabaya, Indonesia"
        )
        _uiState.update { it.copy(suggestedLocations = suggested) }
    }
    
    private fun loadRecentLocations() {
        // TODO: Load from local storage
        val recent = listOf(
            "San Antonio, TX",
            "Austin, TX"
        )
        _uiState.update { it.copy(recentLocations = recent) }
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        // Filter suggested locations
        if (query.isNotEmpty()) {
            val filtered = _uiState.value.suggestedLocations.filter {
                it.contains(query, ignoreCase = true)
            }
            _uiState.update { it.copy(suggestedLocations = filtered) }
        } else {
            loadSuggestedLocations()
        }
    }
    
    fun selectLocation(location: String) {
        _uiState.update { it.copy(currentLocation = location) }
        
        // Add to recent locations
        val recent = _uiState.value.recentLocations.toMutableList()
        recent.remove(location)
        recent.add(0, location)
        if (recent.size > 3) {
            recent.removeLast()
        }
        _uiState.update { it.copy(recentLocations = recent) }
        
        // TODO: Save to local storage and update user profile
    }
    
    fun getCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Implement actual GPS location fetching
                delay(1000)
                
                // Simulated current location
                val location = "San Antonio, TX"
                selectLocation(location)
                
                _uiState.update { it.copy(isLoading = false) }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
