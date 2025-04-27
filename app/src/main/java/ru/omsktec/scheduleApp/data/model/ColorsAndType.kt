package ru.omsktec.scheduleApp.data.model

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class CustomColors(
    val background: Color,
    val itemPrimary: Color,
    val itemSecondary: Color,
    val mainText: Color,
    val secondaryTextAndIcons: Color,
    val accent: Color,
    val forLine: Color
)

data class CustomTypography(
    val robotoMedium20: TextStyle,
    val robotoRegular20: TextStyle,
    val robotoRegular16: TextStyle,
    val robotoMedium14: TextStyle,
    val robotoRegular14: TextStyle,
    val robotoMedium12: TextStyle,
    val robotoRegular12: TextStyle,
)

val LocalCustomColors = compositionLocalOf<CustomColors> {
    error("No CustomColors provided!")
}

val LocalCustomTypography = compositionLocalOf<CustomTypography> {
    error("No CustomTypography provided!")
}