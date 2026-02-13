package com.englishflashcard.data.repository

import com.englishflashcard.data.local.database.QuizResultDao
import com.englishflashcard.domain.model.QuizResult
import com.englishflashcard.domain.repository.QuizResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizResultRepositoryImpl(
    private val quizResultDao: QuizResultDao
) : QuizResultRepository {

    override suspend fun saveResult(result: QuizResult): Long {
        return quizResultDao.insertResult(result.toEntity())
    }

    override fun getResultsByLesson(lessonId: Long): Flow<List<QuizResult>> {
        return quizResultDao.getResultsByLesson(lessonId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAverageScore(lessonId: Long, limit: Int): Float? {
        return quizResultDao.getAverageScore(lessonId, limit)
    }
}

