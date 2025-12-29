package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadServiceCategories()
        loadAvailableJobs()
        loadMyJobs()
        loadNotificationCount()
        loadDailyMissions()
    }

    private fun loadDailyMissions() {
        // Sample Daily Missions
        val missions = listOf(
            DailyMission("1", "Login hari ini", 10, true),
            DailyMission("2", "Lihat 3 job baru", 20, false),
            DailyMission("3", "Bantu 1 tetangga", 50, false)
        )
        _uiState.update { it.copy(dailyMissions = missions) }
    }
    
    private fun loadServiceCategories() {
        // Sample categories matching the design
        val categories = listOf(
            ServiceCategory(id = "1", name = "Hairdresser", iconName = "scissors"),
            ServiceCategory(id = "2", name = "Cleaning", iconName = "cleaning"),
            ServiceCategory(id = "3", name = "Painting", iconName = "painting"),
            ServiceCategory(id = "4", name = "Cooking", iconName = "cooking")
        )
        _uiState.update { it.copy(serviceCategories = categories) }
    }
    
    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
    
    fun loadAvailableJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(availableJobs = JobListState.Loading) }
            
            try {
                // TODO: Replace with actual repository call
                // val jobs = jobRepository.getAvailableJobs()
                
                // Simulasi network call
                delay(1000)
                
                // Sample data
                val sampleJobs = listOf(
                    JobItem(
                        id = "1",
                        category = "Angkut",
                        title = "Bantu angkat lemari ke lantai 2",
                        description = "Lemari kayu cukup berat, butuh 2 orang bantu angkat ke lantai atas.",
                        price = "Rp50.000",
                        requester = "Mbak Sari",
                        verified = true,
                        rating = 4.5f,
                        reviewCount = 63
                    ),
                    JobItem(
                        id = "2",
                        category = "Titip Beli",
                        title = "Titip beli bahan masakan",
                        description = "Tolong belikan bawang merah 1kg, cabai 1/2kg, tomat 1kg di pasar.",
                        price = "Rp25.000",
                        requester = "Pak Budi",
                        verified = true,
                        rating = 4.8f,
                        reviewCount = 120
                    ),
                    JobItem(
                        id = "3",
                        category = "Perbaikan",
                        title = "Bantu pasang lampu taman",
                        description = "Pasang 3 lampu taman di halaman rumah. Tangga sudah disediakan.",
                        price = "Rp40.000",
                        requester = "Bu Ani",
                        verified = false,
                        rating = 4.3f,
                        reviewCount = 45
                    )
                )
                
                _uiState.update {
                    it.copy(
                        availableJobs = if (sampleJobs.isEmpty()) {
                            JobListState.Empty
                        } else {
                            JobListState.Success(sampleJobs)
                        }
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(availableJobs = JobListState.Error("Gagal memuat job: ${e.message}"))
                }
            }
        }
    }
    
    fun loadMyJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(myJobs = JobListState.Loading) }
            
            try {
                // TODO: Replace with actual repository call
                // val jobs = jobRepository.getMyJobs()
                
                // Simulasi network call
                delay(800)
                
                // Sample data
                val sampleMyJobs = listOf(
                    JobItem(
                        id = "m1",
                        category = "Titip beli",
                        title = "Titip beli obat flu",
                        description = "Titip beli obat flu dan vitamin di apotek depan komplek.",
                        price = "Rp30.000",
                        requester = "Kamu",
                        verified = false
                    )
                )
                
                _uiState.update {
                    it.copy(
                        myJobs = if (sampleMyJobs.isEmpty()) {
                            JobListState.Empty
                        } else {
                            JobListState.Success(sampleMyJobs)
                        }
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(myJobs = JobListState.Error("Gagal memuat job: ${e.message}"))
                }
            }
        }
    }
    
    fun refreshJobs() {
        loadAvailableJobs()
        loadMyJobs()
    }
    
    // AI Feature functions
    fun showAiSheet() {
        _uiState.update { it.copy(showAiSheet = true) }
    }
    
    fun hideAiSheet() {
        _uiState.update { 
            it.copy(
                showAiSheet = false,
                aiQuestion = "",
                aiAnswer = ""
            ) 
        }
    }
    
    fun updateAiQuestion(question: String) {
        _uiState.update { it.copy(aiQuestion = question) }
    }
    
    fun askAI() {
        viewModelScope.launch {
            _uiState.update { it.copy(aiLoading = true) }
            
            try {
                // TODO: Replace with actual AI API call
                // val answer = aiRepository.ask(_uiState.value.aiQuestion)
                
                // Simulasi AI response
                delay(1500)
                
                val answer = "Ini contoh jawaban AI:\n\n" +
                        "1. Kalau berat, minta bantuan tetangga lain.\n" +
                        "2. Kosongkan isi lemari dulu supaya lebih ringan.\n" +
                        "3. Pakai alas kain/karpet di kaki lemari agar mudah digeser.\n" +
                        "4. Jaga punggung tetap lurus dan angkat dengan kekuatan kaki."
                
                _uiState.update { 
                    it.copy(
                        aiAnswer = answer,
                        aiLoading = false
                    ) 
                }
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        aiAnswer = "Maaf, terjadi kesalahan: ${e.message}",
                        aiLoading = false
                    ) 
                }
            }
        }
    }
    
    // Location functions
    fun updateLocation(location: String) {
        _uiState.update { it.copy(currentLocation = location) }
    }
    
    // Notification functions  
    fun loadNotificationCount() {
        viewModelScope.launch {
            // TODO: Replace with actual repository call
            delay(500)
            
            // Sample unread count
            val unreadCount = 2
            _uiState.update { it.copy(unreadNotificationCount = unreadCount) }
        }
    }
}
