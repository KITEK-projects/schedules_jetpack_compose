package ru.omsktek.scheduleApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.omsktek.scheduleApp.ui.components.layouts.AlertLayout
import ru.omsktek.scheduleApp.ui.theme.customColors
import ru.omsktek.scheduleApp.viewmodel.MyViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PreloadScreen(viewModel: MyViewModel, navController: NavController) {
    if (viewModel.responseCode == 0 && viewModel.apiError == "") {
        if (viewModel.settingsData.clientName == "None") {
            AlertLayout(
                header = "Приветствуем!",
                description = "Для продолжения вам нужно сделать очень важный выбор",
                buttonTitle = "Выбрать",
                onButtonClick = {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                }
            )
        } else {
            if (viewModel.settingsData.clientName != "") {
                viewModel.apiService.getSchedule(viewModel, viewModel.settingsData.clientName)
            }

            IsLoading()
        }

    } else if (viewModel.responseCode == 404) {
        AlertLayout(
            header = "Упс...",
            description = "Кажется нет расписания для ${viewModel.settingsData.clientName}, но вы можете выбрать другое расписание",
            buttonTitle = "Выбрать",
            onButtonClick = {
                navController.navigate("change_schedule") {
                    popUpTo("main")
                }
            }
        )
    } else if (viewModel.responseCode == 0 && "No address associated with hostname" in viewModel.apiError ) {
        AlertLayout(
            header = "Упс...",
            description = "Кажется вы не подключены к интернету",
            buttonTitle = "Обновить",
            onButtonClick = {
                viewModel.responseCode = 0
                viewModel.apiError = ""
            }
        )
    } else {
        AlertLayout(
            header = "Упс...",
            description = "Произошла не известная ошибка \n responseCode: ${viewModel.responseCode} \n ${viewModel.apiError}",
            buttonTitle = "Выбрать",
            onButtonClick = {
                navController.navigate("change_schedule") {
                    popUpTo("main")
                }
            }
        )
    }
}

@Composable
fun IsLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = customColors.secondaryTextAndIcons,
            strokeWidth = 2.5.dp,
        )
    }
}