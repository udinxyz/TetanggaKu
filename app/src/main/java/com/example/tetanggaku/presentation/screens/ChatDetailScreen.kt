package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
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
import com.example.tetanggaku.presentation.components.BottomNavigationBar
import com.example.tetanggaku.presentation.viewmodels.HomeTab

// Purple accent color from design
private val PurpleAccent = Color(0xFF7C3AED)
private val LightGray = Color(0xFFF3F4F6)
private val MessageGray = Color(0xFFE5E7EB)

data class ChatMessage(
    val id: String,
    val text: String,
    val timestamp: String,
    val isSent: Boolean,
    val isVoice: Boolean = false,
    val voiceDuration: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    userName: String = "Fahime Rafiei",
    userStatus: String = "Last online at 10:45 PM",
    selectedTab: HomeTab = HomeTab.CHAT,
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    
    // Sample messages
    val messages = remember {
        listOf(
            ChatMessage(
                id = "1",
                text = "Hello Fahime! How was the class?",
                timestamp = "10:33 PM",
                isSent = true
            ),
            ChatMessage(
                id = "2",
                text = "Hi Ehaz! It was so boring!",
                timestamp = "10:45 PM",
                isSent = false
            ),
            ChatMessage(
                id = "3",
                text = "",
                timestamp = "10:33 PM",
                isSent = true,
                isVoice = true,
                voiceDuration = "00:20"
            ),
            ChatMessage(
                id = "4",
                text = "Oh! I was sure about that! Because Mr. Smith is terrible on teaching.",
                timestamp = "10:45 PM",
                isSent = false
            )
        )
    }
    
    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F2937)
                        )
                    }
                    
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Avatar",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    // Name and status
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = userStatus,
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column {
                // Message input
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { /* TODO: Attach */ }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Attach",
                                tint = Color(0xFF6B7280)
                            )
                        }
                        
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            color = LightGray
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (messageText.isEmpty()) "Message..." else messageText,
                                    color = Color(0xFF9CA3AF),
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        
                        IconButton(onClick = { /* TODO: Emoji */ }) {
                            Icon(
                                imageVector = Icons.Filled.Face,
                                contentDescription = "Emoji",
                                tint = Color(0xFF6B7280)
                            )
                        }
                        
                        IconButton(onClick = { /* TODO: Voice */ }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Voice",
                                tint = Color(0xFF6B7280)
                            )
                        }
                    }
                }
                
                // Bottom navigation
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onHomeClick = onHomeClick,
                    onJobsClick = onJobsClick,
                    onChatClick = onChatClick,
                    onProfileClick = onProfileClick
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (message.isSent) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (message.isVoice) {
                // Voice message
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (message.isSent) PurpleAccent else MessageGray,
                    modifier = Modifier.widthIn(max = 280.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play",
                            tint = if (message.isSent) Color.White else Color(0xFF1F2937),
                            modifier = Modifier.size(24.dp)
                        )
                        
                        // Waveform placeholder
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(24.dp)
                                .background(
                                    if (message.isSent) Color.White.copy(alpha = 0.3f) 
                                    else Color(0xFF9CA3AF).copy(alpha = 0.3f),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        
                        Text(
                            text = message.voiceDuration ?: "00:00",
                            fontSize = 12.sp,
                            color = if (message.isSent) Color.White else Color(0xFF6B7280)
                        )
                    }
                }
            } else {
                // Text message
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (message.isSent) PurpleAccent else MessageGray,
                    modifier = Modifier.widthIn(max = 280.dp)
                ) {
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        color = if (message.isSent) Color.White else Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
            
            // Timestamp and check marks
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (message.isSent) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Sent",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = message.timestamp,
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}
