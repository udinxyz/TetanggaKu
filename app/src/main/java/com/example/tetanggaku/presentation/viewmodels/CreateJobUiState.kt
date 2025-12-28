package com.example.tetanggaku.presentation.viewmodels

data class CreateJobUiState(
    val title: String = "",
    val category: String = "",
    val reward: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val description: String = "",
    val volunteerNeeded: String = "",
    val note: String = "",
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
