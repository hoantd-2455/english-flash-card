# Repository Contracts

**Feature**: 001-flashcard-learning-app  
**Date**: February 13, 2026

This document defines the contracts (interfaces) for data repositories in the application. These contracts define the boundary between the domain layer and data layer.

---

## LessonRepository

Manages CRUD operations for lessons.

```kotlin
interface LessonRepository {
    /**
     * Get all lessons, ordered by most recently updated
     * @return Flow of lesson list (reactive updates)
     */
    fun getAllLessons(): Flow<List<Lesson>>
    
    /**
     * Get a specific lesson by ID
     * @param lessonId Unique lesson identifier
     * @return Flow of lesson or null if not found
     */
    fun getLessonById(lessonId: Long): Flow<Lesson?>
    
    /**
     * Create a new lesson
     * @param lesson Lesson to create (id will be auto-generated)
     * @return ID of newly created lesson
     */
    suspend fun createLesson(lesson: Lesson): Long
    
    /**
     * Update an existing lesson
     * @param lesson Lesson with updated fields
     */
    suspend fun updateLesson(lesson: Lesson)
    
    /**
     * Delete a lesson (cascades to word pairs, progress, sessions, quiz results)
     * @param lessonId ID of lesson to delete
     */
    suspend fun deleteLesson(lessonId: Long)
    
    /**
     * Get lesson statistics (computed from related entities)
     * @param lessonId Lesson to compute stats for
     * @return Statistics including card counts, mastery, due reviews
     */
    suspend fun getLessonStats(lessonId: Long): LessonStats
}
```

---

## WordPairRepository

Manages word pairs within lessons.

```kotlin
interface WordPairRepository {
    /**
     * Get all word pairs for a lesson, ordered by orderIndex
     * @param lessonId Parent lesson ID
     * @return Flow of word pair list
     */
    fun getWordPairsByLesson(lessonId: Long): Flow<List<WordPair>>
    
    /**
     * Get a specific word pair by ID
     * @param wordPairId Word pair identifier
     * @return Word pair or null
     */
    suspend fun getWordPairById(wordPairId: Long): WordPair?
    
    /**
     * Create a new word pair in a lesson
     * @param wordPair Word pair to create
     * @return ID of newly created word pair
     */
    suspend fun createWordPair(wordPair: WordPair): Long
    
    /**
     * Update an existing word pair
     * @param wordPair Word pair with updated fields
     */
    suspend fun updateWordPair(wordPair: WordPair)
    
    /**
     * Delete a word pair (cascades to learning progress)
     * @param wordPairId ID of word pair to delete
     */
    suspend fun deleteWordPair(wordPairId: Long)
    
    /**
     * Reorder word pairs within a lesson
     * @param lessonId Lesson containing word pairs
     * @param newOrder List of word pair IDs in desired order
     */
    suspend fun reorderWordPairs(lessonId: Long, newOrder: List<Long>)
}
```

---

## LearningProgressRepository

Manages spaced repetition progress tracking.

```kotlin
interface LearningProgressRepository {
    /**
     * Get progress for a specific word pair
     * @param wordPairId Word pair to get progress for
     * @return Flow of progress record (auto-created if not exists)
     */
    fun getProgressForWordPair(wordPairId: Long): Flow<LearningProgress?>
    
    /**
     * Get all progress records for a lesson
     * @param lessonId Lesson to get progress for
     * @return Flow of progress list
     */
    fun getProgressForLesson(lessonId: Long): Flow<List<LearningProgress>>
    
    /**
     * Get word pairs that are due for review
     * @param lessonId Lesson to check (or null for all lessons)
     * @param currentTime Current timestamp for comparison
     * @return List of word pairs with progress where nextReviewDate <= currentTime
     */
    suspend fun getDueWordPairs(lessonId: Long?, currentTime: Long): List<WordPairWithProgress>
    
    /**
     * Update progress after a review
     * @param wordPairId Word pair that was reviewed
     * @param rating Difficulty rating (AGAIN, HARD, GOOD, EASY)
     * @param reviewTime Timestamp of review
     * @return Updated progress record with new interval and next review date
     */
    suspend fun recordReview(
        wordPairId: Long,
        rating: DifficultyRating,
        reviewTime: Long
    ): LearningProgress
    
    /**
     * Reset progress for a word pair (back to NEW state)
     * @param wordPairId Word pair to reset
     */
    suspend fun resetProgress(wordPairId: Long)
}

enum class DifficultyRating {
    AGAIN,  // Restart learning (interval = 1 day)
    HARD,   // Difficult (interval × 1.2)
    GOOD,   // Normal (interval × easiness factor)
    EASY    // Easy (interval × easiness factor × 1.3)
}

data class WordPairWithProgress(
    val wordPair: WordPair,
    val progress: LearningProgress
)
```

---

## PracticeSessionRepository

Records practice session history.

