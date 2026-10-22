package com.metro.launcher.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.launcherDataStore by preferencesDataStore(name = "metro_launcher")

class LauncherPreferences(private val context: Context) {
    val note: Flow<String> = context.launcherDataStore.data.map { values ->
        values[NOTE_KEY].orEmpty()
    }
    val highScore: Flow<Int> = context.launcherDataStore.data.map { values ->
        values[HIGH_SCORE_KEY] ?: 0
    }

    suspend fun saveNote(value: String) {
        context.launcherDataStore.edit { values ->
            values[NOTE_KEY] = value
        }
    }

    suspend fun saveHighScore(value: Int) {
        context.launcherDataStore.edit { values ->
            if (value > (values[HIGH_SCORE_KEY] ?: 0)) {
                values[HIGH_SCORE_KEY] = value
            }
        }
    }

    private companion object {
        val NOTE_KEY = stringPreferencesKey("note")
        val HIGH_SCORE_KEY = intPreferencesKey("2048_high_score")
    }
}
