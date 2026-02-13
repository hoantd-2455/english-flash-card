package com.englishflashcard.data.repository

import com.englishflashcard.data.local.database.LessonDao
import com.englishflashcard.data.local.database.WordPairDao
import com.englishflashcard.domain.model.Lesson
import com.englishflashcard.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonRepositoryImpl(
    private val lessonDao: LessonDao,
    private val wordPairDao: WordPairDao
) : LessonRepository {

    override fun getAllLessons(): Flow<List<Lesson>> {
        return lessonDao.getAllLessons().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLessonById(lessonId: Long): Flow<Lesson?> {
        return lessonDao.getLessonById(lessonId).map { it?.toDomain() }
    }

    override suspend fun createLesson(lesson: Lesson): Long {
        return lessonDao.insertLesson(lesson.toEntity())
    }

    override suspend fun updateLesson(lesson: Lesson) {
        lessonDao.updateLesson(lesson.toEntity())
        // Update word count
        val count = wordPairDao.getWordPairCountByLesson(lesson.id)
        lessonDao.updateWordCount(lesson.id, count)
    }

    override suspend fun deleteLesson(lessonId: Long) {
        lessonDao.deleteLessonById(lessonId)
    }
}

