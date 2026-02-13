package com.englishflashcard.ui.screens.lessonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.englishflashcard.domain.repository.LessonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Lesson List screen
 * Manages list of lessons and navigation to create/edit/detail screens
 */
class LessonListViewModel(
    private val lessonRepository: LessonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonListUiState())
    val uiState: StateFlow<LessonListUiState> = _uiState.asStateFlow()

    init {
        loadLessons()
    }

    fun loadLessons() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            lessonRepository.getAllLessons()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load lessons"
                        )
                    }
                }
                .collect { lessons ->
                    _uiState.update {
                        it.copy(
                            lessons = lessons,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun deleteLesson(lessonId: Long) {
        viewModelScope.launch {
            try {
                lessonRepository.deleteLesson(lessonId)
                // Lessons will auto-update via Flow
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

