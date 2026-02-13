package com.englishflashcard.data.repository

import com.englishflashcard.data.local.entity.LearningProgressEntity
import com.englishflashcard.data.local.entity.LessonEntity
import com.englishflashcard.data.local.entity.PracticeSessionEntity
import com.englishflashcard.data.local.entity.QuizResultEntity
import com.englishflashcard.data.local.entity.WordPairEntity
import com.englishflashcard.domain.model.LearningProgress
import com.englishflashcard.domain.model.Lesson
import com.englishflashcard.domain.model.MasteryLevel
import com.englishflashcard.domain.model.PracticeSession
import com.englishflashcard.domain.model.QuizResult
import com.englishflashcard.domain.model.WordPair

// Lesson mappers
fun LessonEntity.toDomain() = Lesson(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    totalWords = totalWords
)

fun Lesson.toEntity() = LessonEntity(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    totalWords = totalWords
)

// WordPair mappers
fun WordPairEntity.toDomain() = WordPair(
    id = id,
    lessonId = lessonId,
    englishTerm = englishTerm,
    vietnameseMeaning = vietnameseMeaning,
    orderIndex = orderIndex,
    createdAt = createdAt
)

fun WordPair.toEntity() = WordPairEntity(
    id = id,
    lessonId = lessonId,
    englishTerm = englishTerm,
    vietnameseMeaning = vietnameseMeaning,
    orderIndex = orderIndex,
    createdAt = createdAt
)

// LearningProgress mappers
fun LearningProgressEntity.toDomain() = LearningProgress(
    id = id,
    wordPairId = wordPairId,
    easinessFactor = easinessFactor,
    interval = interval,
    repetitions = repetitions,
    lastReviewDate = lastReviewDate,
    nextReviewDate = nextReviewDate,
    masteryLevel = MasteryLevel.valueOf(masteryLevel),
    reviewCount = reviewCount
)

fun LearningProgress.toEntity() = LearningProgressEntity(
    id = id,
    wordPairId = wordPairId,
    easinessFactor = easinessFactor,
    interval = interval,
    repetitions = repetitions,
    lastReviewDate = lastReviewDate,
    nextReviewDate = nextReviewDate,
    masteryLevel = masteryLevel.name,
    reviewCount = reviewCount
)

// PracticeSession mappers
fun PracticeSessionEntity.toDomain() = PracticeSession(
    id = id,
    lessonId = lessonId,
    startTime = startTime,
    endTime = endTime,
    cardsReviewed = cardsReviewed,
    cardsKnown = cardsKnown,
    cardsUnknown = cardsUnknown,
    completedSuccessfully = completedSuccessfully
)

fun PracticeSession.toEntity() = PracticeSessionEntity(
    id = id,
    lessonId = lessonId,
    startTime = startTime,
    endTime = endTime,
    cardsReviewed = cardsReviewed,
    cardsKnown = cardsKnown,
    cardsUnknown = cardsUnknown,
    completedSuccessfully = completedSuccessfully
)

// QuizResult mappers
fun QuizResultEntity.toDomain() = QuizResult(
    id = id,
    lessonId = lessonId,
    timestamp = timestamp,
    totalQuestions = totalQuestions,
    correctAnswers = correctAnswers,
    multipleChoiceCount = multipleChoiceCount,
    typingCount = typingCount,
    multipleChoiceCorrect = multipleChoiceCorrect,
    typingCorrect = typingCorrect,
    scorePercentage = scorePercentage
)

fun QuizResult.toEntity() = QuizResultEntity(
    id = id,
    lessonId = lessonId,
    timestamp = timestamp,
    totalQuestions = totalQuestions,
    correctAnswers = correctAnswers,
    multipleChoiceCount = multipleChoiceCount,
    typingCount = typingCount,
    multipleChoiceCorrect = multipleChoiceCorrect,
    typingCorrect = typingCorrect,
    scorePercentage = scorePercentage
)

