package com.englishflashcard.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Deep Green Theme - Dark Mode
private val DeepGreenDarkColorScheme = darkColorScheme(
    primary = MediumGreen,
    primaryContainer = DeepGreen,
    secondary = LightGreen,
    secondaryContainer = MintGreen,
    tertiary = LimeAccent,
    background = DeepBackground,
    surface = SurfaceGreen,
    surfaceVariant = Color(0xFF263238),
    error = DeepError,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFFB0BEC5)
)

// Deep Green Theme - Light Mode (Main)
private val DeepGreenLightColorScheme = lightColorScheme(
    primary = DeepGreen,
    primaryContainer = MediumGreen,
    secondary = LightGreen,
    secondaryContainer = MintGreen,
    tertiary = LimeAccent,
    tertiaryContainer = Color(0xFFF0F4C3),
    background = LightBackground,
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F8E9),
    error = DeepError,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextSecondary,
    onError = Color.White,
    outline = MediumGreen.copy(alpha = 0.5f)
)

@Composable
fun EnglishFlashcardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DeepGreenDarkColorScheme
    } else {
        DeepGreenLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set statusbar to deep green for modern look
            window.statusBarColor = DeepGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

