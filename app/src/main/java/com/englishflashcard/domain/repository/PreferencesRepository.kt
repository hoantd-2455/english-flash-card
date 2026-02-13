package com.englishflashcard.domain.repository

import com.englishflashcard.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun updateFlashcardColor(color: String)
    suspend fun updateTextSize(textSize: com.englishflashcard.domain.model.TextSize)
    suspend fun updatePracticeShuffle(enabled: Boolean)
    suspend fun resetToDefaults()
}

