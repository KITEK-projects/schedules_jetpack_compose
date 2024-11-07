package com.example.kitekapp.retrofit2

import com.example.kitekapp.Schedules
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("schedule/")
    suspend fun getSchedule(
        @Query("client") client: String,
        @Header("X-CLIENT-TIME") time: String
        ): Response<Schedules>
}