package com.example.kitekapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.R

@Composable
fun Header(navController: NavController, text: String) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
            )
            IconButton(
                onClick = {
                    val canGoBack = navController.popBackStack()
                    if (!canGoBack) {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LunchItem(vm: MyViewModel, isSeniorCource: Boolean) {
    val time: MutableList<List<String>> = vm.calculateSecondLessonAndLunchBreak(false, isSeniorCource)
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${time[0][0]} - ${time[0][1]}",
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