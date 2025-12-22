package com.project.googlegemini.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(private val context: Context) {
    companion object {
        private val IS_LIGHT_MODE = booleanPreferencesKey("is_light_mode")
    }

    val isLightMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LIGHT_MODE] ?: false // Default is dark mode (false = dark, true = light)
    }

    suspend fun setLightMode(isLight: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LIGHT_MODE] = isLight
        }
    }
}
