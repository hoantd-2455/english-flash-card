# Data Model: English Vocabulary Flashcard Learning App

**Feature**: 001-flashcard-learning-app  
**Date**: February 13, 2026  
**Phase**: 1 - Design

## Overview

This document defines the data model for the flashcard learning app, including entity definitions, relationships, validation rules, and state transitions. All entities are designed for Room Database implementation with SQLite.

---

## Entity Definitions

### 1. Lesson

Represents a vocabulary learning unit containing multiple word pairs.

**Attributes**:
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | PRIMARY KEY, AUTO_INCREMENT | Unique lesson identifier |
| `title` | String | NOT NULL, max 100 chars | Lesson title (user-defined) |
| `description` | String | NULLABLE, max 500 chars | Optional lesson description |
| `createdAt` | Long | NOT NULL, default current timestamp | Creation timestamp (milliseconds) |
| `updatedAt` | Long | NOT NULL, default current timestamp | Last update timestamp |
| `totalWords` | Int | NOT NULL, default 0 | Cached count of word pairs |

**Validation Rules**:
- Title must not be empty or whitespace-only
- Title must be 1-100 characters
- Description can be empty but max 500 characters
- `totalWords` is computed field, updated via trigger or application logic

**Indexes**:
- PRIMARY KEY on `id`
- INDEX on `updatedAt` for sorting recent lessons

---

### 2. WordPair

Individual vocabulary item within a lesson.

**Attributes**:
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | PRIMARY KEY, AUTO_INCREMENT | Unique word pair identifier |
| `lessonId` | Long | FOREIGN KEY → Lesson.id, ON DELETE CASCADE | Parent lesson reference |
| `englishTerm` | String | NOT NULL, max 200 chars | English word or phrase |
| `vietnameseMeaning` | String | NOT NULL, max 500 chars | Vietnamese translation |
| `orderIndex` | Int | NOT NULL, default 0 | Display order within lesson |
| `createdAt` | Long | NOT NULL | Creation timestamp |

**Validation Rules**:
- `englishTerm` must not be empty, 1-200 characters
- `vietnameseMeaning` must not be empty, 1-500 characters
- Both terms trimmed of leading/trailing whitespace
- `orderIndex` starts at 0, increments for each new word in lesson

**Relationships**:
- Many-to-One with Lesson (many word pairs belong to one lesson)
- Cascade delete: deleting lesson deletes all word pairs

**Indexes**:
- PRIMARY KEY on `id`
- INDEX on `lessonId` for fast lesson queries
- COMPOSITE INDEX on `(lessonId, orderIndex)` for ordered retrieval

---

### 3. LearningProgress

Tracks user's mastery status for each word pair using spaced repetition.

**Attributes**:
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | PRIMARY KEY, AUTO_INCREMENT | Unique progress record identifier |
| `wordPairId` | Long | FOREIGN KEY → WordPair.id, ON DELETE CASCADE | Associated word pair |
| `easinessFactor` | Float | NOT NULL, default 2.5 | SM-2 easiness factor (1.3-3.0) |
| `interval` | Int | NOT NULL, default 0 | Days until next review |
| `repetitions` | Int | NOT NULL, default 0 | Consecutive correct reviews |
| `lastReviewDate` | Long | NULLABLE | Timestamp of last review |
| `nextReviewDate` | Long | NULLABLE | Timestamp when review is due |
| `masteryLevel` | String | NOT NULL, default 'NEW' | Enum: NEW, LEARNING, MASTERED |
| `reviewCount` | Int | NOT NULL, default 0 | Total number of reviews |

**Validation Rules**:
- `easinessFactor` must be between 1.3 and 3.0 (SM-2 algorithm constraint)
- `interval` must be >= 0 (in days)
- `repetitions` must be >= 0
- `masteryLevel` must be one of: 'NEW', 'LEARNING', 'MASTERED'

**State Transitions**:
```
NEW → LEARNING (first practice)
LEARNING → MASTERED (repetitions >= 3 and interval >= 21 days)
MASTERED → LEARNING (if user marks "Again")
LEARNING → LEARNING (ongoing practice)
```

**Relationships**:
- One-to-One with WordPair (each word pair has one progress record)
- Cascade delete: deleting word pair deletes progress

**Indexes**:
- PRIMARY KEY on `id`
- UNIQUE INDEX on `wordPairId` (one progress per word)
- INDEX on `nextReviewDate` for finding due reviews

---

### 4. PracticeSession

Records a learning session with performance metrics.

