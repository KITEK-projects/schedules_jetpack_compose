package com.example.kitekapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun Settings(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
    ) {
        Header(navController, "Настройки")
        ChangeTimeClass()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeTimeClass() {
    var selectedIndex by remember { mutableIntStateOf(1) }
    val options = listOf("1:20", "1:30", "1:10")
    val checkedState = remember { mutableStateOf(true) }
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Разрыв пары",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            Switch(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it },
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
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = RoundedCornerShape(4.dp),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
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


