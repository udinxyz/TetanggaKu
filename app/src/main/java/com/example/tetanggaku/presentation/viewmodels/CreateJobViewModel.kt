package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateJobViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(CreateJobUiState())
    val uiState: StateFlow<CreateJobUiState> = _uiState.asStateFlow()
    
    fun updateTitle(value: String) {
        _uiState.update { it.copy(title = value) }
        validateForm()
    }
    
    fun updateCategory(value: String) {
        _uiState.update { it.copy(category = value) }
        validateForm()
    }
    
    fun updateReward(value: String) {
        _uiState.update { it.copy(reward = value) }
        validateForm()
    }
    
    fun updateDate(value: String) {
        _uiState.update { it.copy(date = value) }
        validateForm()
    }
    
    fun updateTime(value: String) {
        _uiState.update { it.copy(time = value) }
        validateForm()
    }
    
    fun updateLocation(value: String) {
        _uiState.update { it.copy(location = value) }
        validateForm()
    }
    
    fun updateDescription(value: String) {
        _uiState.update { it.copy(description = value) }
        validateForm()
    }
    
    fun updateVolunteerNeeded(value: String) {
        _uiState.update { it.copy(volunteerNeeded = value) }
    }
    
    fun updateNote(value: String) {
        _uiState.update { it.copy(note = value) }
    }
    
    private fun validateForm() {
        val currentState = _uiState.value
        val isValid = currentState.title.isNotBlank() &&
                currentState.category.isNotBlank() &&
                currentState.reward.isNotBlank() &&
                currentState.date.isNotBlank() &&
                currentState.location.isNotBlank() &&
                currentState.description.isNotBlank()
        
        _uiState.update { it.copy(isFormValid = isValid) }
    }
    
    fun createJob(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Implementasi create job ke repository/API
                // val result = jobRepository.createJob(
                //     title = _uiState.value.title,
                //     category = _uiState.value.category,
                //     reward = _uiState.value.reward,
                //     date = _uiState.value.date,
                //     time = _uiState.value.time,
                //     location = _uiState.value.location,
                //     description = _uiState.value.description,
                //     volunteerNeeded = _uiState.value.volunteerNeeded,
                //     note = _uiState.value.note
                // )
                
                // Simulasi network call
                kotlinx.coroutines.delay(500)
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    ) 
                }
                
                onSuccess()
                resetForm()
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal membuat job: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun resetForm() {
        _uiState.value = CreateJobUiState()
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
