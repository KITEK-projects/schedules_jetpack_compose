package ru.omsktec.scheduleApp.utils

import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getDate(viewModel: MyViewModel, page: Int): String {
    val date = LocalDate.parse(viewModel.schedule.schedules[page].date)
    val formatter = DateTimeFormatter.ofPattern("E, d MMMM", Locale("ru"))
    return date.format(formatter)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun isMonday(date: String): Boolean {
    val ldate = LocalDate.parse(date)
    return ldate.dayOfWeek == DayOfWeek.MONDAY
}

fun isFirstCourse(groupName: String): Boolean {
    val firstDigit = groupName.firstOrNull { it.isDigit() } ?: return false
    return firstDigit == '1' || firstDigit == '2'
}