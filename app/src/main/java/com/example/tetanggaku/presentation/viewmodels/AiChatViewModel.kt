package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class AiChatViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()
    
    init {
        loadChatHistory()
        // Add welcome message
        addAiMessage("Halo! Saya Tetangga AI, asisten virtual kamu. Bagaimana saya bisa membantu hari ini?")
    }
    
    private fun loadChatHistory() {
        // Sample chat history
        val history = listOf(
            ChatHistory(
                "1",
                "Tips Angkat Lemari",
                "Bagaimana cara aman angkat lemari?",
                "Kemarin"
            ),
            ChatHistory(
                "2",
                "Rekomendasi Harga",
                "Berapa harga wajar untuk...",
                "2 hari lalu"
            )
        )
        _uiState.update { it.copy(chatHistory = history) }
    }
    
    fun updateInput(text: String) {
        _uiState.update { it.copy(currentInput = text) }
    }
    
    fun sendMessage() {
        val input = _uiState.value.currentInput.trim()
        if (input.isEmpty()) return
        
        // Add user message
        val userMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            content = input,
            isFromUser = true
        )
        
        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                currentInput = "",
                isLoading = true
            )
        }
        
        // Simulate AI response
        viewModelScope.launch {
            delay(1500)
            
            val aiResponse = generateAiResponse(input)
            val aiMessage = ChatMessage(
                id = System.currentTimeMillis().toString(),
                content = aiResponse,
                isFromUser = false
            )
            
            _uiState.update {
                it.copy(
                    messages = it.messages + aiMessage,
                    isLoading = false
                )
            }
        }
    }
    
    private fun addAiMessage(content: String) {
        val message = ChatMessage(
            id = System.currentTimeMillis().toString(),
            content = content,
            isFromUser = false
        )
        _uiState.update {
            it.copy(messages = it.messages + message)
        }
    }
    
    private fun generateAiResponse(question: String): String {
        return when {
            question.contains("harga", ignoreCase = true) -> 
                "Berdasarkan data, harga rata-rata untuk layanan serupa di area kamu adalah Rp25.000 - Rp50.000. Tapi harga bisa berbeda tergantung kompleksitas pekerjaan."
            
            question.contains("angkat", ignoreCase = true) || question.contains("berat", ignoreCase = true) ->
                "Tips aman mengangkat barang berat:\n\n1. Minta bantuan minimal 1-2 orang\n2. Kosongkan isi barang dulu\n3. Gunakan kain/karpet sebagai alas untuk digeser\n4. Jaga postur tubuh - tekuk lutut, jangan membungkuk\n5. Istirahat setiap 5-10 menit"
            
            question.contains("job", ignoreCase = true) || question.contains("pekerjaan", ignoreCase = true) ->
                "Rekomendasi job untuk kamu:\n\n• Titip beli di Alfamart (Rp15.000)\n• Bantu angkat barang (Rp30.000)\n• Jaga rumah sementara (Rp50.000)\n\nMau lihat yang mana?"
            
            else ->
                "Terima kasih atas pertanyaannya! Saya akan coba bantu. Bisa jelaskan lebih detail apa yang kamu butuhkan? Misalnya kategori layanan, lokasi, atau budget yang kamu punya."
        }
    }
    
    fun toggleHistory() {
        _uiState.update { it.copy(showHistory = !it.showHistory) }
    }
    
    fun selectChatHistory(history: ChatHistory) {
        // Load previous chat
        _uiState.update { it.copy(showHistory = false) }
        // Could load full conversation here
    }
    
    fun clearChat() {
        _uiState.update {
            it.copy(
                messages = emptyList(),
                currentInput = ""
            )
        }
        addAiMessage("Chat dibersihkan. Ada yang bisa saya bantu?")
    }
}
