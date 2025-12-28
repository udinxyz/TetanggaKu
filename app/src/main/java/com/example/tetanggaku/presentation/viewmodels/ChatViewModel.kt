package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ChatViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    init {
        loadConversations()
    }
    
    private fun loadConversations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Replace with actual repository call
                // Simulasi network call
                delay(800)
                
                // Sample conversations data
                val sampleConversations = listOf(
                    ConversationItem(
                        id = "1",
                        name = "Mbak Sari",
                        lastMessage = "Oke, terima kasih ya!",
                        timestamp = "10:30 AM",
                        unreadCount = 0,
                        isOnline = true
                    ),
                    ConversationItem(
                        id = "2",
                        name = "Pak Budi",
                        lastMessage = "Barang sudah saya titip di pos satpam",
                        timestamp = "09:15 AM",
                        unreadCount = 2,
                        isOnline = false
                    ),
                    ConversationItem(
                        id = "3",
                        name = "Bu Ani",
                        lastMessage = "Kapan bisa bantu pasang lampunya?",
                        timestamp = "Yesterday",
                        unreadCount = 1,
                        isOnline = false
                    ),
                    ConversationItem(
                        id = "4",
                        name = "Mas Andi",
                        lastMessage = "Terima kasih sudah membantu",
                        timestamp = "Yesterday",
                        unreadCount = 0,
                        isOnline = true
                    )
                )
                
                _uiState.update { 
                    it.copy(
                        conversations = sampleConversations,
                        isLoading = false
                    ) 
                }
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Gagal memuat percakapan: ${e.message}"
                    ) 
                }
            }
        }
    }
    
    fun refreshConversations() {
        loadConversations()
    }
}
