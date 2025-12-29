package com.example.tetanggaku.presentation.viewmodels

data class HelperProfileUiState(
    val helper: HelperProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class HelperProfile(
    val id: String,
    val name: String,
    val photoUrl: String? = null,
    val rating: Double,
    val reviewCount: Int,
    val completedJobs: Int,
    val level: Int,
    val title: String,
    val badges: List<String> = emptyList(),
    val bio: String = "",
    val location: String = "",
    val memberSince: String = "",
    val reviews: List<HelperReview> = emptyList(),
    val skills: List<String> = emptyList(),
    val responseTime: String = "< 1 jam"
)

data class HelperReview(
    val id: String,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val jobTitle: String,
    val date: String
)
