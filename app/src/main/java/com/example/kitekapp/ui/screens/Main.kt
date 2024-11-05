package com.example.kitekapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kitekapp.ClassItem
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: MyViewModel = viewModel())
{
    val pagerState = rememberPagerState { vm.schedule.schedule.size }
    Column(
        modifier = modifier,
    ) {
        Header(pagerState = pagerState, navController = navController)
        Schedule(pagerState = pagerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(vm: MyViewModel = viewModel(), pagerState: PagerState, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = vm.GetDate(pagerState.currentPage),
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable {
                        navController.navigate("change_schedule") {
                            popUpTo("main")
                        }
                    },
            ) {
                Text(
                    text = vm.schedule.client,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
            }
        }
        IconButton(
            onClick = {},
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
fun Schedule(vm: MyViewModel = viewModel(), pagerState: PagerState) {
    Column {
        HorizontalPager(state = pagerState) { page ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(vm.schedule.schedule[page].classes) {
                        item -> Item(item)
                }
            }
        }

    }

}

@Composable
fun Item(item: ClassItem) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxSize()
    ){
        Row(
            modifier = Modifier
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
                    text = "08:45",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "10:15",
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
        if (item.number == 2) {
            Row(
                modifier = Modifier
                    .padding(end = 4.dp, start = 4.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "14:45 - 15:35",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = "Обеденный перерыв",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                )
            }
        }
    }
}