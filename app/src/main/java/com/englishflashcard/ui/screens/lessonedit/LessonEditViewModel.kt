package com.englishflashcard.ui.screens.lessonedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.englishflashcard.domain.model.Lesson
import com.englishflashcard.domain.model.WordPair
import com.englishflashcard.domain.repository.LessonRepository
import com.englishflashcard.domain.repository.WordPairRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MIN_WORD_PAIRS = 1

/**
 * ViewModel for Lesson Create/Edit screen
 * Handles lesson creation and modification with word pairs
 */
class LessonEditViewModel(
    private val lessonRepository: LessonRepository,
    private val wordPairRepository: WordPairRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonEditUiState())
    val uiState: StateFlow<LessonEditUiState> = _uiState.asStateFlow()

    fun loadLesson(lessonId: Long) {
        if (lessonId == 0L) return // New lesson

        viewModelScope.launch {
            try {
                // Get lesson once instead of collecting
                val lesson = lessonRepository.getLessonById(lessonId).firstOrNull()

                if (lesson != null) {
                    // Get word pairs once instead of collecting
                    val words = wordPairRepository.getWordPairsByLesson(lessonId).firstOrNull() ?: emptyList()

                    _uiState.update {
                        it.copy(
                            lessonId = lesson.id,
                            title = lesson.title,
                            description = lesson.description ?: "",
                            wordPairs = words.map { word ->
                                WordPairInput(
                                    id = word.id,
                                    english = word.englishTerm,
                                    vietnamese = word.vietnameseMeaning
                                )
                            }.ifEmpty { listOf(WordPairInput()) }
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to load lesson: ${e.message}")
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                validationErrors = it.validationErrors.copy(titleError = null)
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateWordPair(index: Int, english: String, vietnamese: String) {
        val currentPairs = _uiState.value.wordPairs.toMutableList()
        if (index in currentPairs.indices) {
            currentPairs[index] = currentPairs[index].copy(
                english = english,
                vietnamese = vietnamese
            )
            _uiState.update {
                it.copy(
                    wordPairs = currentPairs,
                    validationErrors = it.validationErrors.copy(
                        wordPairErrors = it.validationErrors.wordPairErrors - index
                    )
                )
            }
        }
    }

    fun addWordPair() {
        _uiState.update {
            it.copy(wordPairs = it.wordPairs + WordPairInput())
        }
    }

    fun removeWordPair(index: Int) {
        val currentPairs = _uiState.value.wordPairs.toMutableList()
        if (currentPairs.size > MIN_WORD_PAIRS && index in currentPairs.indices) {
            currentPairs.removeAt(index)
            _uiState.update { it.copy(wordPairs = currentPairs) }
        }
    }

    fun saveLesson(onSuccess: () -> Unit) {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                val state = _uiState.value
                val currentTime = System.currentTimeMillis()

                val lesson = Lesson(
                    id = state.lessonId,
                    title = state.title.trim(),
                    description = state.description.trim().ifBlank { null },
                    createdAt = if (state.lessonId == 0L) currentTime else 0L,
                    updatedAt = currentTime,
                    totalWords = state.wordPairs.size
                )

                val lessonId = if (state.lessonId == 0L) {
                    lessonRepository.createLesson(lesson)
                } else {
                    lessonRepository.updateLesson(lesson)
                    state.lessonId
                }

                // Save word pairs
                val wordPairs = state.wordPairs.mapIndexed { index, input ->
                    WordPair(
                        id = input.id,
                        lessonId = lessonId,
                        englishTerm = input.english.trim(),
                        vietnameseMeaning = input.vietnamese.trim(),
                        orderIndex = index,
                        createdAt = currentTime
                    )
                }

                // Delete existing words and insert new ones
                wordPairRepository.deleteWordPairsByLesson(lessonId)
                wordPairRepository.createWordPairs(wordPairs)

                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Failed to save lesson: ${e.message}"
                    )
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value
        val errors = mutableMapOf<Int, String>()

        val titleError = when {
            state.title.isBlank() -> "Title cannot be empty"
            else -> null
        }

        state.wordPairs.forEachIndexed { index, pair ->
            when {
                pair.english.isBlank() && pair.vietnamese.isBlank() -> {
                    errors[index] = "Both fields cannot be empty"
                }
                pair.english.isBlank() -> {
                    errors[index] = "English word cannot be empty"
                }
                pair.vietnamese.isBlank() -> {
                    errors[index] = "Vietnamese translation cannot be empty"
                }
            }
        }

        _uiState.update {
            it.copy(
                validationErrors = ValidationErrors(
                    titleError = titleError,
                    wordPairErrors = errors
                )
            )
        }

        return titleError == null && errors.isEmpty()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}


