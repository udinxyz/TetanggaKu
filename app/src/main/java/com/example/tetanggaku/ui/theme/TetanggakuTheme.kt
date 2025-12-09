package com.example.tetanggaku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Warna dasar sederhana (boleh nanti kamu ganti)
private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun TetanggakuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,   // ini pakai Typography dari Type.kt
        content = content
    )
}
