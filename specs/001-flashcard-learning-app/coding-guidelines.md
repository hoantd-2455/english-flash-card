# Coding Guidelines: English Flashcard App

**Reference**: This document summarizes coding principles from `.specify/memory/constitution.md`  
**Target Audience**: Developers implementing tasks from `tasks.md`

---

## Quick Reference Card

### Before Writing Code
- [ ] Read constitution: `.specify/memory/constitution.md`
- [ ] Review user story acceptance criteria from `spec.md`
- [ ] Check task description in `tasks.md`
- [ ] Understand which layer you're working in (UI / Domain / Data)

### While Writing Code
- [ ] Follow Clean Code principles (small functions, meaningful names)
- [ ] Ensure loose coupling (no cross-VM dependencies)
- [ ] Use Material3 components and predefined spacing
- [ ] Add smooth animations where appropriate
- [ ] Keep domain layer free of Android dependencies

### After Writing Code (REQUIRED)
- [ ] Compile: `./gradlew assembleDebug`
- [ ] Lint: `./gradlew lintDebug` (fix ALL errors)
- [ ] Format: `./gradlew ktlintFormat` or IDE formatter
- [ ] Self-review: Read your code critically
- [ ] Test: Write unit test for domain logic (optional)
- [ ] Commit: Use proper message format

---

## Constitution Principles

### 1. Clean Code (NON-NEGOTIABLE)

**Rules**:
- Functions ≤ 30 lines (except Composables with UI layout)
- Classes ≤ 200 lines
- Meaningful names (no abbreviations like `calc`, `l`, `t`, `vm`)
- No magic numbers (use `const val TIMEOUT_MS = 5000`)
- Comments explain WHY, not WHAT
- Delete dead code immediately

**Example - Bad**:
```kotlin
fun calc(l: Lesson): Int {
    var c = 0
    for (w in l.words) {
        if (getProgress(w.id).nr <= 1000) c++
    }
    return c
}
```

**Example - Good**:
```kotlin
private const val CURRENT_TIMESTAMP_MS = 1000L

fun calculateDueCardsCount(lesson: Lesson): Int {
    return lesson.words.count { word ->
        isCardDueForReview(word, CURRENT_TIMESTAMP_MS)
    }
}

private fun isCardDueForReview(word: WordPair, currentTime: Long): Boolean {
    val progress = getProgress(word.id)
    return progress.nextReviewDate <= currentTime
}
```

---

### 2. Loose Coupling (NON-NEGOTIABLE)

**Rules**:
- ViewModels must NOT know about other ViewModels
- Composables receive data via parameters (no direct ViewModel access in child components)
- Use interfaces instead of concrete implementations
- Dependency Injection via constructor
- Each screen testable in isolation

**Example - Bad (Tight Coupling)**:
```kotlin
class LessonDetailViewModel(
    private val practiceViewModel: PracticeViewModel  // ❌
) : ViewModel() {
    fun startPractice() {
        practiceViewModel.loadLesson(lessonId)  // ❌
    }
}

@Composable
fun LessonCard(lessonId: Long) {
    val viewModel: LessonListViewModel = viewModel()  // ❌ Direct access
    val lesson = viewModel.getLesson(lessonId)
    // UI code
}
```

**Example - Good (Loose Coupling)**:
```kotlin
class LessonDetailViewModel(
    private val repository: LessonRepository  // ✅ Interface
) : ViewModel() {
    private val _navigateToPractice = MutableStateFlow<Long?>(null)
    val navigateToPractice: StateFlow<Long?> = _navigateToPractice
    
    fun startPractice() {
        _navigateToPractice.value = lessonId  // ✅ Emit event
    }
}

@Composable
fun LessonCard(
    lesson: Lesson,  // ✅ Data via parameters
    onCardClick: (Long) -> Unit,  // ✅ Callback
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onCardClick(lesson.id) }
    ) {
        Text(lesson.title)
    }
}
```

---

### 3. Simple UI First

**Rules**:
- Use Material3 components by default (Button, Card, TextField, TopAppBar, etc.)
- Maximum 3 colors in theme (primary, secondary, background)
- Use predefined spacing from `dimens.xml` (4dp, 8dp, 16dp, 24dp, 32dp)
- Focus on most important information first
- Mobile-first design

