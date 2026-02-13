package com.englishflashcard.ui.screens.lessondetail

import com.englishflashcard.domain.model.Lesson
import com.englishflashcard.domain.model.WordPair

/**
 * UI state for Lesson Detail screen
 */
data class LessonDetailUiState(
    val lesson: Lesson? = null,
    val wordPairs: List<WordPair> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val progressStats: ProgressStats = ProgressStats()
)

/**
 * Progress statistics for a lesson
 */
data class ProgressStats(
    val totalWords: Int = 0,
    val dueCards: Int = 0,
    val learningCards: Int = 0,
    val masteredCards: Int = 0
)

