package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CompareCartDarkColorScheme = darkColorScheme(
    primary = BrandCyan,
    onPrimary = BackgroundDark,
    primaryContainer = BrandBlueDark,
    onPrimaryContainer = Color.White,
    secondary = BrandBlue,
    onSecondary = Color.White,
    secondaryContainer = SurfaceVariantDark,
    onSecondaryContainer = BrandCyan,
    tertiary = AsliGreen,
    onTertiary = Color.Black,
    tertiaryContainer = AsliGreenContainer,
    onTertiaryContainer = AsliGreen,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderActive,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun CompareCartTheme(
    darkTheme: Boolean = true, // Default to sleek high-contrast dark theme as requested
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CompareCartDarkColorScheme,
        typography = Typography,
        content = content
    )
}