**Example - Material3 Theme**:
```kotlin
// app/src/main/java/com/englishflashcard/ui/theme/Theme.kt
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),      // Purple
    secondary = Color(0xFF03DAC6),    // Teal
    background = Color(0xFFFFFBFE)    // White
)

// Usage in Composable:
Button(
    onClick = { /* action */ },
    colors = ButtonDefaults.buttonColors()  // Uses theme colors
) {
    Text("Start Practice")
}
```

**Example - Consistent Spacing**:
```xml
<!-- res/values/dimens.xml -->
<resources>
    <dimen name="spacing_xs">4dp</dimen>
    <dimen name="spacing_sm">8dp</dimen>
    <dimen name="spacing_md">16dp</dimen>
    <dimen name="spacing_lg">24dp</dimen>
    <dimen name="spacing_xl">32dp</dimen>
</resources>
```

```kotlin
// Usage:
Column(
    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_md))
) {
    Text("Title", modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_sm)))
    Text("Content")
}
```

---

### 4. Smooth Animations Required

**Rules**:
- All screen transitions animated
- Flashcard flip uses AnimatedContent
- Swipe gestures have spring physics
- Loading states show progress indicators
- Target: 60fps for all animations

**Example - Flashcard Flip Animation**:
```kotlin
@Composable
fun FlashcardView(
    word: WordPair,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = isFlipped,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) + 
            scaleIn(initialScale = 0.92f, animationSpec = tween(300)) with
            fadeOut(animationSpec = tween(300)) +
            scaleOut(targetScale = 0.92f, animationSpec = tween(300))
        },
        modifier = modifier
    ) { flipped ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onFlip() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (flipped) word.vietnameseMeaning else word.englishTerm,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
```

**Example - Navigation Animations**:
```kotlin
// In NavGraph.kt
composable(
    route = "practice/{lessonId}",
    arguments = listOf(navArgument("lessonId") { type = NavType.LongType }),
    enterTransition = {
        slideInHorizontally(initialOffsetX = { it }) + fadeIn()
    },
    exitTransition = {
        slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
    }
) { backStackEntry ->
    val lessonId = backStackEntry.arguments?.getLong("lessonId") ?: 0L
    PracticeScreen(lessonId = lessonId)
}
```

**Example - List Item Animations**:
```kotlin
LazyColumn {
    items(
        items = lessons,
        key = { lesson -> lesson.id }
    ) { lesson ->
        LessonCard(
            lesson = lesson,
            onCardClick = onLessonClick,
            modifier = Modifier.animateItemPlacement()  // ✅ Animate reordering
        )
    }
}
```

---

### 5. Code Quality Gates

**Required Gates**:
1. ✅ Compilation: Code must compile without errors
2. ✅ Lint: Zero lint errors
3. ✅ Format: Follow Kotlin style guide
4. ✅ Review: Self-review changes
5. ✅ Test: Unit tests for domain logic (optional for UI)

**Commands**:
```bash
# 1. Compile
./gradlew assembleDebug

# 2. Lint
./gradlew lintDebug

# 3. Format
./gradlew ktlintFormat
# OR use Android Studio: Code → Reformat Code (Ctrl+Alt+L)

# 4. Check formatting (CI/CD friendly)
./gradlew ktlintCheck

# 5. Run tests
./gradlew test

# All in one command:
./gradlew clean lintDebug ktlintCheck assembleDebug test
```

---

## Architecture Layers

```
┌─────────────────────────────────────────┐
│ UI Layer                                │
│ - Composables                           │
│ - ViewModels                            │
│ - UiState                               │
│ - Navigation                            │
└─────────────────────────────────────────┘
              ↓ depends on ↓
┌─────────────────────────────────────────┐
│ Domain Layer (Pure Kotlin)              │
│ - Use Cases                             │
│ - Domain Models                         │
│ - Repository Interfaces                 │
└─────────────────────────────────────────┘
              ↓ depends on ↓
┌─────────────────────────────────────────┐
│ Data Layer                              │
│ - Repository Implementations            │
│ - Room DAOs                             │
│ - Entities                              │
│ - DataStore                             │
└─────────────────────────────────────────┘
```

