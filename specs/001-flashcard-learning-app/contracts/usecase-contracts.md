# Use Case Contracts

**Feature**: 001-flashcard-learning-app  
**Date**: February 13, 2026

This document defines the business logic contracts (use cases) that orchestrate repository operations to fulfill feature requirements.

---

## SpacedRepetitionUseCase

Implements the SM-2 algorithm for spaced repetition scheduling.

```kotlin
interface SpacedRepetitionUseCase {
    /**
     * Calculate next review schedule based on user's difficulty rating
     * 
     * @param currentProgress Current learning progress for the word
     * @param rating User's difficulty rating (AGAIN, HARD, GOOD, EASY)
     * @param reviewTime Timestamp when review occurred
     * @return Updated progress with new interval, easiness factor, next review date
     */
    fun calculateNextReview(
        currentProgress: LearningProgress,
        rating: DifficultyRating,
        reviewTime: Long
    ): LearningProgress
    
    /**
     * Get recommended lesson to study based on due cards and progress
     * 
     * @param allLessons List of all available lessons
     * @param currentTime Current timestamp
     * @return Lesson with most due cards, or null if no lessons have due cards
     */
    suspend fun getRecommendedLesson(
        allLessons: List<Lesson>,
        currentTime: Long
    ): Lesson?
}
```

**SM-2 Algorithm Details**:

```kotlin
fun calculateNextReview(
    currentProgress: LearningProgress,
    rating: DifficultyRating,
    reviewTime: Long
): LearningProgress {
    var easinessFactor = currentProgress.easinessFactor
    var interval = currentProgress.interval
    var repetitions = currentProgress.repetitions
    
    when (rating) {
        DifficultyRating.AGAIN -> {
            // Restart learning
            repetitions = 0
            interval = 1  // 1 day
            easinessFactor = max(1.3f, easinessFactor - 0.2f)
        }
        
        DifficultyRating.HARD -> {
            repetitions++
            interval = max(1, (interval * 1.2f).toInt())
            easinessFactor = max(1.3f, easinessFactor - 0.15f)
        }
        
        DifficultyRating.GOOD -> {
            repetitions++
            if (repetitions == 1) {
                interval = 1
            } else if (repetitions == 2) {
                interval = 6
            } else {
                interval = (interval * easinessFactor).toInt()
            }
        }
        
        DifficultyRating.EASY -> {
            repetitions++
            if (repetitions == 1) {
                interval = 4
            } else {
                interval = (interval * easinessFactor * 1.3f).toInt()
            }
            easinessFactor = min(3.0f, easinessFactor + 0.15f)
        }
    }
    
    // Determine mastery level
    val masteryLevel = when {
        repetitions == 0 -> MasteryLevel.NEW
        repetitions >= 3 && interval >= 21 -> MasteryLevel.MASTERED
        else -> MasteryLevel.LEARNING
    }
    
    val nextReviewDate = reviewTime + (interval * 24 * 60 * 60 * 1000L)
    
    return currentProgress.copy(
        easinessFactor = easinessFactor,
        interval = interval,
        repetitions = repetitions,
        lastReviewDate = reviewTime,
        nextReviewDate = nextReviewDate,
        masteryLevel = masteryLevel,
        reviewCount = currentProgress.reviewCount + 1
    )
}
```

---

## QuizGenerationUseCase

Generates quiz questions with mixed formats (multiple-choice and typing).

```kotlin
interface QuizGenerationUseCase {
    /**
     * Generate a quiz for a lesson with mixed question types
     * 
     * @param lessonId Lesson to generate quiz for
     * @param wordPairs All word pairs in the lesson
     * @return List of quiz questions (multiple-choice and typing mixed)
     */
    suspend fun generateQuiz(
        lessonId: Long,
        wordPairs: List<WordPair>
    ): List<QuizQuestion>
    
    /**
     * Validate user's answer to a typing question
     * 
     * @param userAnswer User's typed answer
     * @param correctAnswer Expected correct answer
     * @return True if answer is correct (case-insensitive, trimmed)
     */
    fun validateTypingAnswer(
        userAnswer: String,
        correctAnswer: String
    ): Boolean
}

sealed class QuizQuestion {
    abstract val wordPair: WordPair
    abstract val questionNumber: Int
    
    /**
     * Multiple-choice question showing English term with 4 Vietnamese options
     */
    data class MultipleChoice(
        override val wordPair: WordPair,
        override val questionNumber: Int,
        val options: List<String>,  // 4 options, shuffled
        val correctOptionIndex: Int // Index of correct answer in options list
    ) : QuizQuestion()
    
    /**
     * Typing question showing English term with text input for Vietnamese meaning
     */
    data class Typing(
        override val wordPair: WordPair,
        override val questionNumber: Int
    ) : QuizQuestion()
}
```

