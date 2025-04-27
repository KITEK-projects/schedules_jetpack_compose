package ru.omsktec.scheduleApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import ru.omsktec.scheduleApp.ui.components.layouts.MainLayout

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MyViewModel,
) {
    if (viewModel.responseCode in 200..299 && viewModel.apiError == "") {
        val pagerState = rememberPagerState(
            initialPage = viewModel.currentPage,
            pageCount = { viewModel.schedule.schedules.size }
        )

        // Синхронизация состояния с ViewModel
        LaunchedEffect(pagerState.currentPage) {
            viewModel.updateCurrentPage(pagerState.currentPage)
        }

        MainLayout(pagerState, navController, viewModel)
    } else {
        PreloadScreen(viewModel, navController)
    }
}