**Rules**:
- Dependencies flow downward only (UI → Domain → Data)
- Domain layer has **NO Android imports** (pure Kotlin)
- Data layer returns **domain models**, not entities
- UI layer observes **StateFlow/Flow**, never calls suspend functions directly in Composables

**Example - Layer Boundaries**:
```kotlin
// ❌ BAD - Domain layer with Android dependency
package com.englishflashcard.domain.usecase

import android.content.Context  // ❌ Android import in domain layer!

class SpacedRepetitionUseCase(
    private val context: Context  // ❌
) { }

// ✅ GOOD - Pure Kotlin domain layer
package com.englishflashcard.domain.usecase

class SpacedRepetitionUseCase(
    private val progressRepository: LearningProgressRepository  // ✅ Interface
) {
    fun calculateNextReview(
        progress: LearningProgress,
        rating: DifficultyRating
    ): LearningProgress {
        // Pure Kotlin logic, no Android dependencies
    }
}
```

---

## Commit Message Format

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code change without changing functionality
- `docs`: Documentation only
- `test`: Adding tests
- `chore`: Build process, dependencies, tooling

**Scopes**:
- `US1`, `US2`, `US3`, `US4`, `US5`
- `foundation`
- `polish`

**Examples**:
```bash
feat(US1): implement lesson list screen with empty state

fix(US2): resolve flashcard flip animation jank on low-end devices

The animation was stuttering on devices with < 2GB RAM due to
excessive recomposition. Fixed by using remember and derivedStateOf.

refactor(foundation): extract repository interface to separate file

test(US3): add SM-2 algorithm unit tests for all difficulty ratings

docs(quickstart): add coding standards and quality gates section

chore(foundation): update Room dependency to 2.6.1
```

---

## Common Patterns

### ViewModel Pattern

```kotlin
class LessonListViewModel(
    private val lessonRepository: LessonRepository,
    private val statsUseCase: LessonStatsUseCase
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow<LessonListUiState>(LessonListUiState.Loading)
    val uiState: StateFlow<LessonListUiState> = _uiState.asStateFlow()
    
    // Navigation Events (one-time events)
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()
    
    init {
        loadLessons()
    }
    
    private fun loadLessons() {
        viewModelScope.launch {
            lessonRepository.getAllLessons()
                .catch { error ->
                    _uiState.value = LessonListUiState.Error(error.message ?: "Unknown error")
                }
                .collect { lessons ->
                    _uiState.value = if (lessons.isEmpty()) {
                        LessonListUiState.Empty
                    } else {
                        LessonListUiState.Success(lessons)
                    }
                }
        }
    }
    
    fun onLessonClick(lessonId: Long) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.ToLessonDetail(lessonId))
        }
    }
    
    fun onAddLessonClick() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.ToLessonCreate)
        }
    }
}

// UI State sealed class
sealed interface LessonListUiState {
    object Loading : LessonListUiState
    object Empty : LessonListUiState
    data class Success(val lessons: List<Lesson>) : LessonListUiState
    data class Error(val message: String) : LessonListUiState
}

// Navigation events
sealed interface NavigationEvent {
    data class ToLessonDetail(val lessonId: Long) : NavigationEvent
    object ToLessonCreate : NavigationEvent
}
```

### Composable Pattern

```kotlin
@Composable
fun LessonListScreen(
    viewModel: LessonListViewModel = viewModel(),
    navController: NavController
) {
    // Collect state
    val uiState by viewModel.uiState.collectAsState()
    
    // Collect navigation events
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.ToLessonDetail -> 
                    navController.navigate("detail/${event.lessonId}")
                NavigationEvent.ToLessonCreate -> 
                    navController.navigate("create")
            }
        }
    }
    
    // UI rendering
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Lessons") },
                actions = {
                    IconButton(onClick = { /* settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onAddLessonClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Lesson")
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is LessonListUiState.Loading -> LoadingScreen()
            is LessonListUiState.Empty -> EmptyStateScreen(onAddClick = viewModel::onAddLessonClick)
            is LessonListUiState.Success -> LessonList(
                lessons = state.lessons,
                onLessonClick = viewModel::onLessonClick,
                modifier = Modifier.padding(paddingValues)
            )
            is LessonListUiState.Error -> ErrorScreen(message = state.message)
        }
    }
}

@Composable
private fun LessonList(
    lessons: List<Lesson>,
    onLessonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(lessons, key = { it.id }) { lesson ->
            LessonCard(
                lesson = lesson,
                onCardClick = { onLessonClick(lesson.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_md))
                    .padding(vertical = dimensionResource(R.dimen.spacing_sm))
                    .animateItemPlacement()
            )
        }
    }
}
```

