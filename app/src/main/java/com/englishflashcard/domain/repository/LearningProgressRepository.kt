package com.englishflashcard.domain.repository

import com.englishflashcard.domain.model.DifficultyRating
import com.englishflashcard.domain.model.LearningProgress
import kotlinx.coroutines.flow.Flow

interface LearningProgressRepository {
    fun getProgressForWordPair(wordPairId: Long): Flow<LearningProgress?>
    fun getProgressForLesson(lessonId: Long): Flow<List<LearningProgress>>
    suspend fun getDueCountByLesson(lessonId: Long, currentTime: Long): Int
    suspend fun recordReview(
        wordPairId: Long,
        rating: DifficultyRating,
        reviewTime: Long
    ): LearningProgress
    suspend fun resetProgress(wordPairId: Long)
    suspend fun createProgress(progress: LearningProgress): Long
    suspend fun updateProgress(progress: LearningProgress)
}

