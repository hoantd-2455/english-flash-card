package com.englishflashcard.domain.model

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