---

## Self-Review Checklist

Before marking a task complete, review your code:

### Code Quality
- [ ] All function names clearly describe what they do
- [ ] All variable names are meaningful (no `x`, `temp`, `data`)
- [ ] No functions > 30 lines (except UI layouts)
- [ ] No classes > 200 lines
- [ ] No magic numbers (all constants named)
- [ ] No commented-out code
- [ ] No unused imports

### Architecture
- [ ] ViewModel doesn't depend on other ViewModels
- [ ] Composables receive data via parameters
- [ ] Domain layer has no Android imports
- [ ] Dependencies flow downward (UI → Domain → Data)
- [ ] Repository returns domain models, not entities

### UI/UX
- [ ] Uses Material3 components
- [ ] Uses theme colors (not hardcoded colors)
- [ ] Uses dimens.xml for spacing
- [ ] Animations are smooth (if applicable)
- [ ] Loading/error states handled

### Quality Gates
- [ ] Code compiles: `./gradlew assembleDebug`
- [ ] Zero lint errors: `./gradlew lintDebug`
- [ ] Code formatted: `./gradlew ktlintFormat`
- [ ] Self-reviewed changes
- [ ] Unit tests written (for domain logic)

### Commit
- [ ] Commit message follows format: `type(scope): description`
- [ ] Changes grouped logically (one task per commit)

---

## Performance Tips

### Avoid Recomposition

```kotlin
// ❌ BAD - Creates new lambda on every recomposition
@Composable
fun LessonCard(lesson: Lesson, viewModel: LessonListViewModel) {
    Card(onClick = { viewModel.onLessonClick(lesson.id) }) {  // ❌ New lambda
        Text(lesson.title)
    }
}

// ✅ GOOD - Stable callback reference
@Composable
fun LessonCard(
    lesson: Lesson,
    onCardClick: (Long) -> Unit  // ✅ Stable reference
) {
    val onClick = remember(lesson.id) { { onCardClick(lesson.id) } }
    Card(onClick = onClick) {
        Text(lesson.title)
    }
}
```

### Use remember and derivedStateOf

```kotlin
// ❌ BAD - Recalculates on every recomposition
@Composable
fun LessonDetailScreen(lesson: Lesson) {
    val dueCards = lesson.words.filter { isDue(it) }.size  // ❌ Recalculates
    Text("Due cards: $dueCards")
}

// ✅ GOOD - Cached calculation
@Composable
fun LessonDetailScreen(lesson: Lesson) {
    val dueCards = remember(lesson.words) {
        lesson.words.count { isDue(it) }
    }
    Text("Due cards: $dueCards")
}
```

### LazyColumn Keys

```kotlin
// ❌ BAD - No keys, can't animate properly
LazyColumn {
    items(lessons) { lesson ->
        LessonCard(lesson)
    }
}

// ✅ GOOD - Stable keys enable animations
LazyColumn {
    items(lessons, key = { it.id }) { lesson ->
        LessonCard(
            lesson = lesson,
            modifier = Modifier.animateItemPlacement()
        )
    }
}
```

---

## Resources

- **Constitution**: `.specify/memory/constitution.md`
- **Plan**: `specs/001-flashcard-learning-app/plan.md`
- **Tasks**: `specs/001-flashcard-learning-app/tasks.md`
- **Quickstart**: `specs/001-flashcard-learning-app/quickstart.md`
- **Contracts**: `specs/001-flashcard-learning-app/contracts/`

**External References**:
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose/mental-model)
- [Material3 Components](https://m3.material.io/components)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

**Last Updated**: 2026-02-13  
**Version**: 1.0.0

