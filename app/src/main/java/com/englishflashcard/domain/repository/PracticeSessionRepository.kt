package com.englishflashcard.domain.repository

import com.englishflashcard.domain.model.PracticeSession
import kotlinx.coroutines.flow.Flow

interface PracticeSessionRepository {
    suspend fun startSession(lessonId: Long, startTime: Long): Long
    suspend fun updateSession(session: PracticeSession)
    suspend fun completeSession(sessionId: Long, endTime: Long)
    fun getSessionsByLesson(lessonId: Long): Flow<List<PracticeSession>>
    suspend fun getLastCompletedSession(lessonId: Long): PracticeSession?
}

