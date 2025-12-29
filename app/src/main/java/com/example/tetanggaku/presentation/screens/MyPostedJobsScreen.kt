package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.viewmodels.*

private val TealPrimary = Color(0xFF2D7A7A)
private val BackgroundColor = Color(0xFFF9FAFB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostedJobsScreen(
    onBack: () -> Unit,
    onJobClick: (String) -> Unit,
    onHelperClick: (String) -> Unit = {},
    viewModel: RequesterJobViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf<RequesterJob?>(null) }
    var showRateDialog by remember { mutableStateOf<HelperInfo?>(null) }
    var selectedJobForRating by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Job Saya",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Selector
            TabSelector(
                selectedFilter = uiState.activeFilter,
                onFilterChange = { viewModel.setFilter(it) }
            )

            // Content
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = TealPrimary)
                }
            } else {
                val filteredJobs = viewModel.getFilteredJobs()
                
                if (filteredJobs.isEmpty()) {
                    EmptyState(filter = uiState.activeFilter)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredJobs) { job ->
                            RequesterJobCard(
                                job = job,
                                onClick = { onJobClick(job.id) },
                                onConfirm = {
                                    if (job.needsConfirmation) {
                                        showConfirmDialog = job
                                    }
                                },
                                onRate = {
                                    job.helper?.let { helper ->
                                        selectedJobForRating = job.id
                                        showRateDialog = helper
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Dialogs
        showConfirmDialog?.let { job ->
            JobConfirmationDialog(
                job = job,
                onConfirm = {
                    viewModel.confirmJobCompletion(job.id)
                    showConfirmDialog = null
                    // Show rating dialog after confirmation
                    job.helper?.let { helper ->
                        selectedJobForRating = job.id
                        showRateDialog = helper
                    }
                },
                onReject = { reason ->
                    viewModel.rejectCompletion(job.id, reason)
                    showConfirmDialog = null
                },
                onDismiss = { showConfirmDialog = null }
            )
        }

        showRateDialog?.let { helper ->
            RateHelperDialog(
                helper = helper,
                onSubmit = { rating, review ->
                    // TODO: Submit rating
                    showRateDialog = null
                    selectedJobForRating = null
                },
                onDismiss = {
                    showRateDialog = null
                    selectedJobForRating = null
                }
            )
        }
    }
}

@Composable
private fun TabSelector(
    selectedFilter: JobFilter,
    onFilterChange: (JobFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            text = "Active",
            isSelected = selectedFilter == JobFilter.ACTIVE,
            onClick = { onFilterChange(JobFilter.ACTIVE) },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            text = "History",
            isSelected = selectedFilter == JobFilter.HISTORY,
            onClick = { onFilterChange(JobFilter.HISTORY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) TealPrimary else Color(0xFFF3F4F6))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color(0xFF6B7280),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun RequesterJobCard(
    job: RequesterJob,
    onClick: () -> Unit,
    onConfirm: () -> Unit,
    onRate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.category,
                    fontSize = 12.sp,
                    color = TealPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                StatusBadge(status = job.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = job.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = job.price,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6366F1)
            )

            // Helper Info (if assigned)
            job.helper?.let { helper ->
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFBFDBFE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = helper.name.first().toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D4ED8)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = helper.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${helper.rating}",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }

            // Action Buttons
            if (job.needsConfirmation) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6B7280)
                        )
                    ) {
                        Text("Lihat", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22C55E)
                        )
                    ) {
                        Text("Konfirmasi", fontSize = 13.sp)
                    }
                }
            } else if (job.status == RequesterJobStatus.COMPLETED && job.helper != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onRate,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TealPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Beri Rating", fontSize = 13.sp)
                }
            } else if (job.status == RequesterJobStatus.OPEN) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${job.applicantsCount} applicants",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: RequesterJobStatus) {
    val (text, bgColor, textColor) = when (status) {
        RequesterJobStatus.OPEN -> Triple("Open", Color(0xFFDEF7EC), Color(0xFF03543F))
        RequesterJobStatus.IN_PROGRESS -> Triple("In Progress", Color(0xFFEBF5FF), Color(0xFF1E429F))
        RequesterJobStatus.WAITING_REVIEW -> Triple("Review", Color(0xFFFEF3C7), Color(0xFF92400E))
        RequesterJobStatus.COMPLETED -> Triple("Completed", Color(0xFFDEF7EC), Color(0xFF03543F))
        RequesterJobStatus.CANCELLED -> Triple("Cancelled", Color(0xFFFDE8E8), Color(0xFF9B1C1C))
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun EmptyState(filter: JobFilter) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (filter == JobFilter.ACTIVE) Icons.Filled.Info else Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color(0xFFD1D5DB),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (filter == JobFilter.ACTIVE) 
                    "Belum ada job aktif" 
                else 
                    "Belum ada riwayat job",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (filter == JobFilter.ACTIVE)
                    "Buat job baru untuk mulai"
                else
                    "Job yang selesai akan muncul di sini",
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

// Job Confirmation Dialog
@Composable
fun JobConfirmationDialog(
    job: RequesterJob,
    onConfirm: () -> Unit,
    onReject: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }

    if (showRejectDialog) {
        RejectDialog(
            onReject = onReject,
            onDismiss = { showRejectDialog = false }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Konfirmasi Pekerjaan Selesai?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("Job: ${job.title}")
                    Spacer(modifier = Modifier.height(4.dp))
                    job.helper?.let {
                        Text("Helper: ${it.name}")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Upah: ${job.price}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6366F1)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚠️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Pastikan pekerjaan sudah benar-benar selesai!",
                                fontSize = 12.sp,
                                color = Color(0xFF92400E),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E)
                    )
                ) {
                    Text("✓ Konfirmasi")
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { showRejectDialog = true }) {
                        Text("Belum Selesai", color = Color(0xFFEF4444))
                    }
                    TextButton(onClick = onDismiss) {
                        Text("Tutup")
                    }
                }
            }
        )
    }
}

@Composable
private fun RejectDialog(
    onReject: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kenapa Belum Selesai?", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    "Beri tahu helper kenapa pekerjaan belum bisa dikonfirmasi:",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    placeholder = { Text("Contoh: Masih ada yang kurang...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (reason.isNotBlank()) onReject(reason) },
                enabled = reason.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                )
            ) {
                Text("Kirim")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// Rate Helper Dialog
@Composable
fun RateHelperDialog(
    helper: HelperInfo,
    onSubmit: (Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var review by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBFDBFE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = helper.name.first().toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1D4ED8)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Beri Rating untuk ${helper.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    "Bagaimana kinerjanya?",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Star Rating
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFBBF24) else Color(0xFFD1D5DB),
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { rating = index + 1 }
                                .padding(2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Review Text
                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    placeholder = { Text("Tulis review (opsional)", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                if (rating > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (rating) {
                            1 -> "😞 Kurang"
                            2 -> "😐 Cukup"
                            3 -> "🙂 Baik"
                            4 -> "😊 Sangat Baik"
                            5 -> "🤩 Luar Biasa!"
                            else -> ""
                        },
                        fontSize = 13.sp,
                        color = TealPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, review) },
                enabled = rating > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary
                )
            ) {
                Text("Kirim Rating")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Lewati")
            }
        }
    )
}
