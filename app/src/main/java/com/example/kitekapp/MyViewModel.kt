package com.example.kitekapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitekapp.retrofit2.ClientsApi
import com.example.kitekapp.retrofit2.ScheduleApi
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
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
import java.time.format.DateTimeFormatter
import java.util.Locale

data class LessonItem(
    val title: String,
    val type: String,
    val partner: String,
    val location: String?
)

data class Lesson(
    val time: List<String>,
    val number: Int,
    val items: List<LessonItem>
)

data class Lessons(
    val date: String,
    val lessons: List<Lesson>
)

data class Schedules(
    val clientName: String = "None",
    val schedules: List<Lessons> = emptyList(),
)

data class Settings(
    val clientName: String = "",
    val isCuratorHour: Boolean = true,
    val selectedLessonDuration: Int = 1
)

data class Error(
    var scheduleCodeError: Int = 0,
    var scheduleMessageError: String = "",
    var clientsCodeError: Int = 0,
    var clientsMessageError: String = "",
) {
    val isScheduleDefault: Boolean
        get() = scheduleCodeError == 0 && scheduleMessageError == ""

    val isClientsSuccessful: Boolean
        get() = clientsCodeError in listOf(0, 200) && clientsMessageError == ""
}


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

    fun saveSettings(settings: Settings) {
        viewModelScope.launch {
            dataStoreManager.saveToDataStore(settings)
            updateTime(schedules)
        }
    }


    var schedules by mutableStateOf(Schedules())

    var error by mutableStateOf(Error())

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
        this.schedules = schedules
    }

    private fun updateError(
        scheduleCodeError: Int? = null,
        scheduleMessageError: String? = null,
        clientsCodeError: Int? = null,
        clientsMessageError: String? = null,
    ) {
        error = Error(
            scheduleCodeError = scheduleCodeError ?: error.scheduleCodeError,
            scheduleMessageError = scheduleMessageError ?: error.clientsMessageError,
            clientsCodeError = clientsCodeError ?: error.clientsCodeError,
            clientsMessageError = clientsMessageError ?: error.clientsMessageError
        )
    }

    fun updateSelectLessonDuration(upd: Int) {
        viewModelScope.launch {
            selectLessonDuration = upd
            saveSettings(
                Settings(
                    clientName = settings.value!!.clientName,
                    isCuratorHour = settings.value!!.isCuratorHour,
                    selectedLessonDuration = upd
                )
            )
            updateTime(schedules)
        }
    }

    private fun getClient(): OkHttpClient {
        return client
    }

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://schedule.omsktec-playgrounds.ru/api/v1/")
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val scheduleApi = retrofit.create(ScheduleApi::class.java)
    private val clientsApi = retrofit.create(ClientsApi::class.java)

    fun getSchedule(client: String) {
        updateSchedules(Schedules())
        viewModelScope.launch {
            try {
                val answer = scheduleApi.getSchedule(
                    client,
//                    LocalDate.now().atStartOfDay(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    "2021-01-12T22:47:25+06:00"
                )

                if (answer.isSuccessful) {
                    answer.body()?.let { responseSchedule ->
                        println(responseSchedule)
                        updateTime(responseSchedule)
                        updateError(scheduleCodeError = 0)
                    }
                } else {
                    updateError(scheduleCodeError = answer.code())
                }
            } catch (e: Exception) {
                updateError(scheduleMessageError = e.toString())
            }
        }
    }

    private fun updateTime(schedules: Schedules) {
        viewModelScope.launch {
            val updatedLessons = schedules.schedules.map { lschedule ->
                val updatedLessonsItem = lschedule.lessons.map { lessonItem ->
                    val generatedTime = calculateLessonSchedule(
                        lessonItem.number - 1,
                        isCuratorHour = if (_settings.value?.isCuratorHour == true) isMonday(lschedule.date) else false,
                        isSeniorCourse = when (typeClient(schedules.clientName)) {
                            "teach" -> when (typeClient(lessonItem.items.first().partner)) {
                                "1-2" -> false
                                "3-4" -> true
                                else -> false
                            }
                            "1-2" -> false
                            "3-4" -> true
                            else -> false
                        }
                    )
                    println("Generated time for lesson: $generatedTime")
                    lessonItem.copy(time = generatedTime)
                }
                Lessons(lschedule.date, updatedLessonsItem)
            }

            updateSchedules(Schedules(schedules.clientName, updatedLessons))
        }
    }

    fun getClients(isTeacher: Int) {
        clients = emptyList()
        fun Int.toBoolean(): Boolean = this == 1
        viewModelScope.launch {
            try {
                val answer = clientsApi.getClients(isTeacher.toBoolean())

                if (answer.isSuccessful) {
                    clients = answer.body()!!

                    updateError(clientsCodeError = 0)
                } else {
                    updateError(clientsCodeError = answer.code())
                    clients = emptyList()
                }
            } catch (e: Exception) {
                updateError(clientsMessageError = e.toString())
                clients = emptyList()
            }
        }
    }

    fun getDate(page: Int): String {
        val date = LocalDate.parse(schedules.schedules[page].date)
        val formatter = DateTimeFormatter.ofPattern("E d MMMM", Locale("ru"))
        return date.format(formatter)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

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

    private fun calculateLessonSchedule(
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
                        + if (!isSeniorCourse) if (isCuratorHour) 35 else 30 else 0
            )
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
}