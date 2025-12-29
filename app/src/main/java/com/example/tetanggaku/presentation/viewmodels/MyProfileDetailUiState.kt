package com.example.tetanggaku.presentation.viewmodels

data class JobHistoryItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Int,
    val date: String,
    val status: String, // "completed", "cancelled", "in_progress"
    val rating: Double? = null
)

data class MyProfileDetailUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userTitle: String = "",
    val userPhoto: Int? = null,
    
    // Statistics
    val level: Int = 1,
    val currentXp: Int = 0,
    val nextLevelXp: Int = 1000,
    val totalJobsCompleted: Int = 0,
    val totalEarnings: Int = 0,
    val totalHoursWorked: Int = 0,
    val averageRating: Double = 0.0,
    val responseTime: String = "",
    
    // Job History
    val jobHistory: List<JobHistoryItem> = emptyList(),
    
    // Badges
    val badges: List<Badge> = emptyList(),
    
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
