package com.englishflashcard.ui.screens.lessonedit

/**
 * UI state for Lesson Edit/Create screen
 */
data class LessonEditUiState(
    val lessonId: Long = 0,
    val title: String = "",
    val description: String = "",
    val wordPairs: List<WordPairInput> = listOf(WordPairInput()),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val validationErrors: ValidationErrors = ValidationErrors()
)

/**
 * Word pair input state
 */
data class WordPairInput(
    val id: Long = 0,
    val english: String = "",
    val vietnamese: String = ""
)

/**
 * Validation errors for the form
 */
data class ValidationErrors(
    val titleError: String? = null,
    val wordPairErrors: Map<Int, String> = emptyMap()
) {
    val hasErrors: Boolean
        get() = titleError != null || wordPairErrors.isNotEmpty()
}

