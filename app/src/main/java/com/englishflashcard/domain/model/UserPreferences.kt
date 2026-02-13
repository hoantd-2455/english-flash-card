package com.englishflashcard.domain.model

data class UserPreferences(
    val flashcardBackgroundColor: String = "#FFFFFF",
    val textSize: TextSize = TextSize.MEDIUM,
    val practiceShuffleEnabled: Boolean = true,
    val showHintsEnabled: Boolean = false
)

