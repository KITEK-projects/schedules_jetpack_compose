package com.example.kitekapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.ui.components.lists.ChangeClientList
import com.example.kitekapp.ui.components.headers.SecondaryScreenHeader

@Composable
fun ChangeClientScreen(
    navController: NavController,
    vm: MyViewModel,
) {
    Column {
        SecondaryScreenHeader(navController, "Выбор расписания")
        ChangeClientList(navController = navController, vm)
    }
}