package com.englishflashcard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.englishflashcard.FlashcardApplication
import com.englishflashcard.ui.screens.lessondetail.LessonDetailScreen
import com.englishflashcard.ui.screens.lessondetail.LessonDetailViewModel
import com.englishflashcard.ui.screens.lessonedit.LessonEditScreen
import com.englishflashcard.ui.screens.lessonedit.LessonEditViewModel
import com.englishflashcard.ui.screens.lessonlist.LessonListScreen
import com.englishflashcard.ui.screens.lessonlist.LessonListViewModel
import com.englishflashcard.ui.screens.practice.PracticeScreen
import com.englishflashcard.ui.screens.practice.PracticeViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val container = (context.applicationContext as FlashcardApplication).container

    NavHost(
        navController = navController,
        startDestination = Screen.LessonList.route,
        modifier = modifier
    ) {
        composable(Screen.LessonList.route) {
            val viewModel = viewModel<LessonListViewModel>(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return LessonListViewModel(container.lessonRepository) as T
                    }
                }
            )

            LessonListScreen(
                viewModel = viewModel,
                onLessonClick = { lessonId ->
                    navController.navigate(Screen.LessonDetail.createRoute(lessonId))
                },
                onCreateLesson = {
                    navController.navigate(Screen.LessonEdit.createRoute())
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(
                navArgument("lessonId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getLong("lessonId") ?: 0L

            // Use viewModel factory to prevent recreation on recomposition
            val viewModel = viewModel<LessonDetailViewModel>(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return LessonDetailViewModel(
                            container.lessonRepository,
                            container.wordPairRepository
                        ) as T
                    }
                }
            )

            LessonDetailScreen(
                viewModel = viewModel,
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() },
                onEditLesson = { id ->
                    navController.navigate(Screen.LessonEdit.createRoute(id))
                },
                onStartPractice = { id ->
                    navController.navigate(Screen.Practice.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.LessonEdit.route,
            arguments = listOf(
                navArgument("lessonId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getLong("lessonId") ?: 0L

            val viewModel = viewModel<LessonEditViewModel>(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return LessonEditViewModel(
                            container.lessonRepository,
                            container.wordPairRepository
                        ) as T
                    }
                }
            )

            LessonEditScreen(
                viewModel = viewModel,
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Practice.route,
            arguments = listOf(
                navArgument("lessonId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getLong("lessonId") ?: 0L

            val viewModel = viewModel<PracticeViewModel>(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return PracticeViewModel(
                            container.lessonRepository,
                            container.wordPairRepository
                        ) as T
                    }
                }
            )

            PracticeScreen(
                viewModel = viewModel,
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("lessonId") { type = NavType.LongType }
            )
        ) {
            // QuizScreen will be implemented in Phase 6
            androidx.compose.material3.Text("Quiz Screen - Coming soon")
        }

        composable(Screen.Settings.route) {
            // SettingsScreen will be implemented in Phase 7
            androidx.compose.material3.Text("Settings Screen - Coming soon")
        }
    }
}

