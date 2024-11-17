package com.example.kitekapp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.kitekapp.ClassItem
import com.example.kitekapp.MyViewModel
import com.example.kitekapp.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LunchItem(item: ClassItem, vm: MyViewModel, date: String) {
    val secondLessonEnd = LocalTime.parse(item.time[0])
    val isSeniorCourse = when (vm.typeClient(vm.schedule.client)) {
        "teach" -> when (vm.typeClient(item.partner)) {
            "1-2" -> false
            "3-4" -> true
            else -> false
        }

        "1-2" -> false
        "3-4" -> true
        else -> false
    }
    val schedule = mutableListOf<String>()

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val lunchStart = secondLessonEnd.plusMinutes((45 + if (isSeniorCourse) 45 else 0).toLong())
    val lunchEnd = lunchStart
        .plusMinutes(if (vm.settings.value!!.isCuratorHour && vm.isMonday(date)) 35 else 30)
        .plusMinutes(if (isSeniorCourse) 10 else 0)

    schedule.add(lunchStart.format(timeFormatter))
    schedule.add(lunchEnd.format(timeFormatter))

    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${schedule[0]} - ${schedule[1]}",
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

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(top = 16.dp),
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
        )
    }
}

@Composable
fun FirstStart(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_change),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground
        )
        CustomButton(
            "Выбрать расписание"
        ) {
            navController.navigate("change_schedule") {
                popUpTo("main")
            }
        }
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
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = 2.5.dp,
        )
    }
}

@Composable
fun ErrorsScreen(
    navController: NavController,
    vm: MyViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(48.dp)
        )
        when (vm.error) {
            400, 404 -> {
                CustomButton(
                    "Выбрать расписание"
                ) {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                }
            }
            else -> {

            }
        }
        Text(
            text = when (vm.error) {
                400 -> "Такого клиента нет, выберите другого"
                404 -> "Нет расписания, можно выбрать другое)"
                500 -> "Ошибка сервера, тут только молится"
                null -> "Нет подключения, лол)"
                else -> "${vm.error}, ${vm.messageError}"
            },
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}