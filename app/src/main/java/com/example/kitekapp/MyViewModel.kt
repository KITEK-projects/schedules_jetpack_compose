package com.example.kitekapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
    var schedule by mutableStateOf(Schedules(
        "ОО-31",
        listOf(
            Schedule(
                "2021-10-11",
                listOf(
                    ClassItem(
                        2,
                        "управление командой проекта",
                        "вариатив",
                        "Марченко Е. А.",
                        ""
                    ),
                    ClassItem(
                        3,
                        "организация и технология производства продукции оп.",
                        "модуль 1",
                        "Коптева Л. С.",
                        "12"
                    ),
                    ClassItem(
                        4,
                        "организация и технология производства продукции оп.",
                        "модуль 1",
                        "Коптева Л. С.",
                        "12"
                    ),
                    ClassItem(
                        5,
                        "психология и этика проф. д-ти",
                        "лекция",
                        "Марченко Е. А.",
                        ""
                    )
                )
            ),
            Schedule(
                "2021-10-12",
                listOf(
                    ClassItem(
                        4,
                        "организация и технология производства продукции оп.",
                        "модуль 1",
                        "Коптева Л. С.",
                        "12"
                    ),
                    ClassItem(
                        5,
                        "организация и технология производства продукции оп.",
                        "модуль 1",
                        "Коптева Л. С.",
                        "12"
                    )
                )
            )
        )
    ))
}