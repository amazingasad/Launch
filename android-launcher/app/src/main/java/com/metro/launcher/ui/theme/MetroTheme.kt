package com.metro.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MetroColors = darkColorScheme(
    primary = Color(0xFFB9F36A),
    onPrimary = Color(0xFF1A2610),
    secondary = Color(0xFF80D9D0),
    background = Color(0xFF101116),
    surface = Color(0xFF191A21),
    surfaceVariant = Color(0xFF252731),
    onBackground = Color(0xFFF5F3EC),
    onSurface = Color(0xFFF5F3EC),
    onSurfaceVariant = Color(0xFFB6B7C1),
)

@Composable
fun MetroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MetroColors,
        content = content,
    )
}
