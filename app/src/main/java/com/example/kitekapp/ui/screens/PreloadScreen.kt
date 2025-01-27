package com.example.kitekapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kitekapp.viewmodel.MyViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PreloadScreen(viewModel: MyViewModel) {
    if (viewModel.responseCode == 0 && viewModel.apiError == "") {
        if (viewModel.settingsData.clientName != "") {
            viewModel.apiService.getSchedule(viewModel, viewModel.settingsData.clientName)
            IsLoading()
        } else {
            Text(
                text = "ВЫБРАТЬ РАСПИСАНИЕ",
                color = Color.White
            )
        }

    } else {
        Text(
            text = "${viewModel.responseCode} \n\n${viewModel.apiError}",
            color = Color.White
        )
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