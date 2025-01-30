package com.example.kitekapp.viewmodel

import android.util.Log
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitekapp.data.DataStoreManager
import com.example.kitekapp.data.model.Schedule
import com.example.kitekapp.data.model.SettingsData
import com.example.kitekapp.data.network.ApiService
import kotlinx.coroutines.launch



class MyViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    var schedule by mutableStateOf(Schedule())
    var clientList by mutableStateOf<List<String>>(emptyList())
    var settingsData by mutableStateOf(SettingsData())

    var responseCode by mutableIntStateOf(0)
    var apiError by mutableStateOf("")
    var selectedTypeClient by mutableIntStateOf(0)


    val apiService = ApiService()

    init {
        viewModelScope.launch {
            loadSettingsData()
        }
    }


    fun updateSettingsData(
        clientName: String? = null,
        isCuratorHour: Boolean? = null,
    ) {
        this.settingsData = SettingsData(
            clientName ?: this.settingsData.clientName,
            isCuratorHour ?: this.settingsData.isCuratorHour,
        )

        saveSettingsData()
        settingsData.clientName?.let { apiService.getSchedule(this, it) }
    }


    private fun saveSettingsData() {
        viewModelScope.launch {
            dataStoreManager.saveToDataStore(settingsData)
        }
    }

    private suspend fun loadSettingsData() {
        dataStoreManager.getFromDataStore().collect { settings ->
            Log.d("MyViewModel", "Loaded settings: $settings")
            settingsData = settings
        }
    }
}
