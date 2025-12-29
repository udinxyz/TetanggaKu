package com.example.tetanggaku.presentation.viewmodels


data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String, // e.g. "verified", "star", "rocket" - we will map this to IconVector
    val isUnlocked: Boolean = false
)

data class ProfileUiState(
    val userName: String = "Udinxyz",
    val userEmail: String = "udinxyz@example.com",
    val userPhoto: Int? = null, // Resource ID untuk foto profil
    val userTitle: String = "Warga Baru", // Title based on level
    val totalJobsCompleted: Int = 0,
    val neighborRating: Double = 0.0,
    val level: Int = 1,
    val currentXp: Int = 0,
    val nextLevelXp: Int = 1000,
    val badge: String = "Tetangga Aktif",
    val badges: List<Badge> = emptyList(), // Koleksi achievement badges
    val unreadMessages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showEditDialog: Boolean = false,
    val isUpdatingProfile: Boolean = false
)
