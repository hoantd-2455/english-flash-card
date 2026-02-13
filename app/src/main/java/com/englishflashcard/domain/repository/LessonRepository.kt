package com.englishflashcard.domain.repository

import com.englishflashcard.domain.model.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonRepository {
    fun getAllLessons(): Flow<List<Lesson>>
    fun getLessonById(lessonId: Long): Flow<Lesson?>
    suspend fun createLesson(lesson: Lesson): Long
    suspend fun updateLesson(lesson: Lesson)
    suspend fun deleteLesson(lessonId: Long)
}
