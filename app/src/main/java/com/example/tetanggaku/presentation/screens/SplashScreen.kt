package com.example.tetanggaku.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tetanggaku.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    var startAnim by remember { mutableStateOf(false) }

    // animasi scale logo (zoom in)
    val logoScale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.7f,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "logoScale"
    )

    // animasi alpha logo (fade in)
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "logoAlpha"
    )

    LaunchedEffect(Unit) {
        startAnim = true
        // total durasi splash ± 1.8 detik
        delay(1800)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1E40AF), // biru tua
                        Color(0xFF1D4ED8)  // biru sedikit lebih terang
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tetanggaku),
            contentDescription = "TetanggaKu Logo",
            modifier = Modifier
                .width(360.dp)   // logo diperbesar
                .height(360.dp)
                .scale(logoScale)
                .alpha(logoAlpha)
        )
    }
}
