package com.example.kitekapp.ui.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),

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
            modifier = Modifier.size(121.dp)
        )
        Text(
            text = when (vm.error.scheduleCodeError) {
                400 -> "Кажется, что-то пошло не так"
                404 -> "Похоже, что расписание пока нет"
                500 -> "Возникла критическая ошибка, расписание не доступно"
                0 -> "Нет подключения к интернету"
                else -> "FATAL ERROR"
            },
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = when (vm.error.scheduleCodeError) {
                400 -> "К сожалению, произошла ошибка, и мы не смогли обработать ваш запрос."
                404 -> "К сожалению, мы не можем найти расписание по вашему запросу. Возможно, оно еще не доступно или было удалено."
                500 -> "К сожалению, возникла внутренняя ошибка сервера, и мы не можем получить доступ к расписанию в данный момент."
                0 -> "К сожалению у вас нет доступа к интернету, выполните подключение и повторите попытку"
                else -> "${vm.error}, ${vm.error.scheduleMessageError}"
            },
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp)
        )
        when (vm.error.scheduleCodeError) {
            0 -> {
                CustomButton(
                    "Повторить попытку"
                ) {
                    vm.getSchedule(vm.settings.value!!.clientName)
                }
            }
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
                CustomButton(
                    "Выбрать расписание"
                ) {
                    navController.navigate("change_schedule") {
                        popUpTo("main")
                    }
                }
            }
        }
    }
}