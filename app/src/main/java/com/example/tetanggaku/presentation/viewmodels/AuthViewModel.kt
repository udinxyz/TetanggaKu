package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }
    
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }
    
    fun login() {
        viewModelScope.launch {
            val currentState = _uiState.value
            
            // Validasi input
            if (currentState.email.isBlank() || currentState.password.isBlank()) {
                _uiState.update { 
                    it.copy(errorMessage = "Email dan password tidak boleh kosong") 
                }
                return@launch
            }
            
            // Set loading state
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // TODO: Implementasi logic authentication sebenarnya
                // Contoh: val result = authRepository.login(email, password)
                
                // Simulasi proses login (hapus ini saat implementasi real)
                kotlinx.coroutines.delay(1000)
                
                // Update state jika berhasil
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        errorMessage = null
                    ) 
                }
            } catch (e: Exception) {
                // Handle error
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Terjadi kesalahan saat login"
                    ) 
                }
            }
        }
    }
    
    fun loginWithSocial(provider: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // TODO: Implementasi social login
                // Contoh: val result = authRepository.loginWithProvider(provider)
                
                kotlinx.coroutines.delay(1000)
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        errorMessage = null
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal login dengan $provider"
                    ) 
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
