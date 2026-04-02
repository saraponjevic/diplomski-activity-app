package com.example.activityapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = SavorySage,
    secondary = AvocadoSmoothie,
    tertiary = BlushBeet,

    background = AppBackground,
    surface = CardBackground,

    onPrimary = WhiteSoft,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,

    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun ActivityAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}