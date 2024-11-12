package com.example.kitekapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitekapp.retrofit2.ClientsApi
import com.example.kitekapp.retrofit2.ScheduleApi
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
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

data class Settings(
    val clientName: String = "ИСР-21",
    val isCuratorHour: Boolean = true
)


class MyViewModel : ViewModel() {


    var schedule by mutableStateOf(Schedules())

    var error by mutableStateOf<Int?>(null)
    var messageError by mutableStateOf<String?>(null)

    var clients by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedIndex by mutableIntStateOf(0)

    var selectLessonDuration by mutableIntStateOf(1)

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    var timeItems = mutableStateListOf<List<String>>()

    private fun updateSchedules(schedules: Schedules) {
        schedule = schedules
    }

    private fun updateTimeItems(time: List<List<String>>) {
        timeItems.clear()
        timeItems.addAll(time)
    }

    fun updateClients(newClients: List<String>) {
        clients = newClients
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectLessonDuration(upd: Int) {
        selectLessonDuration = upd
        updateTimeItems(calculateLessonTimes())
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSchedule(client: String, time: String) {
        updateSchedules(Schedules())
        viewModelScope.launch {
            try {
                val answer = scheduleApi.getSchedule(client, time)

                if (answer.isSuccessful) {
                    answer.body()?.let { updateSchedules(it) }
                    updateTimeItems(calculateLessonTimes())
                    error = null
                } else {
                    error = answer.code()
                }
            } catch (e: Exception) {
                messageError = e.toString()
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
        } else if (date.isEqual(LocalDate.now().minusDays(1))) {
            return "Вчера"
        } else if (date.isEqual(LocalDate.now().plusDays(1))) {
            return "Завтра"
        } else {
            val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
            return date.format(formatter)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun isMonday(page: Int): Boolean {
        val date = LocalDate.parse(schedule.schedule[page].date) // 2021-10-11
        return date.dayOfWeek == DayOfWeek.MONDAY
    }

    fun typeClient(input: String): String {
        if (input.contains('.')) {
            return "teach"
        } else {
            val digits = input.filter { it.isDigit() }
            return when (digits.first()) {
                '1', '2' -> "1-2"
                '3', '4' -> "3-4"
                else -> {
                    "Empty"
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateLessonTimes(): MutableList<List<String>> {

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        var currentTime = LocalTime.parse("08:45", timeFormatter)

        val schedule = mutableListOf<List<String>>()

        // Функция для добавления временных промежутков пары
        fun addLesson(start: LocalTime, duration: Int): List<String> {
            val end = start.plusMinutes(duration.toLong())
            return listOf(start.format(timeFormatter), end.format(timeFormatter))
        }

        val lessonDirection = when(selectLessonDuration) {
            0 -> 80
            1 -> 90
            2 -> 70
            else -> 90
        }

        // Первая пара
        val firstLesson = addLesson(currentTime, lessonDirection)
        schedule.add(firstLesson)

        // Вторая пара
        schedule.add(listOf("", ""))

        // Обеденный перерыв
        schedule.add(0, listOf("", ""))

        // Третья пара
        currentTime = LocalTime.parse(firstLesson[1], timeFormatter).plusMinutes(140)
        val thirdLesson = addLesson(currentTime, lessonDirection)
        schedule.add(thirdLesson)

        // Четвертая пара
        currentTime = LocalTime.parse(thirdLesson[1], timeFormatter).plusMinutes(10)
        val fourthLesson = addLesson(currentTime, lessonDirection)
        schedule.add(fourthLesson)

        // Пятая пара
        currentTime = LocalTime.parse(fourthLesson[1], timeFormatter).plusMinutes(10)
        val fifthLesson = addLesson(currentTime, lessonDirection)
        schedule.add(fifthLesson)

        // Шестая пара
        currentTime = LocalTime.parse(fifthLesson[1], timeFormatter).plusMinutes(5)
        val sixthLesson = addLesson(currentTime, lessonDirection)
        schedule.add(sixthLesson)

        return schedule
    }

    // Функция для вычисления второй пары и обеденного перерыва
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateSecondLessonAndLunchBreak(
        isCuratorHour: Boolean,
        isSeniorCourse: Boolean
    ): MutableList<List<String>> {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val schedule = mutableListOf(
            listOf("", ""),
            listOf("", "")
        )

        val lessonDirection = when(selectLessonDuration) {
            0 -> 80
            1 -> 90
            2 -> 70
            else -> 90
        }

        // Первая пара из списка
        val firstLessonEnd = LocalTime.parse(timeItems[1][1], timeFormatter)

        // Начало второй пары (конец первой пары + 10 минут + кураторский час)
        var secondLessonStart = firstLessonEnd.plusMinutes(10)
        if (isCuratorHour) {
            secondLessonStart = secondLessonStart.plusMinutes(40)
        }

        // Продолжительность второй пары
        var secondLessonDuration = if (isSeniorCourse) lessonDirection else lessonDirection + 30
        secondLessonDuration = if (isCuratorHour) secondLessonDuration + 5 else lessonDirection

        val secondLesson = listOf(
            secondLessonStart.format(timeFormatter),
            secondLessonStart.plusMinutes(secondLessonDuration.toLong()).format(timeFormatter)
        )
        schedule[1] = secondLesson // Заменяем временное значение

        // Обеденный перерыв
        val lunchStart = secondLessonStart.plusMinutes(45)
        val adjustedLunchStart = if (isSeniorCourse) lunchStart.plusMinutes(45) else lunchStart
        val lunchEnd = adjustedLunchStart.plusMinutes(30)
        var adjustedLunchEnd = if (isSeniorCourse) lunchEnd.plusMinutes(10) else lunchEnd
        adjustedLunchEnd = if (isCuratorHour) adjustedLunchEnd.plusMinutes(5) else adjustedLunchEnd
        schedule[0] = listOf(adjustedLunchStart.format(timeFormatter), adjustedLunchEnd.format(timeFormatter))

        return schedule
    }



    fun getTimeItem(number: Int): List<String> {
        return when (number) {
            1 -> timeItems[1]
            2 -> timeItems[2]
            3 -> timeItems[3]
            4 -> timeItems[4]
            5 -> timeItems[5]
            6 -> timeItems[6]
            else -> {
                timeItems[0]
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