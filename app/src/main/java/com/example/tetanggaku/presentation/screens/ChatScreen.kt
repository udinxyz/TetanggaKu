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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.components.BottomNavigationBar
import com.example.tetanggaku.presentation.viewmodels.ChatViewModel
import com.example.tetanggaku.presentation.viewmodels.ConversationItem
import com.example.tetanggaku.presentation.viewmodels.HomeTab

// Purple accent color from design
private val PurpleAccent = Color(0xFF7C3AED)
private val LightGray = Color(0xFFF3F4F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    selectedTab: HomeTab = HomeTab.CHAT,
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChatDetailClick: (String) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ChatViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header with Messages title and compose icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Messages",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        
                        IconButton(
                            onClick = { /* TODO: New message */ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = "New message",
                                tint = Color(0xFF6B7280)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Search bar
                    SearchBar()
                }
            }
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.error != null -> {
                    ErrorState(
                        message = uiState.error!!,
                        onRetry = { viewModel.refreshConversations() }
                    )
                }
                uiState.conversations.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    ConversationList(
                        conversations = uiState.conversations,
                        onConversationClick = onChatDetailClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = LightGray
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search for messages or users",
                color = Color(0xFF9CA3AF),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ConversationList(
    conversations: List<ConversationItem>,
    onConversationClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(conversations) { conversation ->
            ConversationCard(
                conversation = conversation,
                onClick = { onConversationClick(conversation.id) }
            )
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: ConversationItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Avatar",
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = conversation.timestamp,
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(PurpleAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Read",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = PurpleAccent
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "No messages",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "No conversations yet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280)
            )
            Text(
                text = "Start chatting with your neighbors",
                fontSize = 14.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleAccent
                )
            ) {
                Text("Retry")
            }
        }
    }
}
