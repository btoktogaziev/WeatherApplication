package com.example.weatherapplication.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "saved")

class DataStoreManager(private val context: Context) {

    private val CITY_KEY = stringPreferencesKey("city_name")

    suspend fun saveCity(cityName: String) {
        context.dataStore.edit { prefs ->
            prefs[CITY_KEY] = cityName
        }
    }

    val city: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[CITY_KEY] ?: ""
        }
}