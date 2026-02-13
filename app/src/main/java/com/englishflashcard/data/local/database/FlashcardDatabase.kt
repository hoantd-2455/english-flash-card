package com.englishflashcard.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.englishflashcard.data.local.entity.LearningProgressEntity
import com.englishflashcard.data.local.entity.LessonEntity
import com.englishflashcard.data.local.entity.PracticeSessionEntity
import com.englishflashcard.data.local.entity.QuizResultEntity
import com.englishflashcard.data.local.entity.WordPairEntity

@Database(
    entities = [
        LessonEntity::class,
        WordPairEntity::class,
        LearningProgressEntity::class,
        PracticeSessionEntity::class,
        QuizResultEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FlashcardDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao
    abstract fun wordPairDao(): WordPairDao
    abstract fun learningProgressDao(): LearningProgressDao
    abstract fun practiceSessionDao(): PracticeSessionDao
    abstract fun quizResultDao(): QuizResultDao
}

