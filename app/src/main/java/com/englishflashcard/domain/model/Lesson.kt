package com.englishflashcard.domain.model

data class Lesson(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val totalWords: Int = 0
)

