package com.englishflashcard.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.englishflashcard.domain.model.TextSize
import com.englishflashcard.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesDataStore(private val context: Context) {

    private object PreferencesKeys {
        val FLASHCARD_BACKGROUND_COLOR = stringPreferencesKey("flashcard_background_color")
        val TEXT_SIZE = stringPreferencesKey("text_size")
        val PRACTICE_SHUFFLE_ENABLED = booleanPreferencesKey("practice_shuffle_enabled")
        val SHOW_HINTS_ENABLED = booleanPreferencesKey("show_hints_enabled")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            UserPreferences(
                flashcardBackgroundColor = preferences[PreferencesKeys.FLASHCARD_BACKGROUND_COLOR]
                    ?: "#FFFFFF",
                textSize = TextSize.valueOf(
                    preferences[PreferencesKeys.TEXT_SIZE] ?: TextSize.MEDIUM.name
                ),
                practiceShuffleEnabled = preferences[PreferencesKeys.PRACTICE_SHUFFLE_ENABLED]
                    ?: true,
                showHintsEnabled = preferences[PreferencesKeys.SHOW_HINTS_ENABLED]
                    ?: false
            )
        }

    suspend fun updateFlashcardColor(color: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FLASHCARD_BACKGROUND_COLOR] = color
        }
    }

    suspend fun updateTextSize(textSize: TextSize) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEXT_SIZE] = textSize.name
        }
    }

    suspend fun updatePracticeShuffle(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PRACTICE_SHUFFLE_ENABLED] = enabled
        }
    }

    suspend fun updateShowHints(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_HINTS_ENABLED] = enabled
        }
    }

    suspend fun resetToDefaults() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

