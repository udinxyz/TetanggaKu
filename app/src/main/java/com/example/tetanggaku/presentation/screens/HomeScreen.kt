package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// TAB untuk bottom nav di Home — sekarang didefinisikan di file ini
enum class HomeTab {
    BERANDA, JOB_SAYA, PROFIL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onJobDetailClick: () -> Unit,
    onCreateJobClick: () -> Unit = {},          // optional, dipakai tombol "Buat permintaan"
    onAiAsk: (String) -> Unit = {}              // optional, kalau nanti kamu sambung ke backend AI
) {
    val selectedTab = remember { mutableStateOf(HomeTab.BERANDA) }

    // State untuk AI bottom sheet
    val showAiSheet = remember { mutableStateOf(false) }
    val aiQuestion = remember { mutableStateOf("") }
    val aiAnswer = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        floatingActionButton = {
            // Tombol AI di pojok kanan bawah (mengganti tombol +)
            FloatingActionButton(
                onClick = { showAiSheet.value = true },
                containerColor = Color(0xFF4F46E5),
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "AI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        bottomBar = {
            Surface(
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
                    BottomNavItemHome(
                        icon = Icons.Filled.Home,
                        label = "Beranda",
                        selected = selectedTab.value == HomeTab.BERANDA,
                        onClick = { selectedTab.value = HomeTab.BERANDA }
                    )
                    BottomNavItemHome(
                        icon = Icons.Filled.List,
                        label = "Job Saya",
                        selected = selectedTab.value == HomeTab.JOB_SAYA,
                        onClick = { selectedTab.value = HomeTab.JOB_SAYA }
                    )
                    BottomNavItemHome(
                        icon = Icons.Filled.Person,
                        label = "Profil",
                        selected = selectedTab.value == HomeTab.PROFIL,
                        onClick = {
                            selectedTab.value = HomeTab.PROFIL
                            onProfileClick()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab.value) {
            HomeTab.BERANDA -> {
                HomeBerandaContent(
                    modifier = Modifier.padding(innerPadding),
                    onCreateJobClick = onCreateJobClick,
                    onJobDetailClick = onJobDetailClick
                )
            }

            HomeTab.JOB_SAYA -> {
                HomeJobSayaContent(
                    modifier = Modifier.padding(innerPadding),
                    onJobDetailClick = onJobDetailClick
                )
            }

            HomeTab.PROFIL -> {
                // Konten profil di-handle di ProfileScreen via nav
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }

    // ============================
    // BOTTOM SHEET: TETANGGA AI
    // ============================
    if (showAiSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showAiSheet.value = false },
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
                    text = "Tulis masalah atau pekerjaan yang ingin kamu tanyakan. " +
                            "AI akan bantu kasih solusi cepat.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = aiQuestion.value,
                    onValueChange = { aiQuestion.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    placeholder = {
                        Text("Contoh: Cara aman angkat lemari ke lantai 2 gimana?")
                    }
                )

                Button(
                    onClick = {
                        if (aiQuestion.value.isNotBlank()) {
                            onAiAsk(aiQuestion.value)

                            // Sementara: jawaban dummy lokal
                            aiAnswer.value =
                                "Ini contoh jawaban AI:\n\n" +
                                        "1. Kalau berat, minta bantuan tetangga lain.\n" +
                                        "2. Kosongkan isi lemari dulu supaya lebih ringan.\n" +
                                        "3. Pakai alas kain/karpet di kaki lemari agar mudah digeser.\n" +
                                        "4. Jaga punggung tetap lurus dan angkat dengan kekuatan kaki."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F46E5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Dapatkan Solusi")
                }

                if (aiAnswer.value.isNotBlank()) {
                    Divider()
                    Text(
                        text = "Jawaban AI:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = aiAnswer.value,
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
// Konten tab Beranda
// =====================
@Composable
private fun HomeBerandaContent(
    modifier: Modifier = Modifier,
    onCreateJobClick: () -> Unit,
    onJobDetailClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F7))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Hi, Tetangga!",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Komplek Melati, Blok C",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Banner ungu
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF7C3AED)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Butuh bantuan hari ini?",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Lihat job yang tersedia atau buat permintaan baru untuk tetangga di sekitar.",
                    color = Color(0xFFEDE9FE),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onCreateJobClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F3FF),
                        contentColor = Color(0xFF5B21B6)
                    )
                ) {
                    Text("Buat permintaan")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Job di sekitar kamu",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        JobCardItem(
            category = "Angkut",
            title = "Bantu angkat lemari ke lantai 2",
            description = "Lemari kayu cukup berat, butuh 2 orang bantu angkat ke lantai atas.",
            price = "Rp50.000",
            requester = "Mbak Sari",
            verified = true,
            onClick = onJobDetailClick
        )
    }
}

// =====================
// Konten tab Job Saya
// =====================
@Composable
private fun HomeJobSayaContent(
    modifier: Modifier = Modifier,
    onJobDetailClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F7))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Job Saya",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        JobCardItem(
            category = "Titip beli",
            title = "Titip beli obat flu",
            description = "Titip beli obat flu dan vitamin di apotek depan komplek.",
            price = "Rp30.000",
            requester = "Kamu",
            verified = false,
            onClick = onJobDetailClick
        )
    }
}

@Composable
private fun JobCardItem(
    category: String,
    title: String,
    description: String,
    price: String,
    requester: String,
    verified: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = category,
                color = Color(0xFF6366F1),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = price,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = requester,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (verified) {
                    Surface(
                        color = Color(0xFF22C55E),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = "Verified",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavItemHome(
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
