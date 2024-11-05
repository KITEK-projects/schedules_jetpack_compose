package com.example.kitekapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitekapp.R

@Composable
fun Main_change(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
    ) {
        Header(navController)
        ChangeSchedule()
    }

}

@Composable
fun Header(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = "Выбор расписания",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
            )
            IconButton(
                onClick = {
                    navController.navigate("main") {
                        // Убрать возращение в change
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeSchedule() {
    val clientList = remember { mutableStateListOf(
        "KO-21", "ОО-31", "ОО-41", "ПКД-21", "ПКД-22",
        "ПКД-23", "ПКД-31", "ПКД-32", "ПКД-41", "ПКД-42",
        "ГД-21", "ГД-31", "ГД-41", "К-21", "К-31",
        "ТЭ-21", "ТЭ-22", "ТЭ-23", "ТЭ-31", "ТЭ-32",
        "ТЭ-41", "ТЭ-42", "Б-21", "Б-31", "ИСП-21",
        "ИСП-31", "ИСП-41", "ИСР-21", "ИСР-31", "ИСР-41",
        "ПСО-21", "ПСО-31", "ВБ-11", "ГД-11", "ИСП-11",
        "ИСР-11", "ПКД-11", "ПКД-12", "ПСО-11", "ТЭ-11",
        "ТЭ-12"
    )}
    var textField by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Преподаватель", "Группа")
    val filteredList = clientList.filter { it.contains(textField, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
                        .height(52.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White
                    )
                }
            }
        }
        Column {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                        .fillMaxWidth()
                        .height(52.dp)
                )
                TextField(
                    value = textField,
                    onValueChange = { textField = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Color.White,
                        unfocusedPlaceholderColor = Color.White
                    ),
                    textStyle = MaterialTheme.typography.displayMedium,
                    placeholder = {
                        if (selectedIndex == 0) {
                            Text(
                                "Введите фамилию",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text(
                                "Введите группу",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    singleLine = true,
                )
            }
//            Text(
//                text = "можно без капса...",
//                style = MaterialTheme.typography.displaySmall,
//                color = MaterialTheme.colorScheme.secondary,
//                modifier = Modifier
//                    .padding(horizontal = 24.dp)
//                    .fillMaxWidth(),
//            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        ) {
            items(filteredList) { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            }
        }
    }

}
