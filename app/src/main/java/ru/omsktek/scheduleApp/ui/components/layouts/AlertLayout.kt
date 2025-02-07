package ru.omsktek.scheduleApp.ui.components.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.omsktek.scheduleApp.ui.theme.customColors
import ru.omsktek.scheduleApp.ui.theme.customTypography

@Composable
fun AlertLayout(
    header: String,
    description: String,
    buttonTitle: String,
    onButtonClick: () -> Unit
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = header,
            style = customTypography.robotoRegular20,
            color = customColors.mainText
        )
        Text(
            text = description,
            style = customTypography.robotoRegular14,
            color = customColors.secondaryTextAndIcons,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = buttonTitle,
            style = customTypography.robotoMedium14,
            color = customColors.accent,
            modifier = Modifier
                .padding(vertical = 15.dp)
                .clickable { onButtonClick() }
        )
    }
}