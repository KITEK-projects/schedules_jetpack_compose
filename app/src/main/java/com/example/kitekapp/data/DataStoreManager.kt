package com.example.kitekapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kitekapp.data.model.SettingsData
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DataStoreManager(private val context: Context) {

    companion object {
        val clientName = stringPreferencesKey("CLIENT")
        val isCuratorHour = booleanPreferencesKey("isCuratorHour")
    }

    suspend fun saveToDataStore(settingsData: SettingsData) {
        context.datastore.edit {
            it[clientName] = settingsData.clientName
            it[isCuratorHour] = settingsData.isCuratorHour
        }
    }

    fun getFromDataStore() = context.datastore.data.map {
        SettingsData(
            clientName = it[clientName]?:"None",
            isCuratorHour = it[isCuratorHour]?:true,
        )
    }

}