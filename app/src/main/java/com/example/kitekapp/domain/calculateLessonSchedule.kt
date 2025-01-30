package com.example.kitekapp.domain

import com.example.kitekapp.utils.isFirstCourse
import com.example.kitekapp.utils.isMonday
import com.example.kitekapp.viewmodel.MyViewModel

fun calculateLessonSchedule(
    viewModel: MyViewModel,
    number: Int,
    partner: String,
    date: String
): String {
    val hasCuratorialHour = viewModel.settingsData.isCuratorHour && isMonday(date)
    val isFirstYear = isFirstCourse(if(viewModel.schedule.isTeacher) partner else viewModel.schedule.clientName)

    val baseSchedule = if (hasCuratorialHour) {
        listOf(
            "08:45-10:15", // 1 пара
            "11:05-12:35", // 2 пара
            "13:20-14:50", // 3 пара
            "15:00-16:30", // 4 пара
            "16:40-18:10", // 5 пара
            "18:15-19:45", // 6 пара
        )
    } else {
        listOf(
            "08:45-10:15", // 1 пара
            "10:25-11:55", // 2 пара
            "12:35-14:05", // 3 пара
            "14:15-15:45", // 4 пара
            "15:55-17:25", // 5 пара
            "17:30-19:00", // 6 пара
        )
    }


    val baseTime = baseSchedule.getOrNull(number - 1) ?: return "Некорректный номер пары"

    if (number == 2) {
        return if (isFirstYear) {
            if (hasCuratorialHour) {
                val firstPart = "11:05-11:50"
                val secondPart = "12:25-13:10"
                "$firstPart  $secondPart - $number пара"
            } else {
                val firstPart = "10:25-11:10"
                val secondPart = "11:40-12:25"
                "$firstPart  $secondPart - $number пара"
            }
        } else {
            "$baseTime - $number пара"
        }
    }

    return "$baseTime - $number пара"
}
