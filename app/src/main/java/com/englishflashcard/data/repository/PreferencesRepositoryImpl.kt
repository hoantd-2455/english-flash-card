package com.englishflashcard.data.repository

import com.englishflashcard.data.local.preferences.PreferencesDataStore
import com.englishflashcard.domain.model.TextSize
import com.englishflashcard.domain.model.UserPreferences
import com.englishflashcard.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val preferencesDataStore: PreferencesDataStore
) : PreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> {
        return preferencesDataStore.userPreferences
    }

    override suspend fun updateFlashcardColor(color: String) {
        preferencesDataStore.updateFlashcardColor(color)
    }

    override suspend fun updateTextSize(textSize: TextSize) {
        preferencesDataStore.updateTextSize(textSize)
    }

    override suspend fun updatePracticeShuffle(enabled: Boolean) {
        preferencesDataStore.updatePracticeShuffle(enabled)
    }

    override suspend fun resetToDefaults() {
        preferencesDataStore.resetToDefaults()
    }
}

