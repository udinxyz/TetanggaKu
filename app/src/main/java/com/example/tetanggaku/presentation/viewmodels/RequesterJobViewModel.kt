package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class RequesterJobViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(RequesterJobUiState())
    val uiState: StateFlow<RequesterJobUiState> = _uiState.asStateFlow()
    
    init {
        loadMyJobs()
    }
    
    fun loadMyJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Load dari repository/API
                delay(500)
                
                // Sample data
                val sampleJobs = listOf(
                    RequesterJob(
                        id = "1",
                        title = "Bantu Angkat Lemari",
                        category = "Angkut",
                        price = "Rp50.000",
                        status = RequesterJobStatus.WAITING_REVIEW,
                        helper = HelperInfo(
                            id = "h1",
                            name = "Budi Santoso",
                            rating = 4.8,
                            completedJobs = 23,
                            level = 5,
                            title = "Tetangga Teladan"
                        ),
                        needsConfirmation = true
                    ),
                    RequesterJob(
                        id = "2",
                        title = "Titip Beli di Alfamart",
                        category = "Jastip",
                        price = "Rp15.000",
                        status = RequesterJobStatus.IN_PROGRESS,
                        helper = HelperInfo(
                            id = "h2",
                            name = "Siti Aminah",
                            rating = 4.9,
                            completedJobs = 45,
                            level = 7,
                            title = "Sesepuh Komplek"
                        )
                    ),
                    RequesterJob(
                        id = "3",
                        title = "Jaga Rumah Sementara",
                        category = "Jaga",
                        price = "Rp100.000",
                        status = RequesterJobStatus.OPEN,
                        applicantsCount = 3
                    ),
                    RequesterJob(
                        id = "4",
                        title = "Perbaiki Kran Bocor",
                        category = "Perbaikan",
                        price = "Rp75.000",
                        status = RequesterJobStatus.COMPLETED,
                        helper = HelperInfo(
                            id = "h3",
                            name = "Ahmad Wijaya",
                            rating = 4.7,
                            completedJobs = 18,
                            level = 4,
                            title = "Tetangga Ramah"
                        ),
                        completedAt = System.currentTimeMillis() - 86400000
                    )
                )
                
                _uiState.update {
                    it.copy(
                        myJobs = sampleJobs,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat job"
                    )
                }
            }
        }
    }
    
    fun setFilter(filter: JobFilter) {
        _uiState.update { it.copy(activeFilter = filter) }
    }
    
    fun getFilteredJobs(): List<RequesterJob> {
        val jobs = _uiState.value.myJobs
        return when (_uiState.value.activeFilter) {
            JobFilter.ACTIVE -> jobs.filter {
                it.status in listOf(
                    RequesterJobStatus.OPEN,
                    RequesterJobStatus.IN_PROGRESS,
                    RequesterJobStatus.WAITING_REVIEW
                )
            }
            JobFilter.HISTORY -> jobs.filter {
                it.status in listOf(
                    RequesterJobStatus.COMPLETED,
                    RequesterJobStatus.CANCELLED
                )
            }
        }
    }
    
    fun confirmJobCompletion(jobId: String) {
        viewModelScope.launch {
            try {
                // TODO: API call
                delay(800)
                
                _uiState.update {
                    val updatedJobs = it.myJobs.map { job ->
                        if (job.id == jobId) {
                            job.copy(
                                status = RequesterJobStatus.COMPLETED,
                                needsConfirmation = false,
                                completedAt = System.currentTimeMillis()
                            )
                        } else job
                    }
                    it.copy(myJobs = updatedJobs)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Gagal konfirmasi job")
                }
            }
        }
    }
    
    fun rejectCompletion(jobId: String, reason: String) {
        viewModelScope.launch {
            try {
                // TODO: API call with reason
                delay(500)
                
                _uiState.update {
                    val updatedJobs = it.myJobs.map { job ->
                        if (job.id == jobId) {
                            job.copy(
                                status = RequesterJobStatus.IN_PROGRESS,
                                needsConfirmation = false
                            )
                        } else job
                    }
                    it.copy(myJobs = updatedJobs)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Gagal menolak completion")
                }
            }
        }
    }
    
    fun cancelJob(jobId: String) {
        viewModelScope.launch {
            try {
                // TODO: API call
                delay(500)
                
                _uiState.update {
                    val updatedJobs = it.myJobs.map { job ->
                        if (job.id == jobId) {
                            job.copy(status = RequesterJobStatus.CANCELLED)
                        } else job
                    }
                    it.copy(myJobs = updatedJobs)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Gagal membatalkan job")
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
