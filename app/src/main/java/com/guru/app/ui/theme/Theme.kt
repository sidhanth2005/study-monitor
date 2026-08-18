package com.guru.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNeon,
    secondary = PrimaryCyan,
    tertiary = AccentGreen,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

private val AmoledColorScheme = darkColorScheme(
    primary = PrimaryNeon,
    secondary = PrimaryCyan,
    tertiary = AccentOrange,
    background = AmoledBackground,
    surface = AmoledSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNeon,
    secondary = PrimaryCyan,
    tertiary = AccentOrange,
    background = Color(0xFFF5F6FA),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1E1E2E),
    onSurface = Color(0xFF1E1E2E)
)

@Composable
fun GURUTheme(
    themeMode: String = "AMOLED", // LIGHT, DARK, AMOLED
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode.uppercase()) {
        "LIGHT" -> LightColorScheme
        "DARK" -> DarkColorScheme
        "AMOLED" -> AmoledColorScheme
        else -> if (isSystemInDarkTheme()) AmoledColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
