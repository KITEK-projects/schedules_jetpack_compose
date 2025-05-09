package ru.omsktec.scheduleApp.data.network

import androidx.lifecycle.viewModelScope
import ru.omsktec.scheduleApp.data.model.Schedule
import ru.omsktec.scheduleApp.data.network.retrofit2.ClientsApi
import ru.omsktec.scheduleApp.data.network.retrofit2.ScheduleApi
import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ApiService {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://app.omsktec.ru/api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val scheduleApi = retrofit.create(ScheduleApi::class.java)
    private val clientsApi = retrofit.create(ClientsApi::class.java)


    //СЛИТЬ 4 В 2
    private suspend fun getScheduleApi(client: String): Response<Schedule> {
        return scheduleApi.getSchedule(client, LocalDate.now().atStartOfDay(ZoneOffset.UTC).format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME))
//        return scheduleApi.getSchedule(client, "2021-01-12T22:47:25+06:00")
    }

    private suspend fun getClientsApi(isTeacher: Boolean): Response<List<String>> {
        return clientsApi.getClients(isTeacher)
    }

    fun getSchedule(viewModel: MyViewModel, client: String) {
        viewModel.viewModelScope.launch {
            try {
                val response = getScheduleApi(client)
                if (response.isSuccessful) {
                    response.body()?.let { responseSchedule ->
                        viewModel.schedule = responseSchedule
                    }
                    viewModel.responseCode = response.code()
                } else {
                    viewModel.responseCode = response.code()
                }
            } catch (e: Exception) {
                viewModel.apiError = e.toString()
            }
        }
    }

    fun getClients(viewModel: MyViewModel, isTeacher: Int) {
        viewModel.viewModelScope.launch {
            try {
                val response = getClientsApi(isTeacher == 1)

                if (response.isSuccessful) {
                    viewModel.clientList = (response.body() ?: emptyList())
                } else {
                    viewModel.clientList = emptyList()
                }
            } catch (e: Exception) {
                viewModel.clientList = emptyList()
            }
        }
    }

}