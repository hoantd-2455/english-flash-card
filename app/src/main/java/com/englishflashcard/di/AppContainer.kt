package com.englishflashcard.di

import android.content.Context
import androidx.room.Room
import com.englishflashcard.data.local.database.FlashcardDatabase
import com.englishflashcard.data.local.preferences.PreferencesDataStore
import com.englishflashcard.data.repository.LearningProgressRepositoryImpl
import com.englishflashcard.data.repository.LessonRepositoryImpl
import com.englishflashcard.data.repository.PracticeSessionRepositoryImpl
import com.englishflashcard.data.repository.PreferencesRepositoryImpl
import com.englishflashcard.data.repository.QuizResultRepositoryImpl
import com.englishflashcard.data.repository.WordPairRepositoryImpl
import com.englishflashcard.domain.repository.LearningProgressRepository
import com.englishflashcard.domain.repository.LessonRepository
import com.englishflashcard.domain.repository.PracticeSessionRepository
import com.englishflashcard.domain.repository.PreferencesRepository
import com.englishflashcard.domain.repository.QuizResultRepository
import com.englishflashcard.domain.repository.WordPairRepository

/**
 * Dependency injection container for the app
 * Provides repositories and other dependencies
 */
class AppContainer(private val context: Context) {

    // Database
    private val database: FlashcardDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            FlashcardDatabase::class.java,
            "flashcard_database"
        ).build()
    }

    // Repositories
    val lessonRepository: LessonRepository by lazy {
        LessonRepositoryImpl(
            lessonDao = database.lessonDao(),
            wordPairDao = database.wordPairDao()
        )
    }

    val wordPairRepository: WordPairRepository by lazy {
        WordPairRepositoryImpl(database.wordPairDao())
    }

    val learningProgressRepository: LearningProgressRepository by lazy {
        LearningProgressRepositoryImpl(database.learningProgressDao())
    }

    val practiceSessionRepository: PracticeSessionRepository by lazy {
        PracticeSessionRepositoryImpl(database.practiceSessionDao())
    }

    val quizResultRepository: QuizResultRepository by lazy {
        QuizResultRepositoryImpl(database.quizResultDao())
    }

    val preferencesRepository: PreferencesRepository by lazy {
        PreferencesRepositoryImpl(PreferencesDataStore(context))
    }
}

