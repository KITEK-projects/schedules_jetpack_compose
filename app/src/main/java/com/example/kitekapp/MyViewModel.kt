package com.example.kitekapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitekapp.retrofit2.ScheduleApi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ClassItem(
    val number: Int,
    val title: String,
    val type: String,
    val partner: String,
    val location: String
)

data class Schedule(
    val date: String,
    val classes: List<ClassItem>
)

data class Schedules(
    val client: String,
    val schedule: List<Schedule>,
)

class MyViewModel: ViewModel() {
    var schedule by mutableStateOf<Schedules?>(null)

    var error by mutableStateOf<Int?>(null)
    var messageError by mutableStateOf<String?>(null)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://schedule.omsktec-playgrounds.ru/api/v1/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val scheduleApi = retrofit.create(ScheduleApi::class.java)

    fun getSchedule() {
        viewModelScope.launch {
            try {
                val answer = scheduleApi.getSchedule("ИСР-41", "20210411T010000+0600")

                if (answer.isSuccessful) {
                    schedule = answer.body()
                    error = null
                } else {
                    error = answer.code()
                }
            } catch (e: Exception) {
                messageError = e.toString()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(page: Int): String {
        val date = LocalDate.parse("2021-10-11")
        if (date.isEqual(LocalDate.now())) {
            return "Сегодня"
        }
        else if (date.isEqual(LocalDate.now().minusDays(1))) {
            return "Вчера"
        }
        else if (date.isEqual(LocalDate.now().plusDays(1))) {
            return "Завтра"
        }
        else {
            val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
            return date.format(formatter)
        }
    }

    fun typeClient(input: String): String {
        if (input.contains('.')) {
            return "teach"
        } else {
            val digits = input.filter { it.isDigit() }
            return if ( digits != "") {
                when (digits.first()) {
                    '1', '2' -> "1-2"
                    '3', '4' -> "3-4"
                    else -> {
                        "Empty"
                    }
                }
            } else {
                "Not"
            }
        }
    }
//    fun getTimePair(number: Int,): String {
//
//    }
}