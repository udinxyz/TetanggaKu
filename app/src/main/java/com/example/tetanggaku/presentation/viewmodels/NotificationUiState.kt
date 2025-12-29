package com.example.tetanggaku.presentation.viewmodels

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.GENERAL
)

enum class NotificationType {
    GENERAL,
    JOB_UPDATE,
    CHAT_MESSAGE,
    SYSTEM,
    JOB_TAKEN,          // Helper mengambil job requester
    JOB_COMPLETED,      // Helper submit completion
    JOB_CANCELLED,      // Job dibatalkan
    APPLICANT_NEW       // Ada applicant baru
}

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
