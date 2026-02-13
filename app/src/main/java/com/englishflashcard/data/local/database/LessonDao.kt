package com.englishflashcard.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.englishflashcard.data.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY updated_at DESC")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    fun getLessonById(lessonId: Long): Flow<LessonEntity?>

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    suspend fun getLessonByIdSync(lessonId: Long): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity): Long

    @Update
    suspend fun updateLesson(lesson: LessonEntity)

    @Delete
    suspend fun deleteLesson(lesson: LessonEntity)

    @Query("DELETE FROM lessons WHERE id = :lessonId")
    suspend fun deleteLessonById(lessonId: Long)

    @Query("UPDATE lessons SET total_words = :count WHERE id = :lessonId")
    suspend fun updateWordCount(lessonId: Long, count: Int)
}

