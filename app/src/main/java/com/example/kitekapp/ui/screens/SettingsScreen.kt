package com.example.kitekapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.ui.components.headers.SecondaryScreenHeader
import com.example.kitekapp.ui.components.layouts.SettingsLayout

@Composable
fun SettingsScreen(
    navController: NavController,
    vm: MyViewModel,
) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        SecondaryScreenHeader(navController, "Настройки")
        SettingsLayout(navController, vm)
    }
}