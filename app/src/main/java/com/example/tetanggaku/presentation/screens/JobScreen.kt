package com.example.tetanggaku.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.viewmodels.*

// Teal color palette
private val TealPrimary = Color(0xFF2D7A7A)
private val TealLight = Color(0xFFE0F2F1)
private val SuccessGreen = Color(0xFF10B981)
private val LightGray = Color(0xFFF3F4F6)
private val DarkGray = Color(0xFF6B7280)

/**
 * JobScreen with category toggle (Ongoing vs Completed)
 */
@Composable
fun JobScreen(
    modifier: Modifier = Modifier,
    onJobClick: (Job) -> Unit,
    viewModel: JobViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Text(
                text = "My Jobs",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Lihat progress permintaan bantuanmu",
                fontSize = 14.sp,
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Category Toggle
            CategoryToggle(
                selectedCategory = uiState.selectedCategory,
                onCategoryChange = { viewModel.selectCategory(it) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Job List based on selected category
            val jobsToDisplay = when (uiState.selectedCategory) {
                JobCategory.ONGOING -> uiState.ongoingJobs
                JobCategory.COMPLETED -> uiState.completedJobs
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = TealPrimary)
                }
            } else if (jobsToDisplay.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = if (uiState.selectedCategory == JobCategory.ONGOING) {
                                "Tidak ada job yang sedang berlangsung"
                            } else {
                                "Belum ada job yang selesai"
                            },
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(jobsToDisplay) { job ->
                        JobCard(
                            job = job,
                            isCompleted = job.isCompleted,
                            onClick = { onJobClick(job) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Segmented control for category selection
 */
@Composable
private fun CategoryToggle(
    selectedCategory: JobCategory,
    onCategoryChange: (JobCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightGray)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ToggleButton(
            text = "Sedang Berlangsung",
            isSelected = selectedCategory == JobCategory.ONGOING,
            onClick = { onCategoryChange(JobCategory.ONGOING) },
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        ToggleButton(
            text = "Sudah Selesai",
            isSelected = selectedCategory == JobCategory.COMPLETED,
            onClick = { onCategoryChange(JobCategory.COMPLETED) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) TealPrimary else Color.Transparent,
        label = "bg color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else DarkGray,
        label = "text color"
    )
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor
        )
    }
}

/**
 * Job Card - Different UI for ongoing vs completed
 */
@Composable
private fun JobCard(
    job: Job,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Category
            Text(
                text = job.category,
                fontSize = 12.sp,
                color = TealPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Title
            Text(
                text = job.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = job.shortDescription,
                fontSize = 13.sp,
                color = DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isCompleted) {
                // Completed job UI
                CompletedJobInfo(job)
            } else {
                // Ongoing job UI
                OngoingJobProgress(job)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row: Price & Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = "Lihat detail ›",
                    fontSize = 13.sp,
                    color = TealPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Progress indicator for ongoing jobs
 */
@Composable
private fun OngoingJobProgress(job: Job) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progress",
                fontSize = 12.sp,
                color = DarkGray
            )

            // Status badge
            StatusBadge(status = job.status)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = job.progress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            trackColor = Color(0xFFE5E7EB),
            color = TealPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${(job.progress * 100).toInt()}% selesai",
            fontSize = 11.sp,
            color = DarkGray
        )
    }
}

/**
 * Completion info for completed jobs
 */
@Composable
private fun CompletedJobInfo(job: Job) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Completed badge with checkmark
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Completed",
                tint = SuccessGreen,
                modifier = Modifier.size(20.dp)
            )
            
            Column {
                Text(
                    text = "Selesai",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SuccessGreen
                )
                
                job.completedDate?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = DarkGray
                    )
                }
            }
        }

        // Rating stars
        job.rating?.let { rating ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = String.format("%.1f", rating),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }
        }
    }
}

/**
 * Status badge for ongoing jobs
 */
@Composable
private fun StatusBadge(status: String) {
    val (bgColor, textColor) = when {
        status.contains("Selesai", ignoreCase = true) ->
            Color(0xFFD1FAE5) to SuccessGreen
        status.contains("Proses", ignoreCase = true) ||
        status.contains("Hampir", ignoreCase = true) ->
            Color(0xFFFEF3C7) to Color(0xFF92400E)
        else ->
            LightGray to DarkGray
    }

    Box(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}