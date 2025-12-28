package com.example.tetanggaku.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetanggaku.presentation.viewmodels.HomeTab

// Teal color scheme
private val TealPrimary = Color(0xFF2D7A7A)

/**
 * Unified bottom navigation bar component used across all main screens
 */
@Composable
fun BottomNavigationBar(
    selectedTab: HomeTab,
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Filled.Home,
                label = "Home",
                selected = selectedTab == HomeTab.HOME,
                onClick = onHomeClick
            )
            BottomNavItem(
                icon = Icons.Filled.DateRange,
                label = "Jobs",
                selected = selectedTab == HomeTab.JOBS,
                onClick = onJobsClick
            )
            BottomNavItem(
                icon = Icons.Filled.Email,
                label = "Chat",
                selected = selectedTab == HomeTab.CHAT,
                onClick = onChatClick
            )
            BottomNavItem(
                icon = Icons.Filled.Person,
                label = "Profile",
                selected = selectedTab == HomeTab.PROFILE,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) TealPrimary.copy(alpha = 0.1f) else Color.Transparent,
        label = "background"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (selected) TealPrimary else Color(0xFF9CA3AF),
        label = "iconColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = iconColor
        )
    }
}
