package ru.omsktek.scheduleApp.ui.components.lists

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.omsktek.scheduleApp.ui.components.items.ScheduleListItem
import ru.omsktek.scheduleApp.ui.theme.customColors
import ru.omsktek.scheduleApp.ui.theme.customTypography
import ru.omsktek.scheduleApp.viewmodel.MyViewModel

@Composable
fun ScheduleList(vm: MyViewModel, pagerState: PagerState) {
    val snapAnimationSpec = spring(
        stiffness = Spring.StiffnessMedium,
        visibilityThreshold = Int.VisibilityThreshold.toFloat(),
        dampingRatio = Spring.DampingRatioNoBouncy
    )
    Column {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (vm.schedule.schedules.getOrNull(page)?.lessons?.isNotEmpty() == true) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        decayAnimationSpec = rememberSplineBasedDecay(),
                        snapAnimationSpec = snapAnimationSpec
                    )
                ) {
                    itemsIndexed(
                        vm.schedule.schedules.getOrNull(page)?.lessons ?: emptyList()
                    ) { _, item ->
                        ScheduleListItem(vm, item, vm.schedule.schedules[page].date)
                    }
                }
            } else {
                Text(
                    text = "Расписание отсутсвует",
                    style = customTypography.robotoRegular16,
                    color = customColors.secondaryTextAndIcons,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}