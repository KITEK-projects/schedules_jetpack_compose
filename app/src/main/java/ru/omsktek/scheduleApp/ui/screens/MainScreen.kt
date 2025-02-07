package ru.omsktek.scheduleApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.omsktek.scheduleApp.viewmodel.MyViewModel
import ru.omsktek.scheduleApp.ui.components.layouts.MainLayout

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MyViewModel,
) {
    if (viewModel.responseCode in 200..299 && viewModel.apiError == "") {
        val pagerState = rememberPagerState { viewModel.schedule.schedules.size }
        MainLayout(pagerState, navController, viewModel)
    } else {
        PreloadScreen(viewModel, navController)
    }
}