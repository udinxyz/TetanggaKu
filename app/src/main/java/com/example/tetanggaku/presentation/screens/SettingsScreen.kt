package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notification Settings
            SettingsCard(
                title = "Notifikasi",
                items = listOf(
                    SettingItem(
                        icon = Icons.Filled.Notifications,
                        label = "Notifikasi Push",
                        value = if (uiState.notificationsEnabled) "Aktif" else "Nonaktif",
                        enabled = uiState.notificationsEnabled
                    )
                )
            )
            
            // Language Settings
            SettingsCard(
                title = "Bahasa & Tampilan",
                items = listOf(
                    SettingItem(
                        icon = Icons.Filled.AccountCircle,
                        label = "Bahasa",
                        value = uiState.selectedLanguage
                    ),
                    SettingItem(
                        icon = Icons.Filled.Info,
                        label = "Tema",
                        value = uiState.selectedTheme
                    )
                )
            )
            
            // App Info
            SettingsCard(
                title = "Informasi Aplikasi",
                items = listOf(
                    SettingItem(
                        icon = Icons.Filled.Info,
                        label = "Versi Aplikasi",
                        value = uiState.appVersion
                    )
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Note
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFF6FF)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color(0xFF5B8DEF),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Pengaturan ini hanya untuk ditampilkan. Fitur interaktif akan datang segera.",
                        fontSize = 12.sp,
                        color = Color(0xFF5B8DEF),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

data class SettingItem(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val enabled: Boolean = true
)

@Composable
private fun SettingsCard(
    title: String,
    items: List<SettingItem>
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingItemRow(item)
                    if (index < items.size - 1) {
                        Divider(
                            color = Color(0xFFF3F4F6),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingItemRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFE0EAFF)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = Color(0xFF5B8DEF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = item.label,
                fontSize = 15.sp,
                color = Color(0xFF1F2937),
                fontWeight = FontWeight.Normal
            )
        }
        
        Text(
            text = item.value,
            fontSize = 14.sp,
            color = if (item.enabled) Color(0xFF5B8DEF) else Color(0xFF9CA3AF),
            fontWeight = FontWeight.Medium
        )
    }
}
