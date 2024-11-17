package com.example.kitekapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.Settings
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Change(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: MyViewModel,
    settings: Settings?,
) {
    Column(
        modifier = modifier
    ) {
        Header(navController, "Выбор расписания")
        ChangeSchedule(navController = navController, vm, settings)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeSchedule(
    navController: NavController,
    vm: MyViewModel,
    settings: Settings?,
) {

    var textField by remember { mutableStateOf("") }
    val options = listOf("Группа", "Преподаватель")


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        vm.selectedIndex = index
                        vm.updateClients(emptyList())
                    },
                    selected = index == vm.selectedIndex,
                    border = SegmentedButtonDefaults.borderStroke(
                        width = 0.dp,
                        color = Color.Transparent
                    ),
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
        Column {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onBackground)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = textField,
                    onValueChange = {
                        textField = it
                    },
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
                        if (vm.selectedIndex == 1) {
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
        }

        if (vm.clients.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxSize(),
            ) {
                items(vm.clients.filter { it.contains(textField, ignoreCase = true) }) { item ->
                    Button(
                        onClick = {
                            vm.viewModelScope.launch {
                                if (settings != null) {
                                    vm.saveSettings(
                                        Settings(
                                            clientName = item,
                                            isCuratorHour = settings.isCuratorHour
                                        )
                                    )
                                }
                            }
                            vm.getSchedule(item)
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else {
            fun Int.toBoolean(): Boolean = this == 1
            vm.getClients(vm.selectedIndex.toBoolean())
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    strokeWidth = 2.5.dp,
                )
            }
        }
    }

}