**Quiz Generation Logic**:

```kotlin
suspend fun generateQuiz(
    lessonId: Long,
    wordPairs: List<WordPair>
): List<QuizQuestion> {
    val questions = mutableListOf<QuizQuestion>()
    val shuffledWords = wordPairs.shuffled()
    
    // Determine question type distribution
    if (wordPairs.size < 4) {
        // Typing-only for small lessons
        shuffledWords.forEachIndexed { index, wordPair ->
            questions.add(QuizQuestion.Typing(wordPair, index + 1))
        }
    } else {
        // 50/50 mix for lessons with 4+ words
        val halfSize = shuffledWords.size / 2
        
        // First half: Multiple-choice
        shuffledWords.take(halfSize).forEachIndexed { index, wordPair ->
            val wrongAnswers = wordPairs
                .filter { it.id != wordPair.id }
                .shuffled()
                .take(3)
                .map { it.vietnameseMeaning }
            
            val allOptions = (wrongAnswers + wordPair.vietnameseMeaning).shuffled()
            val correctIndex = allOptions.indexOf(wordPair.vietnameseMeaning)
            
            questions.add(
                QuizQuestion.MultipleChoice(
                    wordPair = wordPair,
                    questionNumber = index + 1,
                    options = allOptions,
                    correctOptionIndex = correctIndex
                )
            )
        }
        
        // Second half: Typing
        shuffledWords.drop(halfSize).forEachIndexed { index, wordPair ->
            questions.add(
                QuizQuestion.Typing(
                    wordPair = wordPair,
                    questionNumber = halfSize + index + 1
                )
            )
        }
    }
    
    return questions.shuffled()  // Mix MC and typing questions
}

fun validateTypingAnswer(userAnswer: String, correctAnswer: String): Boolean {
    return userAnswer.trim().lowercase() == correctAnswer.trim().lowercase()
}
```

---

## PracticeFlowUseCase

Manages practice session flow with smart card ordering.

```kotlin
interface PracticeFlowUseCase {
    /**
     * Get cards for practice session, prioritizing due reviews
     * 
     * @param lessonId Lesson to practice
     * @param shuffleEnabled Whether to shuffle cards
     * @param currentTime Current timestamp for checking due cards
     * @return Ordered list of word pairs with progress
     */
    suspend fun getPracticeCards(
        lessonId: Long,
        shuffleEnabled: Boolean,
        currentTime: Long
    ): List<WordPairWithProgress>
    
    /**
     * Record a card review during practice
     * 
     * @param sessionId Current practice session ID
     * @param wordPairId Word pair being reviewed
     * @param known Whether user swiped right (know) or left (don't know)
     * @param reviewTime Timestamp of review
     */
    suspend fun recordCardReview(
        sessionId: Long,
        wordPairId: Long,
        known: Boolean,
        reviewTime: Long
    )
}
```

**Practice Card Ordering Logic**:

```kotlin
suspend fun getPracticeCards(
    lessonId: Long,
    shuffleEnabled: Boolean,
    currentTime: Long
): List<WordPairWithProgress> {
    // Get all word pairs with progress for the lesson
    val allCards = wordPairRepository.getWordPairsByLesson(lessonId)
        .first()
        .map { wordPair ->
            val progress = progressRepository.getProgressForWordPair(wordPair.id).first()
                ?: LearningProgress(wordPairId = wordPair.id)  // Create if not exists
            WordPairWithProgress(wordPair, progress)
        }
    
    // Separate into due and not-due cards
    val dueCards = allCards.filter { 
        it.progress.nextReviewDate?.let { it <= currentTime } ?: true 
    }
    val futurCards = allCards.filter { 
        it.progress.nextReviewDate?.let { it > currentTime } ?: false 
    }
    
    // Prioritize due cards, then add future cards
    val orderedCards = if (shuffleEnabled) {
        dueCards.shuffled() + futurCards.shuffled()
    } else {
        dueCards.sortedBy { it.progress.nextReviewDate } + 
        futurCards.sortedBy { it.wordPair.orderIndex }
    }
    
    return orderedCards
}
```

