package com.emerald.postoffice.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ThaiPostRed,
    onPrimary = White,
    primaryContainer = ThaiPostRedLight,
    secondary = ThaiPostRedDark,
    background = Background,
    surface = White,
    onBackground = TextDark,
    onSurface = TextDark,
    outline = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFFF3F4F6)
)

@Composable
fun PostOfficeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
