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
import com.example.tetanggaku.ui.theme.TetanggakuTheme
import com.example.tetanggaku.R

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onJobClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    userName: String = "Udinxyz",
    userEmail: String = "udinxyz@example.com",
    totalJobsCompleted: Int = 12,
    neighborRating: Double = 4.8,
    level: Int = 3,
    currentXp: Int = 1250,
    nextLevelXp: Int = 2000,
    onMyProfileClick: () -> Unit = {},
    onMessagesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onTermsClick: () -> Unit = {}
) {
    val selectedTabState = remember { mutableStateOf(HomeTab.PROFIL) }
    val xpProgress =
        (currentXp.toFloat() / nextLevelXp.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F7))
    ) {
        // Header biru rounded
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            color = Color(0xFF3B82F6),
            shadowElevation = 0.dp,
            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
        ) {}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp) // space untuk bottom nav
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: open drawer */ }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Edit",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            // Avatar + nama + email + badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // AVATAR: pakai foto profil
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.foto_irfan),
                            contentDescription = "Foto Profil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Badge ceklis
                    Box(
                        modifier = Modifier
                            .offset(y = (-12).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Nama & email diberi padding horizontal biar tidak kepotong
                    Text(
                        text = userName.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                    )

                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Statistik gamifikasi singkat (jobs + rating)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatChip(
                            label = "Jobs selesai",
                            value = "$totalJobsCompleted"
                        )
                        StatChip(
                            label = "Rating tetangga",
                            value = "%.1f★".format(neighborRating)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CARD GAMIFIKASI (Level + XP)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 14.dp
                            )
                        ) {
                            Text(
                                text = "Progress Tetangga Baik",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = "Level $level",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "$currentXp / $nextLevelXp XP",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = Color(0xFFEEF2FF)
                                ) {
                                    Text(
                                        text = "Badge: Tetangga Aktif",
                                        color = Color(0xFF4F46E5),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = xpProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(999.dp)),
                                trackColor = Color(0xFFE5E7EB),
                                color = Color(0xFF4ADE80)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Selesaikan lebih banyak job untuk naik ke Level ${level + 1}.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Card menu utama
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Filled.Person,
                        iconTint = Color(0xFF3B82F6),
                        title = "My Profile",
                        badgeCount = null,
                        onClick = onMyProfileClick
                    )

                    Divider(color = Color(0xFFE5E7EB))

                    ProfileMenuItem(
                        icon = Icons.Filled.Email,
                        iconTint = Color(0xFF3B82F6),
                        title = "Messages",
                        badgeCount = 2,
                        onClick = onMessagesClick
                    )

                    Divider(color = Color(0xFFE5E7EB))

                    ProfileMenuItem(
                        icon = Icons.Filled.Settings,
                        iconTint = Color(0xFF3B82F6),
                        title = "Settings",
                        badgeCount = null,
                        onClick = onSettingsClick
                    )

                    Divider(color = Color(0xFFE5E7EB))

                    ProfileMenuItem(
                        icon = Icons.Filled.Info,
                        iconTint = Color(0xFF3B82F6),
                        title = "Terms & Privacy Policy",
                        badgeCount = null,
                        onClick = onTermsClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Logout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White)
                        .clickable { onLogout() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom navigation seperti di Home
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItemProfile(
                    icon = Icons.Filled.Home,
                    label = "Beranda",
                    selected = selectedTabState.value == HomeTab.BERANDA,
                    onClick = {
                        selectedTabState.value = HomeTab.BERANDA
                        onHomeClick()
                    }
                )

                BottomNavItemProfile(
                    icon = Icons.Filled.List,
                    label = "Job Saya",
                    selected = selectedTabState.value == HomeTab.JOB_SAYA,
                    onClick = {
                        selectedTabState.value = HomeTab.JOB_SAYA
                        onJobClick()
                    }
                )

                BottomNavItemProfile(
                    icon = Icons.Filled.Person,
                    label = "Profil",
                    selected = selectedTabState.value == HomeTab.PROFIL,
                    onClick = {
                        selectedTabState.value = HomeTab.PROFIL
                        // sudah di profil
                    }
                )
            }
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
            userName = "Irfan Zidni",
            userEmail = "irfan@example.com",
            totalJobsCompleted = 12,
            neighborRating = 4.8,
            level = 3,
            currentXp = 1250,
            nextLevelXp = 2000,
            onMyProfileClick = {},
            onMessagesClick = {},
            onSettingsClick = {},
            onTermsClick = {}
        )
    }
}

