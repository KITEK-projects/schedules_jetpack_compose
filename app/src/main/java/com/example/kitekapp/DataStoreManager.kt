package com.example.kitekapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DataStoreManager(private val context: Context) {

    companion object {
        val CLIENT = stringPreferencesKey("CLIENT")
        val isCuratorHour = booleanPreferencesKey("isCuratorHour")
        val selectLessonDirection = stringPreferencesKey("selectLessonDirection")
    }

    suspend fun saveToDataStore(settings: Settings) {
        context.datastore.edit {
            it[CLIENT] = settings.clientName
            it[isCuratorHour] = settings.isCuratorHour
            it[selectLessonDirection] = settings.selectedLessonDuration.toString()
        }
    }

    fun getFromDataStore() = context.datastore.data.map {
        Settings(
            clientName = it[CLIENT]?:"",
            isCuratorHour = it[isCuratorHour]?:true,
            selectedLessonDuration = (it[selectLessonDirection]?:"1").toInt()
        )


    }

}