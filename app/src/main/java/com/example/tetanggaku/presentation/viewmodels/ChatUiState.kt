package com.example.tetanggaku.presentation.viewmodels

// Conversation item data model
data class ConversationItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val avatarUrl: String? = null,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
)

// Chat UI State
data class ChatUiState(
    val conversations: List<ConversationItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
