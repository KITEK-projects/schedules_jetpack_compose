package ru.omsktek.scheduleApp.ui.components.headers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.omsktek.scheduleApp.R
import ru.omsktek.scheduleApp.viewmodel.MyViewModel
import ru.omsktek.scheduleApp.ui.theme.customColors
import ru.omsktek.scheduleApp.ui.theme.customTypography
import ru.omsktek.scheduleApp.utils.getDate

@Composable
fun Header(vm: MyViewModel, pagerState: PagerState, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getDate(vm, pagerState.currentPage),
                style = customTypography.robotoMedium20,
                color = customColors.mainText,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Button(
                modifier = Modifier
                    .heightIn(min = 20.dp, max = 30.dp),
                onClick = {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = customColors.accent
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = vm.schedule.clientName,
                    style = customTypography.robotoRegular14,
                    color = Color.White
                )
            }
        }
        IconButton(
            onClick = {
                navController.navigate("settings") {
                    popUpTo("main")
                }
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Settings",
                tint = customColors.secondaryTextAndIcons,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}