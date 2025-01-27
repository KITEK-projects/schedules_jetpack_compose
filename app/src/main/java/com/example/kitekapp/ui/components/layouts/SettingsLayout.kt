package com.example.kitekapp.ui.components.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.R

import kotlinx.coroutines.launch

@Composable
fun SettingsLayout(
    navController: NavController,
    vm: MyViewModel,
) {
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
            Switch(
                checked = vm.settingsData.isCuratorHour,
                onCheckedChange = {
                    vm.viewModelScope.launch {
                        vm.updateSettingsData(isCuratorHour = it)
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
}