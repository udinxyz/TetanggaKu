package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsPrivacyScreen(
    onBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Ketentuan Layanan", "Kebijakan Privasi")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kebijakan & Privasi", fontWeight = FontWeight.Bold) },
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
                .background(Color.White)
                .padding(padding)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color(0xFF5B8DEF),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFF5B8DEF),
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            
            Divider(color = Color(0xFFE5E7EB), thickness = 1.dp)
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
            ) {
                when (selectedTabIndex) {
                    0 -> TermsContent()
                    1 -> PrivacyContent()
                }
            }
        }
    }
}

@Composable
private fun TermsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ketentuan Layanan TetanggaKu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        
        Text(
            text = "Terakhir diperbarui: 29 Desember 2025",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SectionCard(
            title = "1. Penerimaan Ketentuan",
            content = "Dengan mengakses dan menggunakan aplikasi TetanggaKu, Anda menyetujui untuk terikat oleh ketentuan layanan ini. Jika Anda tidak setuju dengan ketentuan ini, harap tidak menggunakan layanan kami."
        )
        
        SectionCard(
            title = "2. Deskripsi Layanan",
            content = "TetanggaKu adalah platform yang menghubungkan tetangga yang membutuhkan bantuan dengan tetangga yang dapat memberikan bantuan. Kami menyediakan platform, tetapi tidak bertanggung jawab atas kualitas layanan yang diberikan pengguna."
        )
        
        SectionCard(
            title = "3. Akun Pengguna",
            content = "Anda bertanggung jawab untuk menjaga keamanan akun Anda dan semua aktivitas yang terjadi di bawah akun Anda. Anda harus segera memberitahu kami tentang penggunaan akun Anda yang tidak sah."
        )
        
        SectionCard(
            title = "4. Kewajiban Pengguna",
            content = "Pengguna diharapkan untuk: (a) Memberikan informasi yang akurat, (b) Memperlakukan pengguna lain dengan rasa hormat, (c) Menyelesaikan pekerjaan sesuai kesepakatan, (d) Tidak melakukan penipuan atau penyalahgunaan platform."
        )
        
        SectionCard(
            title = "5. Pembayaran dan Biaya",
            content = "Saat ini, TetanggaKu tidak mengenakan biaya platform. Pembayaran untuk layanan dilakukan langsung antara pengguna. Kami berhak untuk memperkenalkan biaya layanan di masa mendatang dengan pemberitahuan sebelumnya."
        )
        
        SectionCard(
            title = "6. Penghentian Layanan",
            content = "Kami berhak untuk menangguhkan atau menghentikan akun Anda jika Anda melanggar ketentuan layanan ini atau jika kami menduga adanya aktivitas yang mencurigakan."
        )
        
        SectionCard(
            title = "7. Batasan Tanggung Jawab",
            content = "TetanggaKu tidak bertanggung jawab atas kerugian langsung, tidak langsung, atau konsekuensial yang timbul dari penggunaan layanan kami. Layanan disediakan \"sebagaimana adanya\" tanpa jaminan apapun."
        )
    }
}

@Composable
private fun PrivacyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Kebijakan Privasi TetanggaKu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        
        Text(
            text = "Terakhir diperbarui: 29 Desember 2025",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SectionCard(
            title = "1. Informasi yang Kami Kumpulkan",
            content = "Kami mengumpulkan informasi yang Anda berikan saat mendaftar, termasuk nama, email, nomor telepon, dan lokasi. Kami juga mengumpulkan informasi tentang penggunaan aplikasi Anda."
        )
        
        SectionCard(
            title = "2. Penggunaan Informasi",
            content = "Informasi Anda digunakan untuk: (a) Menyediakan dan meningkatkan layanan, (b) Menghubungkan Anda dengan pengguna lain, (c) Mengirim notifikasi terkait layanan, (d) Mencegah penipuan dan penyalahgunaan."
        )
        
        SectionCard(
            title = "3. Berbagi Informasi",
            content = "Kami tidak menjual informasi pribadi Anda. Informasi dapat dibagikan dengan pengguna lain untuk tujuan layanan (seperti nama dan lokasi saat ada permintaan job). Kami juga dapat membagikan informasi jika diwajibkan oleh hukum."
        )
        
        SectionCard(
            title = "4. Keamanan Data",
            content = "Kami menggunakan langkah-langkah keamanan standar industri untuk melindungi informasi Anda. Namun, tidak ada metode transmisi melalui internet yang 100% aman."
        )
        
        SectionCard(
            title = "5. Hak Anda",
            content = "Anda memiliki hak untuk: (a) Mengakses informasi pribadi Anda, (b) Memperbarui atau mengoreksi informasi, (c) Menghapus akun Anda, (d) Menolak penggunaan data tertentu."
        )
        
        SectionCard(
            title = "6. Cookies dan Pelacakan",
            content = "Kami menggunakan cookies dan teknologi pelacakan serupa untuk meningkatkan pengalaman pengguna dan menganalisis penggunaan aplikasi. Anda dapat mengontrol pengaturan cookie melalui perangkat Anda."
        )
        
        SectionCard(
            title = "7. Perubahan Kebijakan",
            content = "Kami dapat memperbarui kebijakan privasi ini dari waktu ke waktu. Kami akan memberitahu Anda tentang perubahan signifikan melalui email atau notifikasi di aplikasi."
        )
        
        SectionCard(
            title = "8. Hubungi Kami",
            content = "Jika Anda memiliki pertanyaan tentang kebijakan privasi kami, silakan hubungi kami di: privacy@tetanggaku.com"
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = content,
                fontSize = 13.sp,
                color = Color(0xFF4B5563),
                lineHeight = 20.sp
            )
        }
    }
}
