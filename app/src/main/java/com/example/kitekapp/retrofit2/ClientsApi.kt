package com.example.kitekapp.retrofit2

import com.example.kitekapp.Schedules
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ClientsApi {
    @GET("clients/")
    suspend fun getSchedule(
        @Query("is_teacher") isTeacher: Boolean,
    ): Response<Schedules>
}