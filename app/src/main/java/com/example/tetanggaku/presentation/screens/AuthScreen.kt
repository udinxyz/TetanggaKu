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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.R
import com.example.tetanggaku.presentation.viewmodels.AuthViewModel

@Composable
fun AuthScreen(
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Handle navigation saat login berhasil
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoggedIn()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Clean white background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo - larger and more prominent
            Text(
                text = "TetanggaKu",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A), // Deep blue
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Judul
            Text(
                text = "Login to your Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF374151),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFC62828),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "✕",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFC62828),
                            modifier = Modifier
                                .clickable { viewModel.clearError() }
                                .padding(start = 8.dp)
                        )
                    }
                }
            }

            // Form email  
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email", fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true,
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E3A8A),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form password
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password", fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E3A8A),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Sign in
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E3A8A), // Deep blue
                    contentColor = Color.White
                ),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Sign in",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider "Or sign in with"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE5E7EB)
                )
                Text(
                    text = "  -Or sign in with-  ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE5E7EB)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol sosial pakai logo
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                SocialCircleButton(
                    iconRes = R.drawable.logo_google,
                    contentDescription = "Sign in with Google",
                    onClick = { viewModel.loginWithSocial("Google") },
                    enabled = !uiState.isLoading
                )
                SocialCircleButton(
                    iconRes = R.drawable.logo_facebook,
                    contentDescription = "Sign in with Facebook",
                    onClick = { viewModel.loginWithSocial("Facebook") },
                    enabled = !uiState.isLoading
                )
                SocialCircleButton(
                    iconRes = R.drawable.logo_twitter,
                    contentDescription = "Sign in with Twitter",
                    onClick = { viewModel.loginWithSocial("Twitter") },
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Teks ke halaman Sign up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    fontSize = 13.sp
                )
                Text(
                    text = "Sign up",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1E3A8A),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
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
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(enabled = enabled) { onClick() },
        color = if (enabled) Color(0xFFF5F6FB) else Color(0xFFE0E0E0),
        shadowElevation = if (enabled) 4.dp else 0.dp,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(26.dp),
                alpha = if (enabled) 1f else 0.5f
            )
        }
    }
}
