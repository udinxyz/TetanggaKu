package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SearchViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    init {
        loadRecentSearches()
    }
    
    private fun loadRecentSearches() {
        // TODO: Load from local storage
        val recent = listOf("Cleaning", "Hairdresser", "Painting")
        _uiState.update { it.copy(recentSearches = recent) }
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
    
    fun search() {
        val query = _uiState.value.searchQuery
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasSearched = true) }
            
            try {
                // TODO: Replace with actual repository call
                delay(800)
                
                // Sample search results
                val results = listOf(
                    JobItem(
                        id = "s1",
                        category = "Cleaning",
                        title = "House cleaning service",
                        description = "Professional house cleaning",
                        price = "Rp100.000",
                        requester = "Clean Pro",
                        verified = true,
                        rating = 4.7f,
                        reviewCount = 89
                    ),
                    JobItem(
                        id = "s2",
                        category = "Cleaning",
                        title = "Deep cleaning kitchen",
                        description = "Deep kitchen cleaning service",
                        price = "Rp150.000",
                        requester = "KitchenMaster",
                        verified = true,
                        rating = 4.9f,
                        reviewCount = 120
                    )
                ).filter { it.title.contains(query, ignoreCase = true) || 
                          it.category.contains(query, ignoreCase = true) }
                
                _uiState.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false,
                        error = null
                    )
                }
                
                // Add to recent searches
                addToRecentSearches(query)
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error searching: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                hasSearched = false,
                error = null
            )
        }
    }
    
    private fun addToRecentSearches(query: String) {
        val current = _uiState.value.recentSearches.toMutableList()
        current.remove(query)
        current.add(0, query)
        if (current.size > 5) {
            current.removeLast()
        }
        _uiState.update { it.copy(recentSearches = current) }
        // TODO: Save to local storage
    }
    
    fun selectRecentSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        search()
    }
}
