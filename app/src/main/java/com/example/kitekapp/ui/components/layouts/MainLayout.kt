package com.example.kitekapp.ui.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.kitekapp.viewmodel.MyViewModel
import com.example.kitekapp.ui.components.headers.Header
import com.example.kitekapp.ui.components.lists.ScheduleList


@Composable
fun MainLayout(pagerState: PagerState, navController: NavController, vm: MyViewModel) {
    Column {
        Header(pagerState = pagerState, navController = navController, vm = vm)
        ScheduleList(pagerState = pagerState, vm = vm)
    }
}