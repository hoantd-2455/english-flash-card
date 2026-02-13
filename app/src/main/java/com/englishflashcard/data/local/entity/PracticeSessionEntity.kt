package com.englishflashcard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "practice_sessions",
    foreignKeys = [
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lesson_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["lesson_id"]),
        Index(value = ["start_time"])
    ]
)
data class PracticeSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    @ColumnInfo(name = "start_time")
    val startTime: Long,

    @ColumnInfo(name = "end_time")
    val endTime: Long? = null,

    @ColumnInfo(name = "cards_reviewed")
    val cardsReviewed: Int = 0,

    @ColumnInfo(name = "cards_known")
    val cardsKnown: Int = 0,

    @ColumnInfo(name = "cards_unknown")
    val cardsUnknown: Int = 0,

    @ColumnInfo(name = "completed_successfully")
    val completedSuccessfully: Boolean = false
)

