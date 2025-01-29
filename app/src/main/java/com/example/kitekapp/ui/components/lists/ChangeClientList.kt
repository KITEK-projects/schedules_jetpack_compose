package com.example.kitekapp.ui.components.lists

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
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.R
import com.example.kitekapp.ui.theme.customColors
import com.example.kitekapp.ui.theme.customTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeClientList(
    navController: NavController,
    vm: MyViewModel,
) {
    var textField by remember { mutableStateOf("") }
    val options = listOf("Группа", "Преподаватель")

    val filteredClients = vm.clientList.filter { it.contains(textField, ignoreCase = true) }
    LaunchedEffect(LocalContext.current) {
        vm.apiService.getClients(vm,0)
    }


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
                        vm.selectedTypeClient = index
                        vm.apiService.getClients(vm, index)
                    },
                    selected = index == vm.selectedTypeClient,
                    border = SegmentedButtonDefaults.borderStroke(
                        width = 0.dp,
                        color = Color.Transparent
                    ),
                    colors = SegmentedButtonColors(
                        activeContainerColor = customColors.itemPrimary,
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
                        style = customTypography.robotoRegular16,
                        color = customColors.mainText
                    )
                }
            }
        }
        Column {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(customColors.itemPrimary)
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
                        .padding(horizontal = 4.dp)
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = customColors.mainText,
                        unfocusedTextColor = customColors.mainText,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = customColors.mainText,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = customColors.mainText,
                        unfocusedPlaceholderColor = customColors.mainText
                    ),
                    textStyle = customTypography.robotoRegular16,
                    placeholder = {
                        Text(
                            if (vm.selectedTypeClient == 1) "Введите фамилию" else "Введите группу",
                            color = customColors.secondaryTextAndIcons
                        )
                    },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "",
                            tint = customColors.secondaryTextAndIcons
                        )
                    }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (vm.clientList.isNotEmpty() && filteredClients.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredClients) { item ->
                        Button(
                            onClick = {
                                vm.updateSettingsData(
                                    clientName = item
                                )
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
                                style = customTypography.robotoRegular16,
                                color = customColors.mainText,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            } else if (filteredClients.isEmpty() && vm.clientList.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ничего не найдено...",
                        style = customTypography.robotoRegular12,
                        color = customColors.secondaryTextAndIcons,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = customColors.secondaryTextAndIcons,
                        strokeWidth = 2.5.dp,
                    )
                }
            }
        }
    }
}