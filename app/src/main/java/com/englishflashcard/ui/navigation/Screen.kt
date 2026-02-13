package com.englishflashcard.ui.navigation

sealed class Screen(val route: String) {
    object LessonList : Screen("lesson_list")
    object LessonDetail : Screen("lesson_detail/{lessonId}") {
        fun createRoute(lessonId: Long) = "lesson_detail/$lessonId"
    }
    object LessonEdit : Screen("lesson_edit?lessonId={lessonId}") {
        fun createRoute(lessonId: Long? = null) = if (lessonId != null) {
            "lesson_edit?lessonId=$lessonId"
        } else {
            "lesson_edit"
        }
    }
    object Practice : Screen("practice/{lessonId}") {
        fun createRoute(lessonId: Long) = "practice/$lessonId"
    }
    object Quiz : Screen("quiz/{lessonId}") {
        fun createRoute(lessonId: Long) = "quiz/$lessonId"
    }
    object Settings : Screen("settings")
}

