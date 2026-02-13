package com.englishflashcard.domain.model

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

