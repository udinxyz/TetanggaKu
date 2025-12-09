package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data sederhana untuk 1 job milik user
 */
data class JobUiModel(
    val id: String,
    val title: String,
    val category: String,
    val shortDescription: String,
    val price: String,
    val status: String,      // contoh: "Menunggu tetangga", "Sedang dikerjakan", "Selesai"
    val progress: Float      // 0f..1f
)

/**
 * Daftar Job Saya
 * Job akan bisa di-klik, dan callback onJobClick dipanggil
 */
@Composable
fun JobScreen(
    modifier: Modifier = Modifier,
    jobs: List<JobUiModel> = sampleMyJobs,
    onJobClick: (JobUiModel) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            // Header kecil
            Text(
                text = "Job Saya",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Lihat progress permintaan bantuanmu.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List job
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(jobs) { job ->
                    JobCardItem(
                        job = job,
                        modifier = Modifier.clickable { onJobClick(job) }
                    )
                }
            }
        }
    }
}

/**
 * 1 card job di halaman "Job Saya"
 */
@Composable
private fun JobCardItem(
    job: JobUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            // Kategori
            Text(
                text = job.category,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6366F1),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Judul job
            Text(
                text = job.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Deskripsi singkat
            Text(
                text = job.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress + status
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Status text di kanan
                    StatusPill(text = job.status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = job.progress.coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    trackColor = Color(0xFFE5E7EB),
                    color = Color(0xFF4ADE80)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Harga di bawah
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = job.price,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Lihat detail",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6366F1),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Badge status kecil (Menunggu / Proses / Selesai)
 */
@Composable
private fun StatusPill(text: String) {
    val (bg, fg) = when {
        text.contains("Selesai", ignoreCase = true) ->
            Color(0xFFD1FAE5) to Color(0xFF059669)
        text.contains("Proses", ignoreCase = true) ||
                text.contains("dikerjakan", ignoreCase = true) ->
            Color(0xFFFEF3C7) to Color(0xFF92400E)
        else ->
            Color(0xFFE5E7EB) to Color(0xFF4B5563)
    }

    Box(
        modifier = Modifier
            .background(bg, shape = RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = fg,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Sample data sementara (hardcoded)
 * Nanti bisa diganti dari backend / database
 */
private val sampleMyJobs = listOf(
    JobUiModel(
        id = "1",
        title = "Bantu angkat lemari ke lantai 2",
        category = "Angkut",
        shortDescription = "Lemari kayu cukup berat, butuh 2 orang bantu angkat ke lantai atas.",
        price = "Rp50.000",
        status = "Sedang dikerjakan",
        progress = 0.5f
    ),
    JobUiModel(
        id = "2",
        title = "Titip beli obat flu di apotek",
        category = "Titip beli",
        shortDescription = "Titip beli obat flu di apotek dekat komplek, nanti diganti uangnya.",
        price = "Rp25.000",
        status = "Menunggu tetangga",
        progress = 0.2f
    ),
    JobUiModel(
        id = "3",
        title = "Bantu pasang lampu taman",
        category = "Perbaikan",
        shortDescription = "Pasang 3 lampu taman di halaman rumah. Tangga sudah disediakan.",
        price = "Rp40.000",
        status = "Selesai",
        progress = 1f
    )
)
