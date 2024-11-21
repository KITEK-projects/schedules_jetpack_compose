package com.example.kitekapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.R
import com.example.kitekapp.Settings
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Settings(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: MyViewModel,
    settings: Settings?,
) {
    Column(
        modifier = modifier
    ) {
        Header(navController, "Настройки")
        Screen(navController, vm, settings)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    navController: NavController,
    vm: MyViewModel,
    settings: Settings?
) {
    val options = listOf("1:20", "1:30", "1:10")
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                }
        ) {
            Text(
                text = "Выбор расписания",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            IconButton(
                onClick = {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_right),
                    contentDescription = "Right",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Кураторскиe часы по пн.",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            if (settings != null) {
                Switch(
                    checked = settings.isCuratorHour,
                    onCheckedChange = {
                        vm.viewModelScope.launch {
                            vm.saveSettings(
                                Settings(
                                    clientName = settings.clientName,
                                    isCuratorHour = it
                                )
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.onBackground,
                        checkedBorderColor = Color.Transparent,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onBackground
                    ),
                )
            }
        }
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        vm.updateSelectLessonDuration(index)
                    },
                    selected = index == vm.selectLessonDuration,
                    border = SegmentedButtonDefaults.borderStroke(width = 0.dp, color = Color.Transparent),
                    colors = SegmentedButtonColors(
                        activeContainerColor = MaterialTheme.colorScheme.onBackground,
                        activeBorderColor = Color.Transparent,
                        activeContentColor = Color.Transparent,
                        inactiveContainerColor = Color.Transparent,
                        inactiveBorderColor = Color.Transparent,
                        inactiveContentColor = Color.Transparent,
                        disabledActiveContainerColor = Color.Transparent,
                        disabledActiveBorderColor = Color.Transparent,
                        disabledActiveContentColor = Color.Transparent,
                        disabledInactiveContainerColor = Color.Transparent,
                        disabledInactiveBorderColor = Color.Transparent,
                        disabledInactiveContentColor = Color.Transparent,
                    ),
                    icon = {},
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}


