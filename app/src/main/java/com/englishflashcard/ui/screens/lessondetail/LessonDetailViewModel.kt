package com.englishflashcard.ui.screens.lessondetail

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
 * ViewModel for Lesson Detail screen
 * Displays lesson information, word pairs, and progress statistics
 */
class LessonDetailViewModel(
    private val lessonRepository: LessonRepository,
    private val wordPairRepository: WordPairRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonDetailUiState())
    val uiState: StateFlow<LessonDetailUiState> = _uiState.asStateFlow()

    fun loadLesson(lessonId: Long) {
        viewModelScope.launch {
            println("DEBUG: loadLesson started for lessonId=$lessonId")
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            println("DEBUG: isLoading set to true")

            try {
                // Get lesson once instead of collecting
                println("DEBUG: Fetching lesson...")
                val lesson = lessonRepository.getLessonById(lessonId).firstOrNull()
                println("DEBUG: Lesson fetched: ${lesson?.title}")

                if (lesson != null) {
                    // Get word pairs once instead of collecting
                    println("DEBUG: Fetching word pairs...")
                    val words = wordPairRepository.getWordPairsByLesson(lessonId).firstOrNull() ?: emptyList()
                    println("DEBUG: Word pairs fetched: ${words.size} words")

                    _uiState.update {
                        println("DEBUG: Updating UI state with data")
                        it.copy(
                            lesson = lesson,
                            wordPairs = words,
                            isLoading = false,
                            progressStats = ProgressStats(
                                totalWords = words.size
                                // Progress stats will be populated by US3
                            )
                        )
                    }
                    println("DEBUG: UI state updated, isLoading=false")
                } else {
                    println("DEBUG: Lesson not found!")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Lesson not found"
                        )
                    }
                }
            } catch (e: Exception) {
                println("DEBUG: Error loading lesson: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load lesson: ${e.message}"
                    )
                }
            }
        }
    }

    fun deleteLesson(onDeleted: () -> Unit) {
        val lessonId = _uiState.value.lesson?.id ?: return

        viewModelScope.launch {
            try {
                lessonRepository.deleteLesson(lessonId)
                onDeleted()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete lesson: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}


