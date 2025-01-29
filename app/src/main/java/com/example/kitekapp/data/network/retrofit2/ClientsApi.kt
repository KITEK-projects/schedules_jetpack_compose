package com.example.kitekapp.data.network.retrofit2

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientsApi {
    @GET("clients/")
    suspend fun getClients(
        @Query("is_teacher") isTeacher: Boolean,
    ): Response<List<String>>
}