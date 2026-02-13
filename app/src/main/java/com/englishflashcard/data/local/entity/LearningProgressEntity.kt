package com.englishflashcard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "learning_progress",
    foreignKeys = [
        ForeignKey(
            entity = WordPairEntity::class,
            parentColumns = ["id"],
            childColumns = ["word_pair_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["word_pair_id"], unique = true),
        Index(value = ["next_review_date"])
    ]
)
data class LearningProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "word_pair_id")
    val wordPairId: Long,

    @ColumnInfo(name = "easiness_factor")
    val easinessFactor: Float = 2.5f,

    @ColumnInfo(name = "interval")
    val interval: Int = 0,

    @ColumnInfo(name = "repetitions")
    val repetitions: Int = 0,

    @ColumnInfo(name = "last_review_date")
    val lastReviewDate: Long? = null,

    @ColumnInfo(name = "next_review_date")
    val nextReviewDate: Long? = null,

    @ColumnInfo(name = "mastery_level")
    val masteryLevel: String = "NEW",

    @ColumnInfo(name = "review_count")
    val reviewCount: Int = 0
)

