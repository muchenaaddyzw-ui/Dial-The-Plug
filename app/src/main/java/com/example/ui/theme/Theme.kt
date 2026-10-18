package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DialDarkColorScheme = darkColorScheme(
    primary = Persian,
    onPrimary = Ghost,
    primaryContainer = PersianContainer,
    onPrimaryContainer = Ghost,
    secondary = PersianBlueVibrant,
    onSecondary = Ghost,
    secondaryContainer = Persian,
    onSecondaryContainer = Ghost,
    tertiary = PersianBlueLight,
    onTertiary = Ghost,
    background = DarkBackground,
    onBackground = Ghost,
    surface = DarkSurface,
    onSurface = Ghost,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = GhostSecondary,
    outline = DarkCardBorder,
    outlineVariant = DarkBorderHighlight
)

@Composable
fun DialThePlugTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DialDarkColorScheme,
        typography = DialTypography,
        content = content
    )
}
