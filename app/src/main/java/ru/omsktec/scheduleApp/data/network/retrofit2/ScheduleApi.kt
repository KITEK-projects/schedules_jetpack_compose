package ru.omsktec.scheduleApp.data.network.retrofit2

import ru.omsktec.scheduleApp.data.model.Schedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ScheduleApi {
    @GET("schedule/")
    suspend fun getSchedule(
        @Query("client_name") client: String,
        @Header("X-CLIENT-TIME") time: String
        ): Response<Schedule>
}