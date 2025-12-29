package com.example.tetanggaku.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.R
import com.example.tetanggaku.presentation.components.BottomNavigationBar
import com.example.tetanggaku.presentation.viewmodels.*

// Teal color palette matching AFTER design (darker, more saturated)
private val TealPrimary = Color(0xFF1D5F5F)  // Darker teal for header
private val TealLight = Color(0xFF2D7A7A)
private val TealDark = Color(0xFF0F3F3F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onJobDetailClick: () -> Unit,
    onCreateJobClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onAiClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAiClick,
                containerColor = TealPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "AI",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = uiState.selectedTab,
                onHomeClick = { viewModel.selectTab(HomeTab.HOME) },
                onJobsClick = { viewModel.selectTab(HomeTab.JOBS) },
                onChatClick = {
                    viewModel.selectTab(HomeTab.CHAT)
                    onChatClick()
                },
                onProfileClick = {
                    viewModel.selectTab(HomeTab.PROFILE)
                    onProfileClick()
                }
            )
        }
    ) { innerPadding ->
        when (uiState.selectedTab) {
            HomeTab.HOME -> {
                HomeBerandaContent(
                    modifier = Modifier.padding(innerPadding),
                    uiState = uiState,
                    onCreateJobClick = onCreateJobClick,
                    onJobDetailClick = onJobDetailClick,
                    onSearchClick = onSearchClick,
                    onLocationClick = onLocationClick,
                    onNotificationClick = onNotificationClick,
                    onCategoryClick = onCategoryClick,
                    onRetry = { viewModel.loadAvailableJobs() }
                )
            }

            HomeTab.JOBS -> {
                JobScreen(
                    modifier = Modifier.padding(innerPadding),
                    onJobClick = { job -> onJobDetailClick() }
                )
            }

            HomeTab.CHAT -> {
                // Chat navigation handled by onClick
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            HomeTab.PROFILE -> {
                // Profile navigation handled by onClick
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }

    // AI Bottom Sheet
    if (uiState.showAiSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideAiSheet() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tanya Tetangga AI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Tulis masalah atau pekerjaan yang ingin kamu tanyakan. AI akan bantu kasih solusi cepat.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = uiState.aiQuestion,
                    onValueChange = { viewModel.updateAiQuestion(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    placeholder = {
                        Text("Contoh: Cara aman angkat lemari ke lantai 2 gimana?")
                    },
                    enabled = !uiState.aiLoading
                )

                Button(
                    onClick = { viewModel.askAI() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.aiQuestion.isNotBlank() && !uiState.aiLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.aiLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Dapatkan Solusi")
                    }
                }

                if (uiState.aiAnswer.isNotBlank()) {
                    HorizontalDivider()
                    Text(
                        text = "Jawaban AI:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = uiState.aiAnswer,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// =====================
// Konten tab Beranda dengan Design Baru
// =====================
@Composable
private fun HomeBerandaContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onCreateJobClick: () -> Unit,
    onJobDetailClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLocationClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Dark Teal Header
        TealHeader(
            currentLocation = uiState.currentLocation,
            unreadCount = uiState.unreadNotificationCount,
            onLocationClick = onLocationClick,
            onNotificationClick = onNotificationClick
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // Search Bar
            SearchBar(onClick = onSearchClick)

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Banner (Teal)
            HeroBanner(onCreateJobClick = onCreateJobClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Missions Section
            DailyMissionsSection(
                missions = uiState.dailyMissions,
                onMissionClick = { /* Can handle detailed view */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Service Categories Section
            ServiceCategoriesSection(
                categories = uiState.serviceCategories,
                onCategoryClick = onCategoryClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Popular Services Section
            PopularServicesSection(
                jobListState = uiState.availableJobs,
                onJobDetailClick = onJobDetailClick,
                onRetry = onRetry
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// =====================
// Teal Header Component
// =====================
@Composable
private fun TealHeader(
    currentLocation: String = "San Antonio, TX",
    unreadCount: Int = 0,
    onLocationClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = TealPrimary,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    modifier = Modifier.clickable { onLocationClick() }
                ) {
                    Text(
                        text = "Your Location",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentLocation,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Notification & Profile
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Notification Bell
                Box {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .offset(x = 20.dp, y = 8.dp)
                                .clip(CircleShape)
                                .background(Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Profile Photo
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { /* TODO: Profile */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// =====================
// Search Bar Component
// =====================
@Composable
private fun SearchBar(onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search for a service...",
                color = Color(0xFF9CA3AF),
                fontSize = 14.sp
            )
        }
    }
}

// =====================
// Hero Banner (Teal Theme)
// =====================
@Composable
private fun HeroBanner(onCreateJobClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = TealPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "YOUR SOLUTION,\nONE TAP AWAY!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 22.sp
                )
                Text(
                    text = "Services, Fast & Reliable\nServices At Your Fingertips",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Button(
                    onClick = onCreateJobClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = TealPrimary
                    )
                ) {
                    Text("Explore", fontWeight = FontWeight.SemiBold)
                }
            }

            // Placeholder for isometric illustration
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TealLight.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Service",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

// =====================
// Service Categories Section
// =====================
@Composable
private fun ServiceCategoriesSection(
    categories: List<ServiceCategory>,
    onCategoryClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Service Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = "View all ›",
                fontSize = 14.sp,
                color = TealPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid layout for categories (2x2)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Take only first 4 categories for 2x2 grid
            val displayCategories = categories.take(4)
            
            // First row
            if (displayCategories.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    displayCategories.getOrNull(0)?.let { category ->
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCardGrid(
                                category = category,
                                onClick = { onCategoryClick(category.id) }
                            )
                        }
                    }
                    displayCategories.getOrNull(1)?.let { category ->
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCardGrid(
                                category = category,
                                onClick = { onCategoryClick(category.id) }
                            )
                        }
                    }
                }
            }
            
            // Second row
            if (displayCategories.size > 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    displayCategories.getOrNull(2)?.let { category ->
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCardGrid(
                                category = category,
                                onClick = { onCategoryClick(category.id) }
                            )
                        }
                    }
                    displayCategories.getOrNull(3)?.let { category ->
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCardGrid(
                                category = category,
                                onClick = { onCategoryClick(category.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// =====================
// Category Card Component
// =====================
@Composable
private fun CategoryCard(category: ServiceCategory) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { /* TODO */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(TealPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category.iconName),
                    contentDescription = category.name,
                    tint = TealPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = category.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =====================
// Category Card Grid Component (For 2x2 Layout)
// =====================
@Composable
private fun CategoryCardGrid(
    category: ServiceCategory,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TealPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category.iconName),
                    contentDescription = category.name,
                    tint = TealPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Helper function to get icon based on name
@Composable
private fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "scissors" -> Icons.Filled.Settings // Approximate
        " cleaning" -> Icons.Filled.Build
        "painting" -> Icons.Filled.Edit
        "cooking" -> Icons.Filled.Star
        else -> Icons.Filled.Star
    }
}

// =====================
// Popular Services Section
// =====================
@Composable
private fun PopularServicesSection(
    jobListState: JobListState,
    onJobDetailClick: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Services",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = "View all ›",
                fontSize = 14.sp,
                color = TealPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Handle different states
        when (jobListState) {
            is JobListState.Loading -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(3) {
                        ServiceCardLoading()
                    }
                }
            }
            is JobListState.Success -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(jobListState.jobs) { job ->
                        ServiceCard(job = job, onClick = onJobDetailClick)
                    }
                }
            }
            is JobListState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No services available",
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp
                    )
                }
            }
            is JobListState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = jobListState.message,
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetry) {
                        Text("Retry", color = TealPrimary)
                    }
                }
            }
        }
    }
}

// =====================
// Service Card Component (Horizontal)
// =====================
@Composable
private fun ServiceCard(job: JobItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(TealLight.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = job.title,
                    tint = TealPrimary.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = job.rating.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "(${job.reviewCount} Reviews)",
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                // Title
                Text(
                    text = job.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Price
                Text(
                    text = job.price,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                )
            }
        }
    }
}

// =====================
// Service Card Loading State
// =====================
@Composable
private fun ServiceCardLoading() {
    Card(
        modifier = Modifier.width(180.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFE0E0E0))
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(14.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

// =====================
// Job Saya Content (Keep existing or simplify)
// =====================
@Composable
private fun HomeJobSayaContent(
    modifier: Modifier = Modifier,
    jobListState: JobListState,
    onJobDetailClick: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Job Saya",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (jobListState) {
            is JobListState.Loading -> {
                repeat(2) {
                    ServiceCardLoading()
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            is JobListState.Success -> {
                jobListState.jobs.forEach { job ->
                    ServiceCard(job = job, onClick = onJobDetailClick)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            is JobListState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada job yang kamu buat",
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center
                    )
                }
            }
            is JobListState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = jobListState.message,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

// =====================
// Daily Missions Section
// =====================
@Composable
private fun DailyMissionsSection(
    missions: List<DailyMission>,
    onMissionClick: (DailyMission) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Misi Hari Ini",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal scroll for missions
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(missions) { mission ->
                DailyMissionItem(mission = mission)
            }
        }
    }
}

@Composable
private fun DailyMissionItem(mission: DailyMission) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (mission.isCompleted) BorderStroke(1.dp, Color(0xFF4ADE80)) else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // XP Badge
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFEFF6FF)
                ) {
                    Text(
                        text = "+${mission.xpReward} XP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                if (mission.isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = mission.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (mission.isCompleted) Color(0xFF9CA3AF) else Color(0xFF1F2937),
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
