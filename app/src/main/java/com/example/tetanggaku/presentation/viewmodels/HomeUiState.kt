package com.example.tetanggaku.presentation.viewmodels

import androidx.compose.ui.graphics.vector.ImageVector

// Sealed class untuk data state
sealed class JobListState {
    object Loading : JobListState()
    data class Success(val jobs: List<JobItem>) : JobListState()
    object Empty : JobListState()
    data class Error(val message: String) : JobListState()
}

data class HomeUiState(
    val selectedTab: HomeTab = HomeTab.HOME,
    val availableJobs: JobListState = JobListState.Loading,
    val myJobs: JobListState = JobListState.Loading,
    
    // Service Categories
    val serviceCategories: List<ServiceCategory> = emptyList(),
    
    // Location
    val currentLocation: String = "San Antonio, TX",
    
    // Notifications
    val unreadNotificationCount: Int = 0,
    
    // AI Feature states
    val showAiSheet: Boolean = false,
    val aiQuestion: String = "",
    val aiAnswer: String = "",
    val aiLoading: Boolean = false
)

// Tab enum
enum class HomeTab {
    HOME, JOBS, CHAT, PROFILE
}

// Job item data model
data class JobItem(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val price: String,
    val requester: String,
    val verified: Boolean,
    val rating: Float = 0f,
    val reviewCount: Int = 0
)

// Service Category data model
data class ServiceCategory(
    val id: String,
    val name: String,
    val iconName: String // We'll use this to determine which icon to show
)
