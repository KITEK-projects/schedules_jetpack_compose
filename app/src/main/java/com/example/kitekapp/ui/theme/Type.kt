package com.example.kitekapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.kitekapp.R

val inter = FontFamily(Font(R.font.inter, FontWeight.Normal))

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = inter,
        fontSize = 20.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = inter,
        fontSize = 16.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = inter,
        fontSize = 14.sp,
    ),
)