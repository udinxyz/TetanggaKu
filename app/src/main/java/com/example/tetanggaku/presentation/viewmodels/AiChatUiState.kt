package com.example.tetanggaku.presentation.viewmodels

data class ChatMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatHistory(
    val id: String,
    val title: String,
    val lastMessage: String,
    val date: String
)

data class AiChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val chatHistory: List<ChatHistory> = emptyList(),
    val currentInput: String = "",
    val isLoading: Boolean = false,
    val showHistory: Boolean = false,
    val errorMessage: String? = null
)