```kotlin
interface PracticeSessionRepository {
    /**
     * Start a new practice session
     * @param lessonId Lesson being practiced
     * @param startTime Session start timestamp
     * @return ID of new session
     */
    suspend fun startSession(lessonId: Long, startTime: Long): Long
    
    /**
     * Update session progress (called after each card)
     * @param sessionId Session being updated
     * @param cardsReviewed Total cards shown so far
     * @param cardsKnown Cards marked "know"
     * @param cardsUnknown Cards marked "don't know"
     */
    suspend fun updateSession(
        sessionId: Long,
        cardsReviewed: Int,
        cardsKnown: Int,
        cardsUnknown: Int
    )
    
    /**
     * Complete a practice session
     * @param sessionId Session to complete
     * @param endTime Session end timestamp
     */
    suspend fun completeSession(sessionId: Long, endTime: Long)
    
    /**
     * Get session history for a lesson
     * @param lessonId Lesson to get history for
     * @param limit Maximum number of sessions to return
     * @return Flow of recent sessions, ordered by start time descending
     */
    fun getSessionHistory(lessonId: Long, limit: Int = 10): Flow<List<PracticeSession>>
}
```

---

## QuizResultRepository

Records quiz/test results.

```kotlin
interface QuizResultRepository {
    /**
     * Save a completed quiz result
     * @param result Quiz result to save
     * @return ID of saved result
     */
    suspend fun saveQuizResult(result: QuizResult): Long
    
    /**
     * Get quiz history for a lesson
     * @param lessonId Lesson to get quiz history for
     * @param limit Maximum number of results to return
     * @return Flow of recent quiz results, ordered by timestamp descending
     */
    fun getQuizHistory(lessonId: Long, limit: Int = 10): Flow<List<QuizResult>>
    
    /**
     * Get average quiz score for a lesson
     * @param lessonId Lesson to compute average for
     * @return Average score percentage across all quizzes
     */
    suspend fun getAverageScore(lessonId: Long): Float
}
```

---

## PreferencesRepository

Manages user settings (DataStore-backed).

```kotlin
interface PreferencesRepository {
    /**
     * Get all user preferences as Flow (reactive)
     * @return Flow of current preferences
     */
    fun getPreferences(): Flow<UserPreferences>
    
    /**
     * Update flashcard background color
     * @param colorHex Hex color code (e.g., "#FFFFFF")
     */
    suspend fun setFlashcardColor(colorHex: String)
    
    /**
     * Update text size preference
     * @param textSize Text size enum value
     */
    suspend fun setTextSize(textSize: TextSize)
    
    /**
     * Update practice shuffle setting
     * @param enabled True to shuffle cards by default
     */
    suspend fun setPracticeShuffleEnabled(enabled: Boolean)
    
    /**
     * Reset all preferences to defaults
     */
    suspend fun resetToDefaults()
}

enum class TextSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}
```

---

## Data Classes

Supporting data classes used in repository contracts:

```kotlin
data class Lesson(
    val id: Long = 0,
    val title: String,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val totalWords: Int = 0
)

data class WordPair(
    val id: Long = 0,
    val lessonId: Long,
    val englishTerm: String,
    val vietnameseMeaning: String,
    val orderIndex: Int = 0,
    val createdAt: Long
)

data class LearningProgress(
    val id: Long = 0,
    val wordPairId: Long,
    val easinessFactor: Float = 2.5f,
    val interval: Int = 0,
    val repetitions: Int = 0,
    val lastReviewDate: Long? = null,
    val nextReviewDate: Long? = null,
    val masteryLevel: MasteryLevel = MasteryLevel.NEW,
    val reviewCount: Int = 0
)

enum class MasteryLevel {
    NEW,        // Never practiced
    LEARNING,   // In progress
    MASTERED    // Well-known (3+ repetitions, 21+ day interval)
}

data class PracticeSession(
    val id: Long = 0,
    val lessonId: Long,
    val startTime: Long,
    val endTime: Long? = null,
    val cardsReviewed: Int = 0,
    val cardsKnown: Int = 0,
    val cardsUnknown: Int = 0,
    val completedSuccessfully: Boolean = false
)

data class QuizResult(
    val id: Long = 0,
    val lessonId: Long,
    val timestamp: Long,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val multipleChoiceCount: Int = 0,
    val typingCount: Int = 0,
    val multipleChoiceCorrect: Int = 0,
    val typingCorrect: Int = 0,
    val scorePercentage: Float
)

data class LessonStats(
    val totalWords: Int,
    val newCards: Int,
    val learningCards: Int,
    val masteredCards: Int,
    val dueCards: Int,
    val averageScore: Float,
    val lastPracticeDate: Long?
)

data class UserPreferences(
    val flashcardBackgroundColor: String = "#FFFFFF",
    val textSize: TextSize = TextSize.MEDIUM,
    val practiceShuffleEnabled: Boolean = true,
    val showHintsEnabled: Boolean = false
)
```

---

## Contract Testing Requirements

Each repository implementation must pass these contract tests:

1. **CRUD Operations**: Create, Read, Update, Delete work correctly
2. **Cascade Deletes**: Foreign key cascades work as expected
3. **Reactive Updates**: Flow emissions occur when data changes
4. **Null Safety**: Nullable returns handled correctly
5. **Concurrent Access**: Thread-safe operations under concurrent load
6. **Transaction Integrity**: Multi-step operations are atomic

**Example Test Structure**:
```kotlin
@Test
fun `createLesson returns valid ID and lesson is retrievable`() = runTest {
    val lesson = Lesson(title = "Test", description = "Test lesson", ...)
    val id = repository.createLesson(lesson)
    
    assertTrue(id > 0)
    val retrieved = repository.getLessonById(id).first()
    assertNotNull(retrieved)
    assertEquals("Test", retrieved.title)
}
```

---

**Contracts Status**: ✅ COMPLETE - All repository interfaces defined

