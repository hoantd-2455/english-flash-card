package com.englishflashcard.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.englishflashcard.data.local.entity.LearningProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningProgressDao {
    @Query("SELECT * FROM learning_progress WHERE word_pair_id = :wordPairId")
    fun getProgressByWordPair(wordPairId: Long): Flow<LearningProgressEntity?>

    @Query("SELECT * FROM learning_progress WHERE word_pair_id = :wordPairId")
    suspend fun getProgressByWordPairSync(wordPairId: Long): LearningProgressEntity?

    @Query("""
        SELECT lp.* FROM learning_progress lp
        INNER JOIN word_pairs wp ON lp.word_pair_id = wp.id
        WHERE wp.lesson_id = :lessonId
    """)
    fun getProgressByLesson(lessonId: Long): Flow<List<LearningProgressEntity>>

    @Query("""
        SELECT lp.* FROM learning_progress lp
        INNER JOIN word_pairs wp ON lp.word_pair_id = wp.id
        WHERE wp.lesson_id = :lessonId
    """)
    suspend fun getProgressByLessonSync(lessonId: Long): List<LearningProgressEntity>

    @Query("SELECT * FROM learning_progress WHERE next_review_date <= :currentTime")
    suspend fun getDueProgress(currentTime: Long): List<LearningProgressEntity>

    @Query("""
        SELECT COUNT(*) FROM learning_progress lp
        INNER JOIN word_pairs wp ON lp.word_pair_id = wp.id
        WHERE wp.lesson_id = :lessonId AND lp.next_review_date <= :currentTime
    """)
    suspend fun getDueCountByLesson(lessonId: Long, currentTime: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: LearningProgressEntity): Long

    @Update
    suspend fun updateProgress(progress: LearningProgressEntity)

    @Query("DELETE FROM learning_progress WHERE word_pair_id = :wordPairId")
    suspend fun deleteProgressByWordPair(wordPairId: Long)
}

