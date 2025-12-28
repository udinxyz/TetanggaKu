package com.example.tetanggaku.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JobViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(JobUiState())
    val uiState: StateFlow<JobUiState> = _uiState.asStateFlow()
    
    init {
        loadJobs()
    }
    
    fun selectCategory(category: JobCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
    
    private fun loadJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Load jobs dari repository/API
                // val ongoingJobs = jobRepository.getOngoingJobs()
                // val completedJobs = jobRepository.getCompletedJobs()
                
                // Simulasi loading
                kotlinx.coroutines.delay(500)
                
                // Sample Ongoing Jobs
                val sampleOngoingJobs = listOf(
                    Job(
                        id = "1",
                        title = "Bantu angkat lemari ke lantai 2",
                        category = "Angkut Barang",
                        shortDescription = "Lemari kayu cukup berat, butuh 2 orang bantu angkat ke lantai atas.",
                        price = "Rp 50.000",
                        status = "Sedang Proses",
                        progress = 0.3f,
                        isCompleted = false
                    ),
                    Job(
                        id = "2",
                        title = "Titip beli bahan masakan",
                        category = "Titip Beli",
                        shortDescription = "Tolong belikan bawang merah 1kg, cabai 1/2kg, tomat 1kg di pasar.",
                        price = "Rp 25.000",
                        status = "Sedang Dicari",
                        progress = 0.6f,
                        isCompleted = false
                    ),
                    Job(
                        id = "3",
                        title = "Bantu pasang lampu taman",
                        category = "Perbaikan",
                        shortDescription = "Pasang 3 lampu taman di halaman rumah. Tangga sudah disediakan.",
                        price = "Rp 40.000",
                        status = "Menunggu Konfirmasi",
                        progress = 0.2f,
                        isCompleted = false
                    ),
                    Job(
                        id = "4",
                        title = "Bersihkan halaman rumah",
                        category = "Cleaning",
                        shortDescription = "Bersihkan halaman depan dan belakang, sapu dan pel.",
                        price = "Rp 80.000",
                        status = "Hampir Selesai",
                        progress = 0.75f,
                        isCompleted = false
                    ),
                    Job(
                        id = "5",
                        title = "Cuci sprei dan selimut",
                        category = "Laundry",
                        shortDescription = "Cuci 2 set sprei king size dan 3 selimut tebal.",
                        price = "Rp 35.000",
                        status = "Sedang Proses",
                        progress = 0.45f,
                        isCompleted = false
                    )
                )
                
                // Sample Completed Jobs
                val sampleCompletedJobs = listOf(
                    Job(
                        id = "c1",
                        title = "Titip beli obat flu",
                        category = "Titip Beli",
                        shortDescription = "Titip beli obat flu dan vitamin di apotek depan komplek.",
                        price = "Rp 30.000",
                        status = "Selesai",
                        progress = 1f,
                        isCompleted = true,
                        completedDate = "2 hari lalu",
                        rating = 5.0f
                    ),
                    Job(
                        id = "c2",
                        title = "Pindah furniture",
                        category = "Angkut Barang",
                        shortDescription = "Pindahkan lemari, meja, dan kursi dari kamar ke ruang tamu.",
                        price = "Rp 100.000",
                        status = "Selesai",
                        progress = 1f,
                        isCompleted = true,
                        completedDate = "1 minggu lalu",
                        rating = 4.8f
                    ),
                    Job(
                        id = "c3",
                        title = "Service AC kamar",
                        category = "Perbaikan",
                        shortDescription = "Service AC yang tidak dingin, lengkap dengan cuci filter.",
                        price = "Rp 120.000",
                        status = "Selesai",
                        progress = 1f,
                        isCompleted = true,
                        completedDate = "3 hari lalu",
                        rating = 5.0f
                    ),
                    Job(
                        id = "c4",
                        title = "Cuci motor",
                        category = "Cleaning",
                        shortDescription = "Cuci motor sampai bersih, termasuk bagian dalam.",
                        price = "Rp 20.000",
                        status = "Selesai",
                        progress = 1f,
                        isCompleted = true,
                        completedDate = "1 hari lalu",
                        rating = 4.5f
                    ),
                    Job(
                        id = "c5",
                        title = "Masak untuk acara keluarga",
                        category = "Masak",
                        shortDescription = "Masak nasi goreng, ayam goreng, dan sayur untuk 20 orang.",
                        price = "Rp 200.000",
                        status = "Selesai",
                        progress = 1f,
                        isCompleted = true,
                        completedDate = "5 hari lalu",
                        rating = 4.9f
                    )
                )
                
                _uiState.update {
                    it.copy(
                        ongoingJobs = sampleOngoingJobs,
                        completedJobs = sampleCompletedJobs,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat job"
                    )
                }
            }
        }
    }
    
    fun refreshJobs() {
        loadJobs()
    }
    
    fun updateJobProgress(jobId: String, newProgress: Float) {
        _uiState.update { currentState ->
            val updatedOngoing = currentState.ongoingJobs.map { job ->
                if (job.id == jobId) {
                    job.copy(progress = newProgress)
                } else {
                    job
                }
            }
            currentState.copy(ongoingJobs = updatedOngoing)
        }
    }
}