**Attributes**:
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | PRIMARY KEY, AUTO_INCREMENT | Unique session identifier |
| `lessonId` | Long | FOREIGN KEY → Lesson.id, ON DELETE CASCADE | Lesson being practiced |
| `startTime` | Long | NOT NULL | Session start timestamp |
| `endTime` | Long | NULLABLE | Session end timestamp (null if incomplete) |
| `cardsReviewed` | Int | NOT NULL, default 0 | Number of cards shown |
| `cardsKnown` | Int | NOT NULL, default 0 | Cards marked "know" (swipe right) |
| `cardsUnknown` | Int | NOT NULL, default 0 | Cards marked "don't know" (swipe left) |
| `completedSuccessfully` | Boolean | NOT NULL, default false | True if session finished normally |

**Validation Rules**:
- `endTime` must be >= `startTime` if not null
- `cardsReviewed` should equal `cardsKnown + cardsUnknown`
- `cardsReviewed` must be >= 0

**Relationships**:
- Many-to-One with Lesson
- Cascade delete: deleting lesson deletes all sessions

**Indexes**:
- PRIMARY KEY on `id`
- INDEX on `lessonId` for history retrieval
- INDEX on `startTime` for chronological ordering

---

### 5. QuizResult

Records test performance with question-level details.

**Attributes**:
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | PRIMARY KEY, AUTO_INCREMENT | Unique quiz result identifier |
| `lessonId` | Long | FOREIGN KEY → Lesson.id, ON DELETE CASCADE | Lesson being tested |
| `timestamp` | Long | NOT NULL | Quiz completion timestamp |
| `totalQuestions` | Int | NOT NULL | Total questions in quiz |
| `correctAnswers` | Int | NOT NULL | Number of correct answers |
| `multipleChoiceCount` | Int | NOT NULL, default 0 | Number of MC questions |
| `typingCount` | Int | NOT NULL, default 0 | Number of typing questions |
| `multipleChoiceCorrect` | Int | NOT NULL, default 0 | MC correct count |
| `typingCorrect` | Int | NOT NULL, default 0 | Typing correct count |
| `scorePercentage` | Float | NOT NULL | Overall score (0.0-100.0) |

**Validation Rules**:
- `totalQuestions` = `multipleChoiceCount + typingCount`
- `correctAnswers` = `multipleChoiceCorrect + typingCorrect`
- `scorePercentage` = (`correctAnswers` / `totalQuestions`) × 100
- All counts must be >= 0

**Relationships**:
- Many-to-One with Lesson
- Cascade delete: deleting lesson deletes all quiz results

**Indexes**:
- PRIMARY KEY on `id`
- INDEX on `lessonId` for quiz history
- INDEX on `timestamp` for chronological ordering

---

### 6. UserPreferences

Stores user customization settings (DataStore, not Room).

**Attributes**:
| Field | Type | Default | Description |
|-------|------|---------|-------------|
| `flashcardBackgroundColor` | String | "#FFFFFF" | Hex color code for flashcard background |
| `textSize` | String | "MEDIUM" | Enum: SMALL, MEDIUM, LARGE, EXTRA_LARGE |
| `practiceShuffleEnabled` | Boolean | true | Auto-shuffle flashcards in practice mode |
| `showHintsEnabled` | Boolean | false | Show pronunciation hints (future feature) |

