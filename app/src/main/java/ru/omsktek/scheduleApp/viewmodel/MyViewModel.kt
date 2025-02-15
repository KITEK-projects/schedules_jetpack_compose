package ru.omsktek.scheduleApp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.omsktek.scheduleApp.data.DataStoreManager
import ru.omsktek.scheduleApp.data.model.Schedule
import ru.omsktek.scheduleApp.data.model.SettingsData
import ru.omsktek.scheduleApp.data.network.ApiService
import kotlinx.coroutines.launch



class MyViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    var schedule by mutableStateOf(Schedule())
    var clientList by mutableStateOf<List<String>>(emptyList())
    var settingsData by mutableStateOf(SettingsData())

    var responseCode by mutableIntStateOf(0)
    var apiError by mutableStateOf("")
    var selectedTypeClient by mutableIntStateOf(0)


    val apiService = ApiService()

    var currentPage by mutableIntStateOf(0)
        private set
    fun updateCurrentPage(page: Int) {
        currentPage = page
    }

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
        updateCurrentPage(0)
        settingsData.clientName.let { apiService.getSchedule(this, it) }
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
