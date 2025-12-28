package com.example.tetanggaku.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.presentation.viewmodels.CreateJobViewModel

// Teal color palette
private val TealPrimary = Color(0xFF2D7A7A)
private val TealLight = Color(0xFFE0F2F1)
private val TealDark = Color(0xFF1D5F5F)
private val LightGray = Color(0xFFF9FAFB)
private val BorderGray = Color(0xFFE5E7EB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(
    onBack: () -> Unit,
    onJobCreated: () -> Unit,
    viewModel: CreateJobViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Category options
    val categories = listOf(
        "Angkut Barang", "Titip Beli", "Perbaikan", 
        "Cleaning", "Laundry", "Masak", "Lainnya"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Buat Permintaan",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Bantuan dari tetangga",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealPrimary,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = LightGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header info
            InfoCard(
                icon = Icons.Filled.Info,
                text = "Jelaskan kebutuhan Anda dengan detail agar tetangga mudah memahami"
            )

            // Title Field
            InputCard(
                title = "Judul Permintaan",
                icon = Icons.Filled.Edit
            ) {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    placeholder = { Text("Contoh: Bantu angkat lemari") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealPrimary,
                        focusedLabelColor = TealPrimary
                    )
                )
            }

            // Category Selection
            InputCard(
                title = "Kategori",
                icon = Icons.Filled.List
            ) {
                Text(
                    "Pilih kategori yang sesuai:",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Category chips
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.chunked(3).forEach { rowCategories ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowCategories.forEach { category ->
                                CategoryChip(
                                    text = category,
                                    isSelected = uiState.category == category,
                                    onClick = { viewModel.updateCategory(category) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill empty spaces
                            repeat(3 - rowCategories.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                
                // Custom category option
                if (uiState.category == "Lainnya" || !categories.contains(uiState.category)) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = if (uiState.category == "Lainnya") "" else uiState.category,
                        onValueChange = { viewModel.updateCategory(it) },
                        placeholder = { Text("Tulis kategori lain...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary
                        )
                    )
                }
            }

            // Description
            InputCard(
                title = "Deskripsi Lengkap",
                icon = Icons.Filled.Create
            ) {
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { viewModel.updateDescription(it) },
                    placeholder = { Text("Jelaskan detail pekerjaan yang dibutuhkan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealPrimary
                    )
                )
            }

            // Reward
            InputCard(
                title = "Imbalan",
                icon = Icons.Filled.Star
            ) {
                OutlinedTextField(
                    value = uiState.reward,
                    onValueChange = { viewModel.updateReward(it) },
                    placeholder = { Text("Contoh: Rp 50.000") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Text(
                            "Rp",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealPrimary
                    )
                )
            }

            // Date & Time
            InputCard(
                title = "Waktu & Lokasi",
                icon = Icons.Filled.DateRange
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.date,
                        onValueChange = { viewModel.updateDate(it) },
                        placeholder = { Text("Tanggal (mis. 10 Des 2025)") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Filled.DateRange, null, tint = TealPrimary)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary
                        )
                    )
                    
                    OutlinedTextField(
                        value = uiState.time,
                        onValueChange = { viewModel.updateTime(it) },
                        placeholder = { Text("Jam (mis. 14:00 - 16:00)") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Filled.Settings, null, tint = TealPrimary)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary
                        )
                    )
                    
                    OutlinedTextField(
                        value = uiState.location,
                        onValueChange = { viewModel.updateLocation(it) },
                        placeholder = { Text("Lokasi (mis. Blok C No. 12)") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Filled.LocationOn, null, tint = TealPrimary)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary
                        )
                    )
                }
            }

            // Optional fields
            InputCard(
                title = "Info Tambahan (Opsional)",
                icon = Icons.Filled.Add
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.volunteerNeeded,
                        onValueChange = { viewModel.updateVolunteerNeeded(it) },
                        placeholder = { Text("Butuh berapa orang?") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Filled.Person, null, tint = TealPrimary)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary
                        )
                    )
                    
                    OutlinedTextField(
                        value = uiState.note,
                        onValueChange = { viewModel.updateNote(it) },
                        placeholder = { Text("Catatan khusus...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TealPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp
                    )
                ) {
                    Text("Batal", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { viewModel.createJob(onJobCreated) },
                    enabled = uiState.isFormValid && !uiState.isLoading,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = TealPrimary.copy(alpha = 0.4f)
                    ),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Buat Permintaan", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoCard(icon: ImageVector, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TealLight),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text,
                fontSize = 13.sp,
                color = TealDark,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun InputCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }
            
            content()
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) TealPrimary else Color.White,
        label = "bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF4B5563),
        label = "text"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) TealPrimary else BorderGray,
        label = "border"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor
        )
    }
}
