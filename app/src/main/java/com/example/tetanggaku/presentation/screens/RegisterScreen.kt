package com.example.tetanggaku.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetanggaku.R
import com.example.tetanggaku.presentation.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Handle navigation saat registrasi berhasil
    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) {
            onRegisterSuccess()
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
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Back arrow
            IconButton(
                onClick = onBack,
                enabled = !uiState.isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF374151)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo - matching AuthScreen
                Text(
                    text = "TetanggaKu",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A),
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Create your Account",
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

                // Form name
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Name", fontSize = 14.sp) },
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

                Spacer(modifier = Modifier.height(12.dp))

                // Form email
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Email", fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E3A8A),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Form phone
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    label = { Text("Phone", fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E3A8A),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(12.dp))

                // Form confirm password
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                    label = { Text("Confirm Password", fontSize = 14.sp) },
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

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Sign up
                Button(
                    onClick = { viewModel.register() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E3A8A),
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
                            text = "Sign up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE5E7EB)
                    )
                    Text(
                        text = "  -Or sign up with-  ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE5E7EB)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Social buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    SocialCircleButton(
                        iconRes = R.drawable.logo_google,
                        contentDescription = "Sign up with Google",
                        onClick = { viewModel.registerWithSocial("Google") },
                        enabled = !uiState.isLoading
                    )
                    SocialCircleButton(
                        iconRes = R.drawable.logo_facebook,
                        contentDescription = "Sign up with Facebook",
                        onClick = { viewModel.registerWithSocial("Facebook") },
                        enabled = !uiState.isLoading
                    )
                    SocialCircleButton(
                        iconRes = R.drawable.logo_twitter,
                        contentDescription = "Sign up with Twitter",
                        onClick = { viewModel.registerWithSocial("Twitter") },
                        enabled = !uiState.isLoading
                    )
                }
            }
        }
    }
}
