package ru.omsktec.scheduleApp.data.model

data class LessonItem(
    val title: String,
    val type: String,
    val partner: String,
    val location: String?
)

data class Lesson(
    val number: Int,
    val items: List<LessonItem>
)

data class Lessons(
    val date: String,
    val lessons: List<Lesson>
)

data class Schedule(
    val clientName: String = "",
    val isTeacher: Boolean = false,
    val schedules: List<Lessons> = emptyList(),
)