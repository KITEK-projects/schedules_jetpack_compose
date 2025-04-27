package ru.omsktec.scheduleApp.ui.components.headers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.omsktec.scheduleApp.R
import ru.omsktec.scheduleApp.ui.theme.customColors
import ru.omsktec.scheduleApp.ui.theme.customTypography

@Composable
fun SecondaryScreenHeader(navController: NavController, text: String) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = text,
                style = customTypography.robotoMedium20,
                color = customColors.mainText,
            )
            IconButton(
                onClick = {
                    val canGoBack = navController.popBackStack()
                    if (!canGoBack) {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = customColors.secondaryTextAndIcons,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = customColors.forLine)
    }
}