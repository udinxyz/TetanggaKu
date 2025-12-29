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
    
    
    // Show confirmation dialog
    fun showTakeJobDialog() {
        _uiState.update { it.copy(showConfirmDialog = true) }
    }
    
    // Dismiss all dialogs
    fun dismissDialogs() {
        _uiState.update { 
            it.copy(
                showConfirmDialog = false,
                showCompleteDialog = false,
                showRatingDialog = false
            ) 
        }
    }
    
    // Confirm and take job
    fun confirmTakeJob() {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isTakingJob = true,
                    showConfirmDialog = false
                ) 
            }
            
            try {
                // TODO: Implementasi take job ke API
                // val result = jobRepository.takeJob(uiState.value.jobId)
                
                kotlinx.coroutines.delay(1000)
                
                _uiState.update {
                    it.copy(
                        isTakingJob = false,
                        isJobTaken = true,
                        status = "Ongoing",
                        successMessage = "Job berhasil diambil! Selamat bekerja 💪"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isTakingJob = false,
                        errorMessage = "Gagal mengambil job. Coba lagi."
                    )
                }
            }
        }
    }
    
    // Show complete dialog
    fun showCompleteDialog() {
        _uiState.update { it.copy(showCompleteDialog = true) }
    }
    
    // Mark job as complete (submitted by helper, waiting requester confirmation)
    fun markAsComplete() {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isMarkingComplete = true,
                    showCompleteDialog = false
                ) 
            }
            
            try {
                // TODO: Submit completion ke API
                kotlinx.coroutines.delay(1000)
                
                _uiState.update {
                    it.copy(
                        isMarkingComplete = false,
                        isWaitingConfirmation = true,
                        status = "Waiting Confirmation",
                        successMessage = "Pekerjaan telah disubmit! Menunggu konfirmasi pemberi job ⏳"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isMarkingComplete = false,
                        errorMessage = "Gagal submit pekerjaan"
                    )
                }
            }
        }
    }
    
    // Simulate requester confirming job (for demo purposes)
    fun simulateRequesterConfirmation() {
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(500)
                
                _uiState.update {
                    it.copy(
                        isWaitingConfirmation = false,
                        isJobCompleted = true,
                        status = "Completed",
                        showRatingDialog = true,
                        successMessage = "Pemberi job sudah konfirmasi! Berikan rating yuk 🌟"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error konfirmasi")
                }
            }
        }
    }
    
    // Update rating
    fun setRating(rating: Int) {
        _uiState.update { it.copy(userRating = rating) }
    }
    
    // Update review text
    fun setReview(review: String) {
        _uiState.update { it.copy(userReview = review) }
    }
    
    // Submit rating
    fun submitRating() {
        viewModelScope.launch {
            try {
                // TODO: Submit rating ke API
                kotlinx.coroutines.delay(500)
                
                _uiState.update {
                    it.copy(
                        showRatingDialog = false,
                        successMessage = "Terima kasih! +${it.xpReward} XP telah ditambahkan 🎉"
                    )
                }
                
                // TODO: Trigger XP reward
                // profileViewModel.addXp(uiState.value.xpReward)
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Gagal mengirim rating")
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
