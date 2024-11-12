package com.example.kitekapp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitekapp.ClassItem
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.R
import com.example.kitekapp.Settings

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: MyViewModel,
    settings: Settings?
) {
    if (vm.schedule.client != "None" && vm.schedule.schedule.isNotEmpty() && vm.timeItems.isNotEmpty()) {
        val pagerState = rememberPagerState { vm.schedule.schedule.size }
        Column(
            modifier = modifier,
        ) {
            Header(pagerState = pagerState, navController = navController, vm=vm)
            Schedule(pagerState = pagerState, vm = vm, settings = settings)
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (vm.error == null && vm.messageError == null) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    strokeWidth = 2.5.dp,
                )
            } else {
                ErrorsScreen(vm = vm, navController = navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(vm: MyViewModel, pagerState: PagerState, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = vm.getDate(pagerState.currentPage),
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Button(
                modifier = Modifier
                    .heightIn(min = 20.dp, max = 30.dp),
                onClick = {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = vm.schedule.client,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
            }
        }
        IconButton(
            onClick = {
                navController.navigate("settings") {
                    popUpTo("main")
                }
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Schedule(vm: MyViewModel, pagerState: PagerState, settings: Settings?) {
    val snapAnimationSpec = spring(
        stiffness = Spring.StiffnessMedium,
        visibilityThreshold = Int.VisibilityThreshold.toFloat(),
        dampingRatio = Spring.DampingRatioNoBouncy
    )
    Column {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
        ) { page ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),

                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    decayAnimationSpec = rememberSplineBasedDecay(),
                    snapAnimationSpec = snapAnimationSpec
                )
            ) {
                itemsIndexed(vm.schedule.schedule.getOrNull(page)?.classes ?: emptyList()) { index, item ->

                    if (vm.schedule.schedule[page].classes.count {it.number == 2} == 2) {
                        if (item.number == 2 && vm.schedule.schedule[page].classes[index + 1].number > 2) {

                            val isSeniorCource = when(vm.typeClient(vm.schedule.client)) {
                                "1-2" -> false
                                "3-4" -> true
                                "teach" -> when(vm.typeClient(item.partner)) {
                                    "1-2" -> false
                                    "3-4" -> true

                                    else -> {false}
                                }

                                else -> {false}
                            }

                            Item(item, vm, isSeniorCource, settings, vm.isMonday(pagerState.currentPage))
                            LunchItem(vm, isSeniorCource, settings, vm.isMonday(pagerState.currentPage))
                        } else {
                            Item(item, vm, settings = settings, isMonday = vm.isMonday(pagerState.currentPage))
                        }
                    } else {
                        if (item.number == 2) {

                            val isSeniorCource = when(vm.typeClient(vm.schedule.client)) {
                                "1-2" -> false
                                "3-4" -> true
                                "teach" -> when(vm.typeClient(item.partner)) {
                                    "1-2" -> false
                                    "3-4" -> true

                                    else -> {false}
                                }

                                else -> {false}
                            }

                            Item(item, vm, isSeniorCource, settings, vm.isMonday(pagerState.currentPage))
                            LunchItem(vm, isSeniorCource, settings, vm.isMonday(pagerState.currentPage))
                        } else {
                            Item(item, vm, settings = settings, isMonday = vm.isMonday(pagerState.currentPage))
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Item(
    item: ClassItem,
    vm: MyViewModel,
    isSeniorCource: Boolean = false,
    settings: Settings?,
    isMonday: Boolean
) {
    val time: List<String> = if (item.number == 2) {
        vm.calculateSecondLessonAndLunchBreak(settings!!.isCuratorHour && isMonday, isSeniorCource)[1]
    } else {
        vm.getTimeItem(
            number = item.number,)
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 16.dp),
        ) {
            Text(
                text = time[0],
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = time[1],
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Column {
            Text(
                text = item.partner,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            ) {
                Text(
                    text = "${item.number} пара",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Row {
                    Icon(
                        painter = painterResource(R.drawable.ic_book),
                        contentDescription = "book",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = item.type,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                if (item.location != "") {
                    Text(
                        text = if (item.location.all { it.isDigit() }) "${item.location} каб." else item.location,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

