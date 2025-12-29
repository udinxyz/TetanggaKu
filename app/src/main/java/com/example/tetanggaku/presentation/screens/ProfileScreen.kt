package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.ui.theme.TetanggakuTheme
import com.example.tetanggaku.R
import com.example.tetanggaku.presentation.components.BottomNavigationBar
import com.example.tetanggaku.presentation.components.EditProfileDialog
import com.example.tetanggaku.presentation.components.ProfileDrawerContent
import com.example.tetanggaku.presentation.viewmodels.ProfileViewModel
import com.example.tetanggaku.presentation.viewmodels.HomeTab
import com.example.tetanggaku.presentation.viewmodels.Badge
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState

@Composable
fun ProfileScreen(
    selectedTab: HomeTab = HomeTab.PROFILE,
    onHomeClick: () -> Unit,
    onJobClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onMyProfileClick: () -> Unit = {},
    onMessagesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onMyPostedJobsClick: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTabState = remember { mutableStateOf(HomeTab.PROFILE) }
    val xpProgress = viewModel.getXpProgress()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ProfileDrawerContent(
                userName = uiState.userName,
                userEmail = uiState.userEmail,
                userTitle = uiState.userTitle,
                onHomeClick = onHomeClick,
                onMyJobsClick = onMyPostedJobsClick,
                onSettingsClick = onSettingsClick,
                onHelpClick = onTermsClick,
                onLogout = onLogout,
                onClose = { 
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Light gray background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
            // Blue gradient header with rounded bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Background blue gradient
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    color = Color(0xFF5B8DEF), // Blue color matching reference
                    shadowElevation = 0.dp,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                ) {
                    // Top bar inside blue header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { 
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "Edit",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            modifier = Modifier.clickable { viewModel.showEditDialog() }
                        )
                    }
                }
            }

            // Profile content with offset to overlap header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-100).dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile photo with green checkmark badge
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF4B6CB7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.foto_irfan),
                                contentDescription = "Profile Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Green checkmark badge
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4ADE80))
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name - uppercase
                Text(
                    text = uiState.userName.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Title (Gelar) - NEW
                Text(
                    text = uiState.userTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF5B8DEF)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Email
                Text(
                    text = uiState.userEmail,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats chips (jobs completed + rating)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatChip(
                        label = "Jobs selesai",
                        value = "${uiState.totalJobsCompleted}"
                    )
                    StatChip(
                        label = "Rating",
                        value = "%.1f★".format(uiState.neighborRating)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Gamification card (Level + XP)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 16.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Level ${uiState.level}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = "${uiState.currentXp} / ${uiState.nextLevelXp} XP",
                                    fontSize = 13.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }

                            // Badge pill
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFFDEEAFF)
                            ) {
                                Text(
                                    text = uiState.badge,
                                    color = Color(0xFF5B8DEF),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress bar
                        LinearProgressIndicator(
                            progress = xpProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(999.dp)),
                            trackColor = Color(0xFFE5E7EB),
                            color = Color(0xFF5B8DEF)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Selesaikan lebih banyak job untuk naik level",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Badges Section
                Text(
                    text = "Koleksi Lencana",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.badges.forEach { badge ->
                        BadgeItem(
                            badge = badge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Menu items in white card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // My Profile
                        MenuItemRow(
                            icon = Icons.Filled.Person,
                            title = "My Profile",
                            onClick = onMyProfileClick,
                            showArrow = true
                        )

                        Divider(color = Color(0xFFF3F4F6), thickness = 1.dp)

                        // Messages with badge
                        MenuItemRow(
                            icon = Icons.Filled.Email,
                            title = "Messages",
                            onClick = {
                                viewModel.markMessagesAsRead()
                                onMessagesClick()
                            },
                            badgeCount = uiState.unreadMessages,
                            showArrow = false
                        )

                        Divider(color = Color(0xFFF3F4F6), thickness = 1.dp)

                        // My Posted Jobs (Requester)
                        MenuItemRow(
                            icon = Icons.Filled.List,
                            title = "Job Saya (Pemberi)",
                            onClick = onMyPostedJobsClick,
                            showArrow = true
                        )

                        Divider(color = Color(0xFFF3F4F6), thickness = 1.dp)

                        // Settings
                        MenuItemRow(
                            icon = Icons.Filled.Settings,
                            title = "Settings",
                            onClick = onSettingsClick,
                            showArrow = true
                        )

                        Divider(color = Color(0xFFF3F4F6), thickness = 1.dp)

                        // Terms & Privacy Policy
                        MenuItemRow(
                            icon = Icons.Filled.Info,
                            title = "Terms & Privacy Policy",
                            onClick = onTermsClick,
                            showArrow = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { onLogout() }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        color = Color(0xFF9CA3AF),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(100.dp)) // Space for bottom nav
            }
        }

        // Bottom navigation - unified component
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedTab = selectedTab,
            onHomeClick = onHomeClick,
            onJobsClick = onJobClick,
            onChatClick = onChatClick,
            onProfileClick = { /* Already on profile */ }
        )
        }
    }
    
    // Edit Profile Dialog
    if (uiState.showEditDialog) {
        EditProfileDialog(
            currentName = uiState.userName,
            currentEmail = uiState.userEmail,
            isUpdating = uiState.isUpdatingProfile,
            onDismiss = { viewModel.hideEditDialog() },
            onSave = { name, email ->
                viewModel.updateUserName(name)
                viewModel.updateUserEmail(email)
            }
        )
    }
}

/**
 * Menu item row component
 */
@Composable
private fun MenuItemRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    badgeCount: Int = 0,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with light blue background
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE0EAFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF5B8DEF),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = title,
            fontSize = 15.sp,
            color = Color(0xFF1F2937),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )

        // Badge or arrow
        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else if (showArrow) {
            Text(
                text = "›",
                color = Color(0xFF9CA3AF),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun StatChip(
    label: String,
    value: String
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.9f),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    badgeCount: Int? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEFF4FF)
        ) {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        if (badgeCount != null && badgeCount > 0) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF22C55E))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
private fun BottomNavItemProfile(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFF6366F1) else Color(0xFF9CA3AF)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (selected) Color(0xFF6366F1) else Color(0xFF9CA3AF)
        )
    }
}

@Preview(
    name = "Profile Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenPreview() {
    TetanggakuTheme {
        ProfileScreen(
            onHomeClick = {},
            onJobClick = {},
            onLogout = {},
            onMyProfileClick = {},
            onMessagesClick = {},
            onSettingsClick = {},
            onTermsClick = {}
        )
    }
}

@Composable
private fun BadgeItem(
    badge: Badge,
    modifier: Modifier = Modifier
) {
    val icon = when (badge.iconName) {
        "home" -> Icons.Filled.Home
        "rocket" -> Icons.Filled.ThumbUp // Replacement for Rocket
        "star" -> Icons.Filled.Star
        "heart" -> Icons.Filled.Favorite
        "wallet" -> Icons.Filled.ShoppingCart // Replacement for Wallet
        else -> Icons.Filled.Star
    }
    
    val backgroundColor = if (badge.isUnlocked) Color(0xFFEFF6FF) else Color(0xFFF3F4F6)
    val iconColor = if (badge.isUnlocked) Color(0xFF3B82F6) else Color(0xFF9CA3AF)
    val textColor = if (badge.isUnlocked) Color(0xFF1F2937) else Color(0xFF9CA3AF)
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = badge.name,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = badge.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


