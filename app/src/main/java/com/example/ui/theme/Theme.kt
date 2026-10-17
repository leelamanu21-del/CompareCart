package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CompareCartLightColorScheme = lightColorScheme(
    primary = AmazonTeal,
    onPrimary = Color.White,
    primaryContainer = AmazonYellow,
    onPrimaryContainer = TextDark,
    secondary = AmazonOrange,
    onSecondary = Color.White,
    secondaryContainer = SurfaceLightGrey,
    onSecondaryContainer = TextDark,
    tertiary = AmazonGreen,
    onTertiary = Color.White,
    tertiaryContainer = AsliGreenContainer,
    onTertiaryContainer = AmazonGreen,
    background = BackgroundWhite,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    surfaceVariant = SurfaceLightGrey,
    onSurfaceVariant = TextMutedGrey,
    outline = BorderLight,
    outlineVariant = BorderSubtleGrey,
    error = AmazonRed,
    onError = Color.White
)

@Composable
fun CompareCartTheme(
    darkTheme: Boolean = false, // Clean, crisp Amazon Light Theme by default
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CompareCartLightColorScheme,
        typography = Typography,
        content = content
    )
}
