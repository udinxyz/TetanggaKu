package com.example.tetanggaku.presentation.viewmodels

data class RequesterJobUiState(
    val myJobs: List<RequesterJob> = emptyList(),
    val activeFilter: JobFilter = JobFilter.ACTIVE,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class RequesterJob(
    val id: String,
    val title: String,
    val category: String,
    val price: String,
    val status: RequesterJobStatus,
    val helper: HelperInfo? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val needsConfirmation: Boolean = false,
    val applicantsCount: Int = 0
)

data class HelperInfo(
    val id: String,
    val name: String,
    val rating: Double,
    val completedJobs: Int,
    val photoUrl: String? = null,
    val level: Int = 1,
    val title: String = "Warga Baru"
)

enum class RequesterJobStatus {
    OPEN,              // Baru dibuat, menunggu helper
    IN_PROGRESS,       // Ada helper yang sedang mengerjakan
    WAITING_REVIEW,    // Helper sudah submit, menunggu konfirmasi
    COMPLETED,         // Sudah selesai dan dikonfirmasi
    CANCELLED          // Dibatalkan
}

enum class JobFilter {
    ACTIVE,    // OPEN + IN_PROGRESS + WAITING_REVIEW
    HISTORY    // COMPLETED + CANCELLED
}
