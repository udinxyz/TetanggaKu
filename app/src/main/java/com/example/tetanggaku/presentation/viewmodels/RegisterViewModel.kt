package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }
    
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }
    
    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }
    
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }
    
    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }
    
    fun register() {
        viewModelScope.launch {
            val currentState = _uiState.value
            
            // Validasi input kosong
            if (currentState.name.isBlank()) {
                _uiState.update { 
                    it.copy(errorMessage = "Nama tidak boleh kosong") 
                }
                return@launch
            }
            
            if (currentState.email.isBlank()) {
                _uiState.update { 
                    it.copy(errorMessage = "Email tidak boleh kosong") 
                }
                return@launch
            }
            
            // Validasi format email sederhana
            if (!currentState.email.contains("@")) {
                _uiState.update { 
                    it.copy(errorMessage = "Format email tidak valid") 
                }
                return@launch
            }
            
            if (currentState.phone.isBlank()) {
                _uiState.update { 
                    it.copy(errorMessage = "Nomor telepon tidak boleh kosong") 
                }
                return@launch
            }
            
            if (currentState.password.isBlank()) {
                _uiState.update { 
                    it.copy(errorMessage = "Password tidak boleh kosong") 
                }
                return@launch
            }
            
            // Validasi panjang password
            if (currentState.password.length < 6) {
                _uiState.update { 
                    it.copy(errorMessage = "Password minimal 6 karakter") 
                }
                return@launch
            }
            
            // Validasi password match
            if (currentState.password != currentState.confirmPassword) {
                _uiState.update { 
                    it.copy(errorMessage = "Password tidak cocok") 
                }
                return@launch
            }
            
            // Set loading state
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // TODO: Implementasi logic registration sebenarnya
                // Contoh: val result = authRepository.register(name, email, phone, password)
                
                // Simulasi proses register (hapus ini saat implementasi real)
                kotlinx.coroutines.delay(1500)
                
                // Update state jika berhasil
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isRegistered = true,
                        errorMessage = null
                    ) 
                }
            } catch (e: Exception) {
                // Handle error
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Terjadi kesalahan saat registrasi"
                    ) 
                }
            }
        }
    }
    
    fun registerWithSocial(provider: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // TODO: Implementasi social registration
                // Contoh: val result = authRepository.registerWithProvider(provider)
                
                kotlinx.coroutines.delay(1500)
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isRegistered = true,
                        errorMessage = null
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal registrasi dengan $provider"
                    ) 
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
