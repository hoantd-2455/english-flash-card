package com.englishflashcard.domain.repository

import com.englishflashcard.domain.model.WordPair
import kotlinx.coroutines.flow.Flow

interface WordPairRepository {
    fun getWordPairsByLesson(lessonId: Long): Flow<List<WordPair>>
    suspend fun getWordPairById(wordPairId: Long): WordPair?
    suspend fun createWordPair(wordPair: WordPair): Long
    suspend fun createWordPairs(wordPairs: List<WordPair>): List<Long>
    suspend fun updateWordPair(wordPair: WordPair)
    suspend fun deleteWordPair(wordPairId: Long)
    suspend fun deleteWordPairsByLesson(lessonId: Long)
}

