package com.example.kitekapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = bg,
    onBackground = bg_item,
    onPrimary = opacity_white,
    onSecondary = bg_additional,
    primary = blue,
    secondary = gray,
)

@Composable
fun KITEKAPPTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}