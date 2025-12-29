package com.example.tetanggaku.presentation.viewmodels

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val selectedLanguage: String = "Bahasa Indonesia",
    val selectedTheme: String = "Light",
    val appVersion: String = "1.0.0",
    val isLoading: Boolean = false
)
