package com.englishflashcard.domain.model

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

