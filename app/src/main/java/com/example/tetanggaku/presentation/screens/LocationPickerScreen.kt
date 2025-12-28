package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.viewmodels.LocationViewModel

private val TealPrimary = Color(0xFF2D7A7A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
    onBack: () -> Unit = {},
    onLocationSelected: (String) -> Unit = {},
    viewModel: LocationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Location") },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Search field
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search location...") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                singleLine = true
            )
            
            // Current location button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { viewModel.getCurrentLocation() },
                colors = CardDefaults.cardColors(containerColor = TealPrimary.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Filled.LocationOn,
                        null,
                        tint = TealPrimary
                    )
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Use current location",
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary
                        )
                        Text(
                            "Get your precise location",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TealPrimary
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Recent locations
            if (uiState.recentLocations.isNotEmpty()) {
                Text(
                    "Recent",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                uiState.recentLocations.forEach { location ->
                    LocationItem(
                        location = location,
                        onClick = {
                            viewModel.selectLocation(location)
                            onLocationSelected(location)
                            onBack()
                        }
                    )
                }
                
                Spacer(Modifier.height(16.dp))
            }
            
            // Suggested locations
            Text(
                "Suggested Locations",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn {
                items(uiState.suggestedLocations) { location ->
                    LocationItem(
                        location = location,
                        onClick = {
                            viewModel.selectLocation(location)
                            onLocationSelected(location)
                            onBack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationItem(location: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Filled.LocationOn,
            null,
            tint = Color.Gray
        )
        Text(location, fontSize = 14.sp)
    }
    Divider(color = Color(0xFFE5E7EB))
}
