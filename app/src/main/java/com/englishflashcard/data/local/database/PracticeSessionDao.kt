package com.englishflashcard.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.englishflashcard.data.local.entity.PracticeSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeSessionDao {
    @Query("SELECT * FROM practice_sessions WHERE lesson_id = :lessonId ORDER BY start_time DESC")
    fun getSessionsByLesson(lessonId: Long): Flow<List<PracticeSessionEntity>>

    @Query("SELECT * FROM practice_sessions WHERE lesson_id = :lessonId ORDER BY start_time DESC")
    suspend fun getSessionsByLessonSync(lessonId: Long): List<PracticeSessionEntity>

    @Query("SELECT * FROM practice_sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): PracticeSessionEntity?

    @Query("""
        SELECT * FROM practice_sessions 
        WHERE lesson_id = :lessonId AND completed_successfully = 1 
        ORDER BY start_time DESC 
        LIMIT 1
    """)
    suspend fun getLastCompletedSession(lessonId: Long): PracticeSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PracticeSessionEntity): Long

    @Update
    suspend fun updateSession(session: PracticeSessionEntity)

    @Query("DELETE FROM practice_sessions WHERE lesson_id = :lessonId")
    suspend fun deleteSessionsByLesson(lessonId: Long)
}

