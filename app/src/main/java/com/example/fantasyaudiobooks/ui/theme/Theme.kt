package com.example.fantasyaudiobooks.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3A506B), // A deep blue-gray for primary elements
    secondary = Color(0xFF6F7D7A), // A dark taupe for secondary elements
    tertiary = Color(0xFF5E7267), // A muted, dark green for tertiary elements
    background = Color(0xFF1C1C1C), // A very dark gray for background
    surface = Color(0xFF2A2D2E), // A dark gray with a slight blue tint for surfaces
    onPrimary = Color.White, // White text on primary
    onSecondary = Color(0xFFDADADA), // Light gray text on secondary
    onTertiary = Color.White, // White text on tertiary
    onBackground = Color(0xFFDADADA), // Light gray text on dark background
    onSurface = Color(0xFFDADADA) // Light gray text on dark surface
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5A7D9A), // A desaturated blue-gray
    secondary = Color(0xFF9EB0AA), // A muted taupe for a sophisticated look
    tertiary = Color(0xFF8AA399), // A soft, muted green
    background = Color(0xFFF5F5F5), // A very light gray for background
    surface = Color(0xFFE6E8E9), // A light gray with a slight blue tint for surfaces
    onPrimary = Color.White,
    onSecondary = Color(0xFF2C2C2C), // Dark gray for contrast
    onTertiary = Color.White,
    onBackground = Color(0xFF2C2C2C), // Dark gray for text on light background
    onSurface = Color(0xFF2C2C2C) // Dark gray for text on light surface
)

@Composable
fun FantasyAudiobooksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}