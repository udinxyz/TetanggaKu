package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProfileDetailViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MyProfileDetailUiState())
    val uiState: StateFlow<MyProfileDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadProfileDetail()
    }
    
    private fun loadProfileDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Load from repository/API
                // Simulasi loading
                kotlinx.coroutines.delay(500)
                
                // Dummy data
                val jobHistory = listOf(
                    JobHistoryItem(
                        id = "1",
                        title = "Perbaiki Keran Bocor",
                        category = "Tukang",
                        price = 75000,
                        date = "25 Des 2025",
                        status = "completed",
                        rating = 5.0
                    ),
                    JobHistoryItem(
                        id = "2",
                        title = "Pasang Lampu Taman",
                        category = "Listrik",
                        price = 150000,
                        date = "20 Des 2025",
                        status = "completed",
                        rating = 4.8
                    ),
                    JobHistoryItem(
                        id = "3",
                        title = "Service AC",
                        category = "Elektronik",
                        price = 200000,
                        date = "15 Des 2025",
                        status = "completed",
                        rating = 5.0
                    ),
                    JobHistoryItem(
                        id = "4",
                        title = "Cat Pagar",
                        category = "Tukang",
                        price = 300000,
                        date = "10 Des 2025",
                        status = "completed",
                        rating = 4.5
                    )
                )
                
                val badges = listOf(
                    Badge("1", "Warga Baru", "Bergabung dengan TetanggaKu", "home", true),
                    Badge("2", "Si Gercep", "Merespon job dalam 5 menit", "rocket", true),
                    Badge("3", "Bintang 5", "Mendapatkan rating 5.0 pertama", "star", true),
                    Badge("4", "Dermawan", "Membantu 50 tetangga", "heart", false),
                    Badge("5", "Sultan", "Total penghasilan 1 juta", "wallet", false),
                    Badge("6", "Tetangga Teladan", "Rating 4.8+ dengan 20+ job", "star", true)
                )
                
                _uiState.update {
                    it.copy(
                        userName = "Udinxyz",
                        userEmail = "udinxyz@example.com",
                        userTitle = "Tetangga Teladan",
                        level = 4,
                        currentXp = 1250,
                        nextLevelXp = 2000,
                        totalJobsCompleted = 12,
                        totalEarnings = 1450000,
                        totalHoursWorked = 36,
                        averageRating = 4.8,
                        responseTime = "< 5 menit",
                        jobHistory = jobHistory,
                        badges = badges,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat detail profil"
                    )
                }
            }
        }
    }
    
    fun refreshProfile() {
        loadProfileDetail()
    }
}