---

## LessonStatsUseCase

Computes statistics and metrics for lessons.

```kotlin
interface LessonStatsUseCase {
    /**
     * Get comprehensive statistics for a lesson
     * 
     * @param lessonId Lesson to compute stats for
     * @return Computed statistics including mastery, due cards, scores
     */
    suspend fun getLessonStats(lessonId: Long): LessonStats
    
    /**
     * Get progress metrics across all lessons
     * 
     * @return Summary of user's overall learning progress
     */
    suspend fun getOverallProgress(): OverallProgress
}

data class OverallProgress(
    val totalLessons: Int,
    val totalWords: Int,
    val totalMastered: Int,
    val totalDueToday: Int,
    val averageScore: Float,
    val studyStreak: Int  // Consecutive days with practice
)
```

---

## LessonManagementUseCase

Orchestrates complex lesson operations.

```kotlin
interface LessonManagementUseCase {
    /**
     * Create a new lesson with word pairs
     * 
     * @param title Lesson title
     * @param description Optional description
     * @param wordPairs Initial word pairs
     * @return ID of newly created lesson
     */
    suspend fun createLessonWithWords(
        title: String,
        description: String?,
        wordPairs: List<WordPairInput>
    ): Long
    
    /**
     * Update lesson and its word pairs atomically
     * 
     * @param lessonId Lesson to update
     * @param title Updated title
     * @param description Updated description
     * @param wordPairs Updated word pairs (replaces all existing)
     */
    suspend fun updateLessonWithWords(
        lessonId: Long,
        title: String,
        description: String?,
        wordPairs: List<WordPairInput>
    )
    
    /**
     * Delete lesson with all associated data
     * 
     * @param lessonId Lesson to delete
     */
    suspend fun deleteLesson(lessonId: Long)
}

data class WordPairInput(
    val englishTerm: String,
    val vietnameseMeaning: String
)
```

---

## SettingsUseCase

Manages user preferences with validation.

```kotlin
interface SettingsUseCase {
    /**
     * Get current user preferences
     * 
     * @return Flow of user preferences
     */
    fun getPreferences(): Flow<UserPreferences>
    
    /**
     * Update flashcard color with validation
     * 
     * @param colorHex Hex color code (must be valid format)
     * @throws IllegalArgumentException if color format is invalid
     */
    suspend fun updateFlashcardColor(colorHex: String)
    
    /**
     * Update text size
     * 
     * @param textSize New text size
     */
    suspend fun updateTextSize(textSize: TextSize)
    
    /**
     * Toggle practice shuffle setting
     * 
     * @param enabled New shuffle setting
     */
    suspend fun togglePracticeShuffle(enabled: Boolean)
    
    /**
     * Reset all settings to defaults
     */
    suspend fun resetToDefaults()
}
```

**Validation Logic**:

```kotlin
suspend fun updateFlashcardColor(colorHex: String) {
    // Validate hex color format
    require(colorHex.matches(Regex("^#[0-9A-Fa-f]{6}$"))) {
        "Invalid color format. Must be #RRGGBB"
    }
    
    preferencesRepository.setFlashcardColor(colorHex)
}
```

---

## Use Case Testing Requirements

Each use case must have comprehensive unit tests covering:

1. **Happy Path**: Normal operation with valid inputs
2. **Edge Cases**: Boundary conditions (empty lists, min/max values)
3. **Error Handling**: Invalid inputs, null safety
4. **Algorithm Correctness**: SM-2 calculations verified with known examples
5. **Business Rule Enforcement**: Mastery levels, quiz generation rules

**Example Test**:
```kotlin
@Test
fun `SM-2 algorithm calculates correct interval for GOOD rating`() {
    val progress = LearningProgress(
        easinessFactor = 2.5f,
        interval = 6,
        repetitions = 2
    )
    
    val updated = spacedRepetitionUseCase.calculateNextReview(
        progress,
        DifficultyRating.GOOD,
        System.currentTimeMillis()
    )
    
    assertEquals(15, updated.interval)  // 6 * 2.5 = 15
    assertEquals(3, updated.repetitions)
    assertEquals(MasteryLevel.LEARNING, updated.masteryLevel)
}
```

---

**Use Case Contracts Status**: ✅ COMPLETE - All business logic interfaces defined

