package ru.omsktec.scheduleApp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import ru.omsktec.scheduleApp.ui.components.lists.ChangeClientList
import ru.omsktec.scheduleApp.ui.components.headers.SecondaryScreenHeader

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