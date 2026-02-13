package com.englishflashcard.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.englishflashcard.data.local.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results WHERE lesson_id = :lessonId ORDER BY timestamp DESC")
    fun getResultsByLesson(lessonId: Long): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE lesson_id = :lessonId ORDER BY timestamp DESC")
    suspend fun getResultsByLessonSync(lessonId: Long): List<QuizResultEntity>

    @Query("SELECT * FROM quiz_results WHERE id = :resultId")
    suspend fun getResultById(resultId: Long): QuizResultEntity?

    @Query("""
        SELECT AVG(score_percentage) FROM (
            SELECT score_percentage FROM quiz_results 
            WHERE lesson_id = :lessonId 
            ORDER BY timestamp DESC 
            LIMIT :limit
        )
    """)
    suspend fun getAverageScore(lessonId: Long, limit: Int = 5): Float?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity): Long

    @Query("DELETE FROM quiz_results WHERE lesson_id = :lessonId")
    suspend fun deleteResultsByLesson(lessonId: Long)
}

