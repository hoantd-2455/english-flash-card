package com.englishflashcard.data.repository

import com.englishflashcard.data.local.database.PracticeSessionDao
import com.englishflashcard.data.local.entity.PracticeSessionEntity
import com.englishflashcard.domain.model.PracticeSession
import com.englishflashcard.domain.repository.PracticeSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PracticeSessionRepositoryImpl(
    private val sessionDao: PracticeSessionDao
) : PracticeSessionRepository {

    override suspend fun startSession(lessonId: Long, startTime: Long): Long {
        val session = PracticeSessionEntity(
            lessonId = lessonId,
            startTime = startTime
        )
        return sessionDao.insertSession(session)
    }

    override suspend fun updateSession(session: PracticeSession) {
        sessionDao.updateSession(session.toEntity())
    }

    override suspend fun completeSession(sessionId: Long, endTime: Long) {
        val session = sessionDao.getSessionById(sessionId)
        if (session != null) {
            sessionDao.updateSession(
                session.copy(
                    endTime = endTime,
                    completedSuccessfully = true
                )
            )
        }
    }

    override fun getSessionsByLesson(lessonId: Long): Flow<List<PracticeSession>> {
        return sessionDao.getSessionsByLesson(lessonId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLastCompletedSession(lessonId: Long): PracticeSession? {
        return sessionDao.getLastCompletedSession(lessonId)?.toDomain()
    }
}

