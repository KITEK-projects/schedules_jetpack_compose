package com.example.kitekapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ClassItem(
    val time: List<String>,
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
    val clientName: String = "",
    val isCuratorHour: Boolean = true
)


class MyViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _settings = MutableStateFlow<Settings?>(null)
    val settings: StateFlow<Settings?> get() = _settings

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            dataStoreManager.getFromDataStore()
                .collect { settings ->
                    _settings.value = settings
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveSettings(settings: Settings) {
        viewModelScope.launch {
            dataStoreManager.saveToDataStore(settings)
            updateTime(schedule)
        }
    }


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

    private fun updateSchedules(schedules: Schedules) {
        schedule = schedules
    }

    fun updateClients(newClients: List<String>) {
        clients = newClients
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectLessonDuration(upd: Int) {
        selectLessonDuration = upd
        updateTime(schedule)
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
    fun getSchedule(client: String) {
        updateSchedules(Schedules()) // Сбрасываем текущее расписание
        viewModelScope.launch {
            try {
                val answer = scheduleApi.getSchedule(
                    client,
                    LocalDate.now().minusDays(1).atStartOfDay(ZoneOffset.UTC).format(
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )

                if (answer.isSuccessful) {
                    answer.body()?.let { responseSchedule ->
                        updateTime(responseSchedule)
                        error = null
                    }
                } else {
                    error = answer.code()
                }
            } catch (e: Exception) {
                messageError = e.toString()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTime(schedules: Schedules) {
        viewModelScope.launch {
            val updatedSchedules = schedules.schedule.map { lschedule ->
                val updatedClasses = lschedule.classes.map { classItem ->
                    val generatedTime = calculateLessonSchedule(
                        classItem.number - 1,
                        isCuratorHour = if (_settings.value?.isCuratorHour == true) isMonday(lschedule.date) else false,
                        isSeniorCourse = when (typeClient(schedules.client)) {
                            "teach" -> when (typeClient(classItem.partner)) {
                                "1-2" -> false
                                "3-4" -> true
                                else -> false
                            }

                            "1-2" -> false
                            "3-4" -> true
                            else -> false
                        }
                    )
                    classItem.copy(time = generatedTime)
                }
                Schedule(lschedule.date, updatedClasses)
            }

            // Обновляем глобальное расписание
            updateSchedules(Schedules(schedules.client, updatedSchedules))
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
    fun isMonday(date: String): Boolean {
        val ldate = LocalDate.parse(date) // 2021-10-11
        return ldate.dayOfWeek == DayOfWeek.MONDAY
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
    fun calculateLessonSchedule(
        number: Int,
        isCuratorHour: Boolean = true, // Есть ли кураторский час
        isSeniorCourse: Boolean = true // "3-4" курс
    ): List<String> {

        val lessonDuration = when (selectLessonDuration) {
            0 -> 80
            1 -> 90
            2 -> 70
            else -> 90
        }

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Начальное время первой пары
        var currentTime = LocalTime.of(8, 45)

        // Список для результатов
        val schedule = mutableListOf<List<String>>()

        // Добавляем первую пару
        val firstLessonEnd = currentTime.plusMinutes(lessonDuration.toLong())
        schedule.add(
            listOf(
                currentTime.format(timeFormatter),
                firstLessonEnd.format(timeFormatter)
            )
        )
        currentTime = firstLessonEnd.plusMinutes(10) // Время до второй пары

        // Добавляем кураторский час, если есть
        if (isCuratorHour) {
            currentTime = currentTime.plusMinutes(40)
        }

        // Вторая пара
        val secondLessonEnd =
            currentTime.plusMinutes(
                lessonDuration.toLong()
                        + if (!isSeniorCourse) if (isCuratorHour) 35 else 30 else 0)
        schedule.add(
            listOf(
                currentTime.format(timeFormatter),
                secondLessonEnd.format(timeFormatter)
            )
        )


        // Третья пара
        currentTime =
            secondLessonEnd.plusMinutes((10 + if (isSeniorCourse) 30 + if (isCuratorHour) 5 else 0 else 0).toLong())
        val thirdLessonEnd = currentTime.plusMinutes(lessonDuration.toLong())
        schedule.add(
            listOf(
                currentTime.format(timeFormatter),
                thirdLessonEnd.format(timeFormatter)
            )
        )

        // Четвертая пара
        currentTime = thirdLessonEnd.plusMinutes(10)
        val fourthLessonEnd = currentTime.plusMinutes(lessonDuration.toLong())
        schedule.add(
            listOf(
                currentTime.format(timeFormatter),
                fourthLessonEnd.format(timeFormatter)
            )
        )

        // Пятая пара
        currentTime = fourthLessonEnd.plusMinutes(10)
        val fifthLessonEnd = currentTime.plusMinutes(lessonDuration.toLong())
        schedule.add(
            listOf(
                currentTime.format(timeFormatter),
                fifthLessonEnd.format(timeFormatter)
            )
        )

        // Шестая пара
        currentTime = fifthLessonEnd.plusMinutes(5)
        val sixthLessonEnd = currentTime.plusMinutes(lessonDuration.toLong())
        schedule.add(
            listOf(
                currentTime.format(timeFormatter),
                sixthLessonEnd.format(timeFormatter)
            )
        )

        return schedule[number]
    }

//    fun getLessonTime(client: String): LessonTime {
//
//    }
//    fun getTimePair(number: Int,): String {
//
//    }
}