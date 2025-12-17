package com.example.tetanggaku.presentation.viewmodels

data class ProfileUiState(
    val userName: String = "Udinxyz",
    val userEmail: String = "udinxyz@example.com",
    val userPhoto: Int? = null, // Resource ID untuk foto profil
    val totalJobsCompleted: Int = 0,
    val neighborRating: Double = 0.0,
    val level: Int = 1,
    val currentXp: Int = 0,
    val nextLevelXp: Int = 1000,
    val badge: String = "Tetangga Aktif",
    val unreadMessages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
