package com.englishflashcard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_pairs",
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
        Index(value = ["lesson_id", "order_index"])
    ]
)
data class WordPairEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    @ColumnInfo(name = "english_term")
    val englishTerm: String,

    @ColumnInfo(name = "vietnamese_meaning")
    val vietnameseMeaning: String,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)

