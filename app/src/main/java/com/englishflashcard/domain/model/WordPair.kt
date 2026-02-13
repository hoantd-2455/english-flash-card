package com.englishflashcard.domain.model

data class WordPair(
    val id: Long = 0,
    val lessonId: Long,
    val englishTerm: String,
    val vietnameseMeaning: String,
    val orderIndex: Int = 0,
    val createdAt: Long
)

