package com.example.tetanggaku.presentation.viewmodels

enum class JobCategory {
    ONGOING,    // Sedang Berlangsung
    COMPLETED   // Sudah Selesai
}

data class JobUiState(
    val selectedCategory: JobCategory = JobCategory.ONGOING,
    val ongoingJobs: List<Job> = emptyList(),
    val completedJobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class Job(
    val id: String,
    val title: String,
    val category: String,
    val shortDescription: String,
    val price: String,
    val status: String,
    val progress: Float,
    val isCompleted: Boolean = false,
    val completedDate: String? = null,  // "2 hari lalu"
    val rating: Float? = null            // 0.0 - 5.0
)
