package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class CategoryViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()
    
    fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Replace with actual repository call
                delay(600)
                
                // Sample category data
                val category = ServiceCategory(
                    id = categoryId,
                    name = getCategoryName(categoryId),
                    iconName = "category"
                )
                
                // Sample services in category
                val services = listOf(
                    JobItem(
                        id = "c1",
                        category = category.name,
                        title = "${category.name} - Basic Service",
                        description = "Professional ${category.name.lowercase()} service",
                        price = "Rp75.000",
                        requester = "Pro Service",
                        verified = true,
                        rating = 4.6f,
                        reviewCount = 42
                    ),
                    JobItem(
                        id = "c2",
                        category = category.name,
                        title = "${category.name} - Premium",
                        description = "Premium ${category.name.lowercase()} with extra care",
                        price = "Rp120.000",
                        requester = "Elite Services",
                        verified = true,
                        rating = 4.9f,
                        reviewCount = 98
                    ),
                    JobItem(
                        id = "c3",
                        category = category.name,
                        title = "${category.name} - Express",
                        description = "Quick ${category.name.lowercase()} service",
                        price = "Rp90.000",
                        requester = "Fast Pro",
                        verified = false,
                        rating = 4.4f,
                        reviewCount = 35
                    )
                )
                
                _uiState.update {
                    it.copy(
                        category = category,
                        services = services,
                        isLoading = false,
                        error = null
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading category: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun applyFilter(filter: FilterType) {
        _uiState.update { it.copy(selectedFilter = filter) }
        
        val currentServices = _uiState.value.services
        val sortedServices = when (filter) {
            FilterType.PRICE_LOW_TO_HIGH -> currentServices.sortedBy { 
                it.price.filter { c -> c.isDigit() }.toIntOrNull() ?: 0 
            }
            FilterType.PRICE_HIGH_TO_LOW -> currentServices.sortedByDescending { 
                it.price.filter { c -> c.isDigit() }.toIntOrNull() ?: 0 
            }
            FilterType.RATING -> currentServices.sortedByDescending { it.rating }
            FilterType.NONE -> currentServices
        }
        
        _uiState.update { it.copy(services = sortedServices) }
    }
    
    private fun getCategoryName(id: String): String {
        return when (id) {
            "1" -> "Hairdresser"
            "2" -> "Cleaning"
            "3" -> "Painting"
            "4" -> "Cooking"
            else -> "Service"
        }
    }
}
