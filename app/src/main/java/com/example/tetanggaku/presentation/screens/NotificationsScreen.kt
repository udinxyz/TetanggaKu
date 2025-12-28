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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun NotificationsScreen(
    selectedTab: HomeTab = HomeTab.HOME,
    onBack: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: NotificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (uiState.unreadCount > 0) {
                        TextButton(onClick = { viewModel.markAllAsRead() }) {
                            Text("Mark all read", color = Color.White)
                        }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF9FAFB))
        ) {
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
                uiState.notifications.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.Notifications,
                                null,
                                Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(Modifier.height(16.dp))
                            Text("No notifications", fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                }
                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(uiState.notifications) { notification ->
                            NotificationCard(
                                notification = notification,
                                onClick = { viewModel.markAsRead(notification.id) },
                                onDelete = { viewModel.deleteNotification(notification.id) }
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color(0xFFEFF6FF)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon based on type
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getNotificationColor(notification.type).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = null,
                    tint = getNotificationColor(notification.type),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Column(Modifier.weight(1f)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        notification.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        notification.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    notification.message,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                if (!notification.isRead) {
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TealPrimary)
                    )
                }
            }
        }
    }
}

private fun getNotificationIcon(type: NotificationType) = when (type) {
    NotificationType.JOB_UPDATE -> Icons.Filled.Settings
    NotificationType.CHAT_MESSAGE -> Icons.Filled.Email
    NotificationType.SYSTEM -> Icons.Filled.Info
    else -> Icons.Filled.Notifications
}

private fun getNotificationColor(type: NotificationType) = when (type) {
    NotificationType.JOB_UPDATE -> Color(0xFF2D7A7A)
    NotificationType.CHAT_MESSAGE -> Color(0xFF7C3AED)
    NotificationType.SYSTEM -> Color(0xFF3B82F6)
    else -> Color.Gray
}
