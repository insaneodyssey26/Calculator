package com.masum.calculatorbasic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "calculator_history")

class HistoryDataStore(private val context: Context) {
    
    private object PreferencesKeys {
        val HISTORY = stringPreferencesKey("calculation_history")
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    val historyFlow: Flow<List<CalculationHistory>> = context.dataStore.data
        .map { preferences ->
            try {
                val historyJson = preferences[PreferencesKeys.HISTORY] ?: "[]"
                json.decodeFromString<List<CalculationHistory>>(historyJson)
            } catch (e: Exception) {
                emptyList()
            }
        }
    
    suspend fun saveHistory(history: List<CalculationHistory>) {
        try {
            val historyJson = json.encodeToString(history)
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.HISTORY] = historyJson
            }
        } catch (e: Exception) {
            // Handle error silently for now
        }
    }
    
    suspend fun clearHistory() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.HISTORY)
        }
    }
}
