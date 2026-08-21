package com.example.learnvault.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Creates a single instance of DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "learnvault_preferences")

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val READING_DENSITY = stringPreferencesKey("reading_density")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            themeMode = ThemeMode.valueOf(preferences[THEME_MODE] ?: ThemeMode.SYSTEM.name),
            readingDensity = ReadingDensity.valueOf(preferences[READING_DENSITY] ?: ReadingDensity.COMFORTABLE.name)
        )
    }

    suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode.name
        }
    }

    suspend fun updateReadingDensity(readingDensity: ReadingDensity) {
        dataStore.edit { preferences ->
            preferences[READING_DENSITY] = readingDensity.name
        }
    }
}