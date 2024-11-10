package com.example.kitekapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitekapp.retrofit2.ClientsApi
import com.example.kitekapp.retrofit2.ScheduleApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    val client: String = "None",
    val schedule: List<Schedule> = emptyList(),
)

class MyViewModel: ViewModel() {

    var schedule by mutableStateOf<Schedules>(Schedules())

    var error by mutableStateOf<Int?>(null)
    var messageError by mutableStateOf<String?>(null)

    var clients by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedIndex by mutableIntStateOf(0)

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    private fun updateSchedules(schedules: Schedules) {
        Log.d("updateSchedules", "Состояние обновляется: $schedules")
        schedule = schedules
    }

    fun updateClients(newClients: List<String>) {
        clients = newClients
    }

    private fun getClient(): OkHttpClient {
        return client
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://schedule.omsktec-playgrounds.ru/api/v1/")
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val scheduleApi = retrofit.create(ScheduleApi::class.java)
    private val clientsApi = retrofit.create(ClientsApi::class.java)

    fun getSchedule(client: String, time: String) {
        viewModelScope.launch {
            try {
                val answer = scheduleApi.getSchedule(client, time)

                if (answer.isSuccessful) {
                    Log.d("getSchedule", "Запрос успешен: ${answer.body()}")
                    answer.body()?.let { updateSchedules(it) }

                    error = null
                } else {
                    error = answer.code()
                    Log.d("getSchedule", "Ошибка ответа: ${error}")
                }
            } catch (e: Exception) {
                messageError = e.toString()
                Log.d("getSchedule", "Ошибка запроса: $messageError")
            }
        }
    }

    fun getClients(isTeacher: Boolean) {
        viewModelScope.launch {
            try {
                val answer = clientsApi.getClients(isTeacher)

                if (answer.isSuccessful) {
                    clients = answer.body()!!
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
        val date = LocalDate.parse(schedule.schedule[page].date)
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

//    fun getLessonTime(client: String): LessonTime {
//
//    }
//    fun getTimePair(number: Int,): String {
//
//    }
}