package ru.omsktec.scheduleApp.ui.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import ru.omsktec.scheduleApp.ui.components.headers.Header
import ru.omsktec.scheduleApp.ui.components.lists.ScheduleList


@Composable
fun MainLayout(pagerState: PagerState, navController: NavController, vm: MyViewModel) {
    Column {
        Header(pagerState = pagerState, navController = navController, vm = vm)
        ScheduleList(pagerState = pagerState, vm = vm)
    }
}