package com.englishflashcard.domain.model

enum class MasteryLevel {
    NEW,
    LEARNING,
    MASTERED
}

enum class DifficultyRating {
    AGAIN,      // Wrong answer - reset interval
    HARD,       // Difficult - multiply interval by 1.2
    GOOD,       // Correct - multiply by easiness factor
    EASY        // Very easy - multiply by easiness factor * 1.3
}

enum class TextSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

