package com.englishflashcard.ui.screens.practice

import com.englishflashcard.domain.model.WordPair

/**
 * UI state for Practice screen
 */
data class PracticeUiState(
    val lessonTitle: String = "",
    val cards: List<WordPair> = emptyList(),
    val currentCardIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val isSessionComplete: Boolean = false,
    val knownCount: Int = 0,
    val unknownCount: Int = 0,
    val errorMessage: String? = null
) {
    val currentCard: WordPair?
        get() = cards.getOrNull(currentCardIndex)

    val progress: Float
        get() = if (cards.isEmpty()) 0f else currentCardIndex.toFloat() / cards.size

    val totalCards: Int
        get() = cards.size

    val cardsRemaining: Int
        get() = maxOf(0, cards.size - currentCardIndex)
}

