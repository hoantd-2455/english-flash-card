package com.englishflashcard.data.repository

import com.englishflashcard.data.local.database.LearningProgressDao
import com.englishflashcard.data.local.entity.LearningProgressEntity
import com.englishflashcard.domain.model.DifficultyRating
import com.englishflashcard.domain.model.LearningProgress
import com.englishflashcard.domain.model.MasteryLevel
import com.englishflashcard.domain.repository.LearningProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.max

class LearningProgressRepositoryImpl(
    private val progressDao: LearningProgressDao
) : LearningProgressRepository {

    override fun getProgressForWordPair(wordPairId: Long): Flow<LearningProgress?> {
        return progressDao.getProgressByWordPair(wordPairId).map { it?.toDomain() }
    }

    override fun getProgressForLesson(lessonId: Long): Flow<List<LearningProgress>> {
        return progressDao.getProgressByLesson(lessonId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getDueCountByLesson(lessonId: Long, currentTime: Long): Int {
        return progressDao.getDueCountByLesson(lessonId, currentTime)
    }

    override suspend fun recordReview(
        wordPairId: Long,
        rating: DifficultyRating,
        reviewTime: Long
    ): LearningProgress {
        val existing = progressDao.getProgressByWordPairSync(wordPairId)

        val progress = if (existing != null) {
            calculateNextReview(existing.toDomain(), rating, reviewTime)
        } else {
            // Create new progress
            val newProgress = LearningProgress(
                wordPairId = wordPairId,
                masteryLevel = MasteryLevel.LEARNING
            )
            calculateNextReview(newProgress, rating, reviewTime)
        }

        progressDao.insertProgress(progress.toEntity())
        return progress
    }

    private fun calculateNextReview(
        current: LearningProgress,
        rating: DifficultyRating,
        reviewTime: Long
    ): LearningProgress {
        val newEasiness: Float
        val newInterval: Int
        val newRepetitions: Int
        val newMasteryLevel: MasteryLevel

        when (rating) {
            DifficultyRating.AGAIN -> {
                newEasiness = max(1.3f, current.easinessFactor - 0.2f)
                newInterval = 1 // Reset to 1 day
                newRepetitions = 0
                newMasteryLevel = MasteryLevel.LEARNING
            }
            DifficultyRating.HARD -> {
                newEasiness = max(1.3f, current.easinessFactor - 0.15f)
                newInterval = (current.interval * 1.2f).toInt().coerceAtLeast(1)
                newRepetitions = current.repetitions
                newMasteryLevel = current.masteryLevel
            }
            DifficultyRating.GOOD -> {
                newEasiness = current.easinessFactor
                newInterval = if (current.repetitions == 0) {
                    1
                } else if (current.repetitions == 1) {
                    6
                } else {
                    (current.interval * current.easinessFactor).toInt()
                }
                newRepetitions = current.repetitions + 1
                newMasteryLevel = if (newRepetitions >= 3 && newInterval >= 21) {
                    MasteryLevel.MASTERED
                } else {
                    MasteryLevel.LEARNING
                }
            }
            DifficultyRating.EASY -> {
                newEasiness = current.easinessFactor + 0.15f
                newInterval = if (current.repetitions == 0) {
                    4
                } else {
                    (current.interval * current.easinessFactor * 1.3f).toInt()
                }
                newRepetitions = current.repetitions + 1
                newMasteryLevel = if (newRepetitions >= 3 && newInterval >= 21) {
                    MasteryLevel.MASTERED
                } else {
                    MasteryLevel.LEARNING
                }
            }
        }

        val nextReviewDate = reviewTime + (newInterval * 24 * 60 * 60 * 1000L)

        return current.copy(
            easinessFactor = newEasiness,
            interval = newInterval,
            repetitions = newRepetitions,
            lastReviewDate = reviewTime,
            nextReviewDate = nextReviewDate,
            masteryLevel = newMasteryLevel,
            reviewCount = current.reviewCount + 1
        )
    }

    override suspend fun resetProgress(wordPairId: Long) {
        progressDao.deleteProgressByWordPair(wordPairId)
    }

    override suspend fun createProgress(progress: LearningProgress): Long {
        return progressDao.insertProgress(progress.toEntity())
    }

    override suspend fun updateProgress(progress: LearningProgress) {
        progressDao.updateProgress(progress.toEntity())
    }
}

