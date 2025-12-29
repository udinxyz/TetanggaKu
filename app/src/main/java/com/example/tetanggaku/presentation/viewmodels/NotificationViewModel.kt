package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class NotificationViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    
    init {
        loadNotifications()
    }
    
    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Replace with actual repository call
                delay(500)
                
                // Sample notifications
                val notifications = listOf(
                    Notification(
                        id = "n1",
                        title = "Job Completed! ✓",
                        message = "Helper Budi Santoso has completed 'Bantu Angkat Lemari'. Tap to confirm.",
                        timestamp = "5 min ago",
                        isRead = false,
                        type = NotificationType.JOB_COMPLETED
                    ),
                    Notification(
                        id = "n2",
                        title = "Job Taken",
                        message = "Siti Aminah accepted your job 'Titip Beli di Alfamart'",
                        timestamp = "1 hour ago",
                        isRead = false,
                        type = NotificationType.JOB_TAKEN
                    ),
                    Notification(
                        id = "n3",
                        title = "New Applicant",
                        message = "3 people want to take your job 'Jaga Rumah Sementara'",
                        timestamp = "2 hours ago",
                        isRead = true,
                        type = NotificationType.APPLICANT_NEW
                    ),
                    Notification(
                        id = "n4",
                        title = "New job available!",
                        message = "Someone needs help nearby: Help with moving furniture",
                        timestamp = "Yesterday",
                        isRead = true,
                        type = NotificationType.JOB_UPDATE
                    ),
                    Notification(
                        id = "n5",
                        title = "New message",
                        message = "Pak Budi sent you a message",
                        timestamp = "Yesterday",
                        isRead = true,
                        type = NotificationType.CHAT_MESSAGE
                    ),
                    Notification(
                        id = "n6",
                        title = "Welcome to TetanggaKu!",
                        message = "Complete your profile to get started",
                        timestamp = "2 days ago",
                        isRead = true,
                        type = NotificationType.SYSTEM
                    )
                )
                
                val unreadCount = notifications.count { !it.isRead }
                
                _uiState.update {
                    it.copy(
                        notifications = notifications,
                        unreadCount = unreadCount,
                        isLoading = false,
                        error = null
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading notifications: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun markAsRead(notificationId: String) {
        val updatedNotifications = _uiState.value.notifications.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        val unreadCount = updatedNotifications.count { !it.isRead }
        
        _uiState.update {
            it.copy(
                notifications = updatedNotifications,
                unreadCount = unreadCount
            )
        }
        
        // TODO: Update in repository
    }
    
    fun markAllAsRead() {
        val updatedNotifications = _uiState.value.notifications.map {
            it.copy(isRead = true)
        }
        
        _uiState.update {
            it.copy(
                notifications = updatedNotifications,
                unreadCount = 0
            )
        }
        
        // TODO: Update in repository
    }
    
    fun deleteNotification(notificationId: String) {
        val updatedNotifications = _uiState.value.notifications.filter {
            it.id != notificationId
        }
        val unreadCount = updatedNotifications.count { !it.isRead }
        
        _uiState.update {
            it.copy(
                notifications = updatedNotifications,
                unreadCount = unreadCount
            )
        }
        
        // TODO: Delete in repository
    }
}
