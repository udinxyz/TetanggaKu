package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HelperProfileViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(HelperProfileUiState())
    val uiState: StateFlow<HelperProfileUiState> = _uiState.asStateFlow()
    
    fun loadHelperProfile(helperId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Load from repository/API
                delay(500)
                
                // Sample helper profile
                val helperProfile = HelperProfile(
                    id = helperId,
                    name = "Budi Santoso",
                    rating = 4.8,
                    reviewCount = 23,
                    completedJobs = 45,
                    level = 5,
                    title = "Tetangga Teladan",
                    badges = listOf("Fast Responder", "Reliable", "Top Helper"),
                    bio = "Saya senang membantu tetangga. Berpengalaman dalam pekerjaan angkut barang, perbaikan ringan, dan jastip.",
                    location = "Komplek Melati, Jakarta Selatan",
                    memberSince = "Jan 2024",
                    responseTime = "< 1 jam",
                    skills = listOf("Angkut Barang", "Perbaikan", "Jastip", "Jaga Rumah"),
                    reviews = listOf(
                        HelperReview(
                            id = "r1",
                            reviewerName = "Ibu Sari",
                            rating = 5,
                            comment = "Kerja cepat dan rapi! Sangat membantu.",
                            jobTitle = "Bantu Angkat Lemari",
                            date = "2 hari lalu"
                        ),
                        HelperReview(
                            id = "r2",
                            reviewerName = "Pak Ahmad",
                            rating = 5,
                            comment = "Ramah dan profesional. Recommended!",
                            jobTitle = "Titip Beli Obat",
                            date = "1 minggu lalu"
                        ),
                        HelperReview(
                            id = "r3",
                            reviewerName = "Mbak Dewi",
                            rating = 4,
                            comment = "Baik dan tepat waktu.",
                            jobTitle = "Jaga Rumah",
                            date = "2 minggu lalu"
                        )
                    )
                )
                
                _uiState.update {
                    it.copy(
                        helper = helperProfile,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat profil"
                    )
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
