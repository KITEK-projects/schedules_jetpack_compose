package ru.omsktec.scheduleApp.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.omsktec.scheduleApp.R
import ru.omsktec.scheduleApp.data.model.CustomTypography


val robotoRegular = FontFamily(Font(R.font.roboto, FontWeight.Normal))
val robotoMedium = FontFamily(Font(R.font.roboto, FontWeight.Medium))

val typography = CustomTypography(
    robotoMedium20 = TextStyle(
        fontSize = 20.sp,
        fontFamily = robotoMedium
    ),
    robotoRegular20 = TextStyle(
        fontSize = 20.sp,
        fontFamily = robotoRegular
    ),
    robotoRegular16 = TextStyle(
        fontSize = 16.sp,
        fontFamily = robotoRegular
    ),
    robotoMedium14 = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoMedium
    ),
    robotoRegular14 = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoRegular
    ),
    robotoMedium12 = TextStyle(
        fontSize = 12.sp,
        fontFamily = robotoMedium
    ),
    robotoRegular12 = TextStyle(
        fontSize = 12.sp,
        fontFamily = robotoRegular
    )
)