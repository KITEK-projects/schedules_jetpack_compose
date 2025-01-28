package com.example.kitekapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.kitekapp.data.model.CustomColors
import com.example.kitekapp.data.model.CustomTypography
import com.example.kitekapp.data.model.LocalCustomColors
import com.example.kitekapp.data.model.LocalCustomTypography

@Composable
fun AppTheme(
//    darkTheme: Boolean = isSystemInDarkTheme()
    content: @Composable () -> Unit
) {
    val colorScheme = CustomColors(
        background = background,
        itemPrimary = itemPrimary,
        itemSecondary = itemSecondary,
        mainText = mainText,
        secondaryTextAndIcons = secondaryTextAndIcons,
        accent = accent,
        forLine = forLine
    )

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
        }
    }

    CompositionLocalProvider(
        LocalCustomColors provides colorScheme,
        LocalCustomTypography provides typography
    ) {
        MaterialTheme(
            content = content
        )
    }
}

val customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current

val customTypography: CustomTypography
    @Composable
    get() = LocalCustomTypography.current
