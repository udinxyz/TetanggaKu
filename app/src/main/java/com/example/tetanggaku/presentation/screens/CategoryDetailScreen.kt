package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.components.BottomNavigationBar
import com.example.tetanggaku.presentation.viewmodels.*

private val TealPrimary = Color(0xFF2D7A7A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryId: String,
    selectedTab: HomeTab = HomeTab.HOME,
    onBack: () -> Unit = {},
    onJobClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: CategoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(categoryId) {
        viewModel.loadCategory(categoryId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.category?.name ?: "Category") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onHomeClick = onHomeClick,
                onJobsClick = onJobsClick,
                onChatClick = onChatClick,
                onProfileClick = onProfileClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedFilter == FilterType.NONE,
                        onClick = { viewModel.applyFilter(FilterType.NONE) },
                        label = { Text("All") }
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.selectedFilter == FilterType.PRICE_LOW_TO_HIGH,
                        onClick = { viewModel.applyFilter(FilterType.PRICE_LOW_TO_HIGH) },
                        label = { Text("Price: Low to High") }
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.selectedFilter == FilterType.RATING,
                        onClick = { viewModel.applyFilter(FilterType.RATING) },
                        label = { Text("Top Rated") }
                    )
                }
            }
            
            // Services list
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TealPrimary)
                    }
                }
                uiState.error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(uiState.error!!, color = Color.Red)
                    }
                }
                uiState.services.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No services available", color = Color.Gray)
                    }
                }
                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(uiState.services) { job ->
                            ServiceCard(job, onClick = { onJobClick(job.id) })
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(job: JobItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(job.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(job.description, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
                }
                if (job.verified) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        "Verified",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    job.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TealPrimary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, null, Modifier.size(16.dp), tint = Color(0xFFFBBF24))
                    Text(" ${job.rating} (${job.reviewCount})", fontSize = 14.sp)
                }
            }
        }
    }
}
