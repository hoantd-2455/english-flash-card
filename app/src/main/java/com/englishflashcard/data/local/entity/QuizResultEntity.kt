package com.englishflashcard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_results",
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
        Index(value = ["timestamp"])
    ]
)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "total_questions")
    val totalQuestions: Int,

    @ColumnInfo(name = "correct_answers")
    val correctAnswers: Int,

    @ColumnInfo(name = "multiple_choice_count")
    val multipleChoiceCount: Int = 0,

    @ColumnInfo(name = "typing_count")
    val typingCount: Int = 0,

    @ColumnInfo(name = "multiple_choice_correct")
    val multipleChoiceCorrect: Int = 0,

    @ColumnInfo(name = "typing_correct")
    val typingCorrect: Int = 0,

    @ColumnInfo(name = "score_percentage")
    val scorePercentage: Float
)

