package ru.omsktec.scheduleApp.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
//import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ru.omsktec.scheduleApp.data.model.CustomColors
import ru.omsktec.scheduleApp.data.model.CustomTypography
import ru.omsktec.scheduleApp.data.model.LocalCustomColors
import ru.omsktec.scheduleApp.data.model.LocalCustomTypography

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val darkColorScheme = CustomColors(
        background = background,
        itemPrimary = itemPrimary,
        itemSecondary = itemSecondary,
        mainText = mainText,
        secondaryTextAndIcons = secondaryTextAndIcons,
        accent = accent,
        forLine = forLine
    )

//    val lightColorScheme = CustomColors(
//        background = backgroundLight,
//        itemPrimary = itemPrimaryLight,
//        itemSecondary = itemSecondaryLight,
//        mainText = mainTextLight,
//        secondaryTextAndIcons = secondaryTextAndIconsLight,
//        accent = accent,
//        forLine = forLineLight
//    )

    val colorScheme = darkColorScheme //if (darkTheme) darkColorScheme else lightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Для API >= 30
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
//                    isAppearanceLightStatusBars = !darkTheme
//                    isAppearanceLightNavigationBars = !darkTheme
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
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
