package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetanggaku.R

@Composable
fun AuthScreen(
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2FF)) // background lembut
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo text (kalau mau diganti jadi Image logo, tinggal ubah di sini)
            Text(
                text = "TetanggaKu",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E40AF),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Judul
            Text(
                text = "Login to your Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Form password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Sign in
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onLoggedIn()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E40AF),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider "Or sign in with"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFDFE3F0)
                )
                Text(
                    text = "  Or sign in with  ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFDFE3F0)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol sosial pakai logo
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialCircleButton(
                    iconRes = R.drawable.logo_google,
                    contentDescription = "Sign in with Google"
                )
                SocialCircleButton(
                    iconRes = R.drawable.logo_facebook,
                    contentDescription = "Sign in with Facebook"
                )
                SocialCircleButton(
                    iconRes = R.drawable.logo_twitter,
                    contentDescription = "Sign in with Twitter"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Teks ke halaman Sign up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Sign up",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1E40AF),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
}

@Composable
fun SocialCircleButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        color = Color(0xFFF5F6FB),
        shadowElevation = 4.dp,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
