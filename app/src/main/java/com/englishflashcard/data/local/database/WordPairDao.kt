package com.englishflashcard.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.englishflashcard.data.local.entity.WordPairEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordPairDao {
    @Query("SELECT * FROM word_pairs WHERE lesson_id = :lessonId ORDER BY order_index ASC")
    fun getWordPairsByLesson(lessonId: Long): Flow<List<WordPairEntity>>

    @Query("SELECT * FROM word_pairs WHERE lesson_id = :lessonId ORDER BY order_index ASC")
    suspend fun getWordPairsByLessonSync(lessonId: Long): List<WordPairEntity>

    @Query("SELECT * FROM word_pairs WHERE id = :wordPairId")
    suspend fun getWordPairById(wordPairId: Long): WordPairEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordPair(wordPair: WordPairEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordPairs(wordPairs: List<WordPairEntity>): List<Long>

    @Update
    suspend fun updateWordPair(wordPair: WordPairEntity)

    @Delete
    suspend fun deleteWordPair(wordPair: WordPairEntity)

    @Query("DELETE FROM word_pairs WHERE lesson_id = :lessonId")
    suspend fun deleteWordPairsByLesson(lessonId: Long)

    @Query("SELECT COUNT(*) FROM word_pairs WHERE lesson_id = :lessonId")
    suspend fun getWordPairCountByLesson(lessonId: Long): Int
}