**Validation Rules**:
- `flashcardBackgroundColor` must be valid hex color (#RRGGBB)
- `textSize` must be one of: SMALL, MEDIUM, LARGE, EXTRA_LARGE

**Storage**: Jetpack DataStore (Preferences), not Room Database

---

## Entity Relationships Diagram

```
Lesson (1) ───< (∞) WordPair
                    │
                    └─── (1:1) LearningProgress
                    
Lesson (1) ───< (∞) PracticeSession

Lesson (1) ───< (∞) QuizResult

(UserPreferences stored separately in DataStore)
```

**Cascade Rules**:
- Delete Lesson → Delete all WordPairs, PracticeSessions, QuizResults
- Delete WordPair → Delete associated LearningProgress

---

## Computed Fields & Derived Data

### Lesson Statistics (computed on-demand)

```kotlin
data class LessonStats(
    val totalWords: Int,              // Count of WordPairs
    val newCards: Int,                // Progress with masteryLevel = NEW
    val learningCards: Int,           // Progress with masteryLevel = LEARNING
    val masteredCards: Int,           // Progress with masteryLevel = MASTERED
    val dueCards: Int,                // Progress where nextReviewDate <= now
    val averageScore: Float,          // Average of last 5 QuizResults
    val lastPracticeDate: Long?       // Max startTime from PracticeSessions
)
```

### Learning Progress Metrics

```kotlin
data class ProgressMetrics(
    val lessonId: Long,
    val masteryPercentage: Float,    // (masteredCards / totalWords) × 100
    val reviewsDueToday: Int,        // Count where nextReviewDate is today
    val streakDays: Int              // Consecutive days with practice (future)
)
```

---

## Database Schema (SQL)

```sql
-- Lessons Table
CREATE TABLE lessons (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    total_words INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_lessons_updated_at ON lessons(updated_at);

-- WordPairs Table
CREATE TABLE word_pairs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lesson_id INTEGER NOT NULL,
    english_term TEXT NOT NULL,
    vietnamese_meaning TEXT NOT NULL,
    order_index INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

CREATE INDEX idx_word_pairs_lesson_id ON word_pairs(lesson_id);
CREATE INDEX idx_word_pairs_lesson_order ON word_pairs(lesson_id, order_index);

-- LearningProgress Table
CREATE TABLE learning_progress (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word_pair_id INTEGER NOT NULL UNIQUE,
    easiness_factor REAL NOT NULL DEFAULT 2.5,
    interval INTEGER NOT NULL DEFAULT 0,
    repetitions INTEGER NOT NULL DEFAULT 0,
    last_review_date INTEGER,
    next_review_date INTEGER,
    mastery_level TEXT NOT NULL DEFAULT 'NEW',
    review_count INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (word_pair_id) REFERENCES word_pairs(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_progress_word_pair ON learning_progress(word_pair_id);
CREATE INDEX idx_progress_next_review ON learning_progress(next_review_date);

-- PracticeSessions Table
CREATE TABLE practice_sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lesson_id INTEGER NOT NULL,
    start_time INTEGER NOT NULL,
    end_time INTEGER,
    cards_reviewed INTEGER NOT NULL DEFAULT 0,
    cards_known INTEGER NOT NULL DEFAULT 0,
    cards_unknown INTEGER NOT NULL DEFAULT 0,
    completed_successfully INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

CREATE INDEX idx_sessions_lesson_id ON practice_sessions(lesson_id);
CREATE INDEX idx_sessions_start_time ON practice_sessions(start_time);

-- QuizResults Table
CREATE TABLE quiz_results (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lesson_id INTEGER NOT NULL,
    timestamp INTEGER NOT NULL,
    total_questions INTEGER NOT NULL,
    correct_answers INTEGER NOT NULL,
    multiple_choice_count INTEGER NOT NULL DEFAULT 0,
    typing_count INTEGER NOT NULL DEFAULT 0,
    multiple_choice_correct INTEGER NOT NULL DEFAULT 0,
    typing_correct INTEGER NOT NULL DEFAULT 0,
    score_percentage REAL NOT NULL,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

CREATE INDEX idx_quiz_lesson_id ON quiz_results(lesson_id);
CREATE INDEX idx_quiz_timestamp ON quiz_results(timestamp);
```

---

## Migration Strategy

**Version 1** (Initial): Create all tables with schema above

**Future Versions**: Use Room migration mechanism
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: Add new column for audio pronunciation
        database.execSQL("ALTER TABLE word_pairs ADD COLUMN audio_url TEXT")
    }
}
```

---

## Data Integrity Constraints

1. **Orphan Prevention**: Foreign keys with CASCADE DELETE prevent orphaned records
2. **Unique Constraints**: One LearningProgress per WordPair
3. **Check Constraints** (enforced in app layer):
   - Easiness factor: 1.3 ≤ value ≤ 3.0
   - Mastery level: must be valid enum value
   - Dates: endTime ≥ startTime
4. **Non-null Enforcement**: Critical fields marked NOT NULL

---

## Sample Data (for testing)

```kotlin
// Sample Lesson
Lesson(
    id = 1,
    title = "Basic Greetings",
    description = "Common Vietnamese greetings",
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis(),
    totalWords = 5
)

// Sample WordPairs
WordPair(id = 1, lessonId = 1, englishTerm = "Hello", vietnameseMeaning = "Xin chào", orderIndex = 0)
WordPair(id = 2, lessonId = 1, englishTerm = "Thank you", vietnameseMeaning = "Cảm ơn", orderIndex = 1)
WordPair(id = 3, lessonId = 1, englishTerm = "Goodbye", vietnameseMeaning = "Tạm biệt", orderIndex = 2)

// Sample LearningProgress
LearningProgress(
    id = 1,
    wordPairId = 1,
    easinessFactor = 2.5f,
    interval = 1,
    repetitions = 0,
    masteryLevel = "NEW",
    reviewCount = 0
)
```

---

**Data Model Status**: ✅ COMPLETE - Ready for implementation

