package com.englishflashcard.ui.screens.lessonlist

import com.englishflashcard.domain.model.Lesson

/**
 * UI state for the Lesson List screen
 */
data class LessonListUiState(
    val lessons: List<Lesson> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val recommendedLessonId: Long? = null
)

