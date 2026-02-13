package com.englishflashcard.data.repository

import com.englishflashcard.data.local.database.WordPairDao
import com.englishflashcard.domain.model.WordPair
import com.englishflashcard.domain.repository.WordPairRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordPairRepositoryImpl(
    private val wordPairDao: WordPairDao
) : WordPairRepository {

    override fun getWordPairsByLesson(lessonId: Long): Flow<List<WordPair>> {
        return wordPairDao.getWordPairsByLesson(lessonId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getWordPairById(wordPairId: Long): WordPair? {
        return wordPairDao.getWordPairById(wordPairId)?.toDomain()
    }

    override suspend fun createWordPair(wordPair: WordPair): Long {
        return wordPairDao.insertWordPair(wordPair.toEntity())
    }

    override suspend fun createWordPairs(wordPairs: List<WordPair>): List<Long> {
        val entities = wordPairs.map { it.toEntity() }
        return wordPairDao.insertWordPairs(entities)
    }

    override suspend fun updateWordPair(wordPair: WordPair) {
        wordPairDao.updateWordPair(wordPair.toEntity())
    }

    override suspend fun deleteWordPair(wordPairId: Long) {
        val wordPair = wordPairDao.getWordPairById(wordPairId)
        if (wordPair != null) {
            wordPairDao.deleteWordPair(wordPair)
        }
    }

    override suspend fun deleteWordPairsByLesson(lessonId: Long) {
        wordPairDao.deleteWordPairsByLesson(lessonId)
    }
}

