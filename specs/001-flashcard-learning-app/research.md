# Research: English Vocabulary Flashcard Learning App

**Feature**: 001-flashcard-learning-app  
**Date**: February 13, 2026  
**Phase**: 0 - Research & Technology Selection

## Overview

This document captures research findings for implementing an Android vocabulary learning app with spaced repetition, flashcards, and mixed quiz formats. All technical decisions are documented here with rationale and alternatives considered.

---

## 1. Android UI Framework Selection

### Decision: Jetpack Compose

**Rationale**:
- Modern declarative UI approach reduces boilerplate code
- Better state management with composable functions
- Built-in animation support for flashcard flips and transitions
- Material3 design system integration
- Active development and Google's recommended approach for new Android apps
- Simplified UI testing with Compose Testing APIs

**Alternatives Considered**:
- **XML Views (Traditional)**: More mature ecosystem but requires more boilerplate, harder to maintain complex UI states
- **Flutter**: Cross-platform but adds complexity for Android-only app, larger app size, not native Android

**Implementation Notes**:
- Use `AnimatedContent` for flashcard flip animations
- Use `SwipeToDismiss` or custom gesture detection for swipe left/right
- Material3 theming for consistent design language

---

## 2. Local Database Solution

### Decision: Room Database (SQLite wrapper)

**Rationale**:
- Official Android ORM with compile-time SQL verification
- Seamless integration with Kotlin Coroutines and Flow for reactive data
- Type-safe database access with minimal boilerplate
- Built-in support for migrations
- Excellent performance for expected data volume (1000+ word pairs)
- Well-documented and widely adopted in Android ecosystem

**Alternatives Considered**:
- **Realm**: Good performance but adds external dependency, less integrated with Android architecture
- **Raw SQLite**: More control but requires significant boilerplate and manual query management
- **DataStore**: Not suitable for relational data with complex queries

**Schema Design** (see data-model.md for details):
- Tables: Lessons, WordPairs, LearningProgress, PracticeSessions, QuizResults
- Foreign key relationships with cascade delete
- Indexes on frequently queried columns (lessonId, nextReviewDate)

---

## 3. Spaced Repetition Algorithm

### Decision: SM-2 Algorithm (SuperMemo 2)

**Rationale**:
- Well-proven algorithm used by Anki and other successful flashcard apps
- Simple to implement with clear mathematical formulas
- Supports 4 difficulty levels (Again, Hard, Good, Easy) as required
- Balances simplicity with effectiveness
- No external dependencies required

**Algorithm Parameters**:
- **Again**: Interval = 1 day (restart learning)
- **Hard**: Interval = previous × 1.2, easiness factor decreases
- **Good**: Interval = previous × easiness factor (typically 2.5)
- **Easy**: Interval = previous × easiness factor × 1.3

**Alternatives Considered**:
- **SM-17+**: More sophisticated but complex implementation, diminishing returns for MVP
- **FSRS (Free Spaced Repetition Scheduler)**: Modern alternative but requires more research and tuning
- **Custom algorithm**: Reinventing the wheel without proven effectiveness

**Implementation**:
- Calculate next review date in domain layer use case
- Store interval and easiness factor in LearningProgress table
- Background worker can trigger notifications for due reviews (future enhancement)

---

## 4. Quiz Question Generation Strategy

### Decision: Hybrid Multiple-Choice + Typing with Smart Adaptation

**Rationale**:
- Multiple-choice provides quick assessment and works well for recognition
- Typing questions test recall (harder, more effective for learning)
- Adaptive approach handles edge cases (lessons with <4 words)
- Mixing formats prevents users from gaming the system

**Implementation Strategy**:
- Lessons with <4 words: 100% typing questions
- Lessons with 4+ words: 50% multiple-choice, 50% typing (shuffled)
- Multiple-choice: Select 3 random wrong answers from same lesson
- Typing validation: Case-insensitive, trim whitespace, exact match on normalized string

**Answer Validation Rules**:
```kotlin
fun validateAnswer(userInput: String, correctAnswer: String): Boolean {
    return userInput.trim().lowercase() == correctAnswer.trim().lowercase()
}
```

**Future Enhancements** (not in MVP):
- Levenshtein distance for "close enough" answers
- Synonym matching with dictionary
- Multiple correct answers support

**Alternatives Considered**:
- **Multiple-choice only**: Easier but less effective for learning
- **Typing only**: More effective but frustrating for beginners
- **Voice input**: Interesting but adds complexity and privacy concerns

---

## 5. User Preferences Storage

### Decision: Jetpack DataStore (Preferences)

**Rationale**:
- Modern replacement for SharedPreferences with type safety
- Kotlin Coroutines and Flow support for reactive updates
- Handles data corruption better than SharedPreferences
- Proto DataStore available for complex preferences if needed

**Settings to Store**:
- Flashcard background color (color resource ID or hex)
- Text size (enum: SMALL, MEDIUM, LARGE, EXTRA_LARGE)
- Practice mode defaults (shuffle enabled, show hints)
- UI theme preferences (future: dark mode)

**Alternatives Considered**:
- **SharedPreferences**: Deprecated approach, less safe
- **Room Database**: Overkill for simple key-value preferences

---

## 6. Architecture Pattern

### Decision: MVVM (Model-View-ViewModel) with Clean Architecture

