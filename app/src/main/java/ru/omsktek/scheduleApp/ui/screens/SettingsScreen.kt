package ru.omsktek.scheduleApp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.omsktek.scheduleApp.viewmodel.MyViewModel
import ru.omsktek.scheduleApp.ui.components.headers.SecondaryScreenHeader
import ru.omsktek.scheduleApp.ui.components.layouts.SettingsLayout

@RequiresApi(Build.VERSION_CODES.P)
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