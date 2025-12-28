package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JobDetailViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(JobDetailUiState())
    val uiState: StateFlow<JobDetailUiState> = _uiState.asStateFlow()
    
    fun loadJobDetail(jobId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Load job detail dari repository/API
                // val jobDetail = jobRepository.getJobDetail(jobId)
                
                // Simulasi loading
                kotlinx.coroutines.delay(500)
                
                // Update dengan data default (sudah ada di UiState)
                _uiState.update {
                    it.copy(
                        jobId = jobId,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat detail job"
                    )
                }
            }
        }
    }
    
    fun takeJob() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Implementasi take job ke API
                // val result = jobRepository.takeJob(uiState.value.jobId)
                
                kotlinx.coroutines.delay(1000)
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isJobTaken = true
                    )
                }
                
                // TODO: Trigger XP reward
                // profileViewModel.addXp(uiState.value.xpReward)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengambil job"
                    )
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