**Rationale**:
- Recommended pattern for Jetpack Compose apps
- Clear separation: UI Layer → Domain Layer → Data Layer
- ViewModel survives configuration changes (screen rotation)
- Easy to test business logic in isolation
- Repository pattern abstracts data sources

**Layer Responsibilities**:
- **UI Layer**: Composables, ViewModels, UI state management
- **Domain Layer**: Business logic, use cases (spaced repetition, quiz generation), domain models
- **Data Layer**: Room database, DataStore, repository implementations

**State Management**:
- Use StateFlow and mutableStateOf for reactive UI updates
- Single source of truth principle
- Unidirectional data flow

**Alternatives Considered**:
- **MVI (Model-View-Intent)**: More complex, overkill for this app size
- **MVP**: Outdated, not recommended for Compose

---

## 7. Navigation Strategy

### Decision: Jetpack Navigation Compose

**Rationale**:
- Official navigation library for Compose
- Type-safe navigation with arguments
- Deep linking support for future features
- Handles back stack automatically
- Seamless integration with Compose lifecycle

**Navigation Graph**:
```
LessonListScreen (start destination)
├── LessonDetailScreen(lessonId)
│   ├── LessonEditScreen(lessonId)
│   ├── PracticeScreen(lessonId)
│   └── QuizScreen(lessonId)
└── SettingsScreen
```

**Alternatives Considered**:
- **Manual navigation with state**: Error-prone, hard to maintain
- **Third-party libraries (Voyager, Decompose)**: Not necessary, adds dependency

---

## 8. Testing Strategy

### Decision: Three-layer testing with focus on domain logic

**Testing Breakdown**:
1. **Unit Tests (JUnit 5 + MockK)**:
   - Domain layer use cases (spaced repetition algorithm)
   - Repository implementations with in-memory database
   - Quiz generation logic
   - Answer validation

2. **Integration Tests (Room in-memory DB)**:
   - Database queries and relationships
   - Data migrations
   - Repository contract tests

3. **UI Tests (Espresso + Compose Testing)**:
   - Critical user flows (create lesson, practice, quiz)
   - Swipe gestures in practice mode
   - Navigation between screens

**Test Coverage Goals**:
- Domain layer: 80%+ coverage
- UI layer: Critical paths only (smoke tests)
- Database layer: All queries tested

**Alternatives Considered**:
- **Screenshot testing**: Nice-to-have but not critical for MVP
- **Manual testing only**: Risky for spaced repetition algorithm correctness

---

## 9. Flashcard Animation Implementation

### Decision: Custom AnimatedContent with 3D flip effect

**Rationale**:
- Provides realistic card flip experience
- Compose's `AnimatedContent` supports custom transitions
- Can use `graphicsLayer` for rotation animations
- Smooth 60fps animations on target devices (Android 7+)

**Implementation Approach**:
```kotlin
AnimatedContent(
    targetState = isFlipped,
    transitionSpec = {
        fadeIn() + scaleIn() with fadeOut() + scaleOut() using
            SizeTransform(clip = false)
    }
) { flipped ->
    if (flipped) BackSide() else FrontSide()
}
```

**Performance Considerations**:
- Use `remember` to avoid recomposition overhead
- Lazy loading for large word lists
- Hardware acceleration enabled by default

---

## 10. Vietnamese Language Support

### Decision: Unicode UTF-8 with standard Android text rendering

**Rationale**:
- Android natively supports Vietnamese Unicode characters
- No special libraries needed for diacritics (á, ă, â, đ, etc.)
- TextField and Text composables handle Vietnamese input correctly
- Standard Android IME (keyboard) supports Vietnamese layouts

**Testing Requirements**:
- Test with all Vietnamese diacritics
- Verify proper sorting (locale-aware comparisons)
- Ensure text wrapping handles long Vietnamese words

**Edge Cases**:
- Mixed English-Vietnamese text rendering
- Vietnamese text in different font sizes (settings requirement)

---

## 11. Data Export/Import (Future Enhancement)

**Not in MVP**: Documented for future consideration

**Potential Approach**:
- Export lessons to JSON format
- Import from CSV (compatible with Quizlet format)
- Cloud sync via Google Drive (requires auth)

---

## Technology Stack Summary

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Kotlin | 1.9+ |
| UI Framework | Jetpack Compose | Latest stable |
| Database | Room | 2.6+ |
| Preferences | DataStore | 1.0+ |
| Navigation | Navigation Compose | 2.7+ |
| Architecture | MVVM + Clean Architecture | - |
| Async | Kotlin Coroutines + Flow | 1.7+ |
| DI | Hilt (optional for MVP) | 2.48+ |
| Testing | JUnit 5, MockK, Espresso | Latest |
| Build | Gradle with Kotlin DSL | 8.0+ |

---

## Open Questions (Resolved)

All technical unknowns from Technical Context have been resolved through research above.

---

## References

1. SM-2 Algorithm: https://www.supermemo.com/en/archives1990-2015/english/ol/sm2
2. Jetpack Compose: https://developer.android.com/jetpack/compose
3. Room Database: https://developer.android.com/training/data-storage/room
4. MVVM Architecture: https://developer.android.com/topic/architecture
5. Spaced Repetition Best Practices: https://ncase.me/remember/

---

**Research Status**: ✅ COMPLETE - All technical decisions documented and justified

