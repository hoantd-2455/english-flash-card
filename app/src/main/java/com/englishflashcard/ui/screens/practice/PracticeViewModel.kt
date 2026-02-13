package com.englishflashcard.ui.screens.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.englishflashcard.domain.repository.LessonRepository
import com.englishflashcard.domain.repository.WordPairRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Practice screen
 * Manages flashcard practice session with swipe gestures
 */
class PracticeViewModel(
    private val lessonRepository: LessonRepository,
    private val wordPairRepository: WordPairRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    fun loadLesson(lessonId: Long, shuffleCards: Boolean = true) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val lesson = lessonRepository.getLessonById(lessonId).firstOrNull()
                if (lesson == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Lesson not found"
                        )
                    }
                    return@launch
                }

                val words = wordPairRepository.getWordPairsByLesson(lessonId).firstOrNull() ?: emptyList()
                val cards = if (shuffleCards) words.shuffled() else words

                _uiState.update {
                    it.copy(
                        lessonTitle = lesson.title,
                        cards = cards,
                        isLoading = false,
                        currentCardIndex = 0,
                        isFlipped = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load lesson: ${e.message}"
                    )
                }
            }
        }
    }

    fun flipCard() {
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }

    fun markAsKnown() {
        _uiState.update {
            val newIndex = it.currentCardIndex + 1
            val isComplete = newIndex >= it.cards.size

            it.copy(
                currentCardIndex = newIndex,
                knownCount = it.knownCount + 1,
                isFlipped = false,
                isSessionComplete = isComplete
            )
        }
    }

    fun markAsUnknown() {
        _uiState.update {
            val newIndex = it.currentCardIndex + 1
            val isComplete = newIndex >= it.cards.size

            it.copy(
                currentCardIndex = newIndex,
                unknownCount = it.unknownCount + 1,
                isFlipped = false,
                isSessionComplete = isComplete
            )
        }
    }

    fun resetSession() {
        val cards = _uiState.value.cards
        _uiState.update {
            PracticeUiState(
                lessonTitle = it.lessonTitle,
                cards = cards.shuffled(),
                isLoading = false
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

