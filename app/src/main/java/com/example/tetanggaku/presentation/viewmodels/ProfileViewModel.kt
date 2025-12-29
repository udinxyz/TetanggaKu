package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        // Load profile data saat ViewModel dibuat
        loadProfileData()
    }
    
    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Implementasi load profile dari repository/API
                // Contoh: val profile = profileRepository.getUserProfile()
                
                // Simulasi loading data (hapus saat implementasi real)
                kotlinx.coroutines.delay(500)
                
                // Dummy data untuk demo
                _uiState.update {
                    it.copy(
                        userName = "Udinxyz",
                        userEmail = "udinxyz@example.com",
                        userTitle = "Tetangga Teladan", // Level 4 Title
                        totalJobsCompleted = 12,
                        neighborRating = 4.8,
                        level = 4,
                        currentXp = 1250,
                        nextLevelXp = 2000,

                        badge = "Tetangga Aktif",
                        badges = listOf(
                            Badge("1", "Warga Baru", "Bergabung dengan TetanggaKu", "home", true),
                            Badge("2", "Si Gercep", "Merespon job dalam 5 menit", "rocket", true),
                            Badge("3", "Bintang 5", "Mendapatkan rating 5.0 pertama", "star", true),
                            Badge("4", "Dermawan", "Membantu 50 tetangga", "heart", false),
                            Badge("5", "Sultan", "Total penghasilan 1 juta", "wallet", false)
                        ),
                        unreadMessages = 2,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat data profil"
                    )
                }
            }
        }
    }
    
    fun refreshProfile() {
        loadProfileData()
    }
    
    fun updateUserName(newName: String) {
        viewModelScope.launch {
            try {
                // TODO: Implementasi update name ke API
                // val result = profileRepository.updateUserName(newName)
                
                _uiState.update { it.copy(userName = newName) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "Gagal mengupdate nama") 
                }
            }
        }
    }
    
    fun markMessagesAsRead() {
        _uiState.update { it.copy(unreadMessages = 0) }
    }
    
    fun addXp(xpAmount: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val newXp = currentState.currentXp + xpAmount
            
            // Cek apakah naik level
            if (newXp >= currentState.nextLevelXp) {
                val remainingXp = newXp - currentState.nextLevelXp
                val newLevel = currentState.level + 1
                val newNextLevelXp = calculateNextLevelXp(newLevel)
                val newTitle = getTitleForLevel(newLevel)
                
                _uiState.update {
                    it.copy(
                        level = newLevel,
                        currentXp = remainingXp,
                        nextLevelXp = newNextLevelXp,
                        userTitle = newTitle
                    )
                }
                
                // TODO: Trigger level up notification/animation
            } else {
                _uiState.update { it.copy(currentXp = newXp) }
            }
        }
    }
    
    private fun getTitleForLevel(level: Int): String {
        return when (level) {
            1 -> "Warga Baru"
            2 -> "Tetangga Ramah"
            3 -> "Tetangga Peduli"
            4 -> "Tetangga Teladan"
            5 -> "Sesepuh Komplek"
            in 6..10 -> "Pahlawan Lokal"
            else -> "Legenda"
        }
    }
    
    private fun calculateNextLevelXp(level: Int): Int {
        // Formula sederhana: setiap level butuh 1000 XP lebih banyak
        return 1000 * level
    }
    
    fun getXpProgress(): Float {
        val state = _uiState.value
        return (state.currentXp.toFloat() / state.nextLevelXp.toFloat()).coerceIn(0f, 1f)
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    fun showEditDialog() {
        _uiState.update { it.copy(showEditDialog = true) }
    }
    
    fun hideEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }
    
    fun updateUserEmail(newEmail: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isUpdatingProfile = true) }
                
                // TODO: Implementasi update email ke API
                // val result = profileRepository.updateUserEmail(newEmail)
                
                _uiState.update { 
                    it.copy(
                        userEmail = newEmail,
                        isUpdatingProfile = false,
                        showEditDialog = false
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        errorMessage = "Gagal mengupdate email",
                        isUpdatingProfile = false
                    ) 
                }
            }
        }
    }
}
