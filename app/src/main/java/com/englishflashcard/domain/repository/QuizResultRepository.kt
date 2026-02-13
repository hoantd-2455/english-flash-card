package com.englishflashcard.domain.repository

import com.englishflashcard.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface QuizResultRepository {
    suspend fun saveResult(result: QuizResult): Long
    fun getResultsByLesson(lessonId: Long): Flow<List<QuizResult>>
    suspend fun getAverageScore(lessonId: Long, limit: Int = 5): Float?
}

