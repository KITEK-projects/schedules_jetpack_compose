package com.example.kitekapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitekapp.Lesson
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.R

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Main(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: MyViewModel,
) {
    if (vm.schedules.schedules.isNotEmpty()) {
        val pagerState = rememberPagerState { vm.schedules.schedules.size }
        Column(
            modifier = modifier,
        ) {
            Header(pagerState = pagerState, navController = navController, vm = vm)
            Schedule(pagerState = pagerState, vm = vm)
        }
    } else {
        if (vm.settings.value != null) {
            if (vm.settings.value!!.clientName == "") {
                FirstStart(navController)
            } else {
                if (vm.error.isScheduleDefault) {
                    IsLoading()
                } else {
                    ErrorsScreen(vm = vm, navController = navController)
                }
            }
        }
    }
}

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
                    text = vm.schedules.clientName,
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


@Composable
fun Schedule(vm: MyViewModel, pagerState: PagerState) {
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
            if (vm.schedules.schedules.getOrNull(page)?.lessons?.isNotEmpty() == true) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        decayAnimationSpec = rememberSplineBasedDecay(),
                        snapAnimationSpec = snapAnimationSpec
                    )
                ) {
                    itemsIndexed(
                        vm.schedules.schedules.getOrNull(page)?.lessons ?: emptyList()
                    ) { _, item ->
                        Item(item)
                    }
                }
            } else {
                Text(
                    text = "Сегодня занятия не проводятся",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun Item(
    item: Lesson,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            item.items.mapIndexed { index, lesson ->
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = lesson.type,
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${item.time[0]}-${item.time[1]} - ${item.number} пара",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_partner),
                                contentDescription = "partner",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = lesson.partner,
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        if (!lesson.location.isNullOrEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_location),
                                    contentDescription = "location",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = lesson.location,
                                    style = MaterialTheme.typography.displaySmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    if (item.items.size > 1 && index == 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSecondary)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

