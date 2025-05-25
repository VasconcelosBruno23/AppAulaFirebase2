package com.example.appaulafirebase.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightBlueColorScheme = lightColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFE3F2FD),
    onSurface = Color.Black
)

@Composable
fun AppAulaFirebaseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightBlueColorScheme,
        typography = Typography(),
        content = content
    )
}
