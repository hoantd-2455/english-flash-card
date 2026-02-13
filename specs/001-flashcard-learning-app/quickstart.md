# Quickstart Guide: English Vocabulary Flashcard Learning App

**Feature**: 001-flashcard-learning-app  
**Date**: February 13, 2026  
**Target**: Developers setting up the project for the first time

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Java 17 or later
- **Android SDK**: API 24 (Android 7.0) minimum, API 34 (Android 14) target
- **Gradle**: 8.0+ (included with Android Studio)
- **Git**: For version control

---

## Project Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd english-flash-card
git checkout 001-flashcard-learning-app
```

### 2. Open in Android Studio

1. Open Android Studio
2. Select **File → Open**
3. Navigate to the `english-flash-card` directory
4. Click **OK**
5. Wait for Gradle sync to complete

### 3. Configure SDK

If prompted, install missing SDK components:
- Android SDK Platform 34
- Android SDK Build-Tools 34.0.0
- Android Emulator (optional, for testing)

---

## Project Structure

```
english-flash-card/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/englishflashcard/    # Source code
│   │   │   │   ├── ui/                        # Jetpack Compose UI
│   │   │   │   ├── domain/                    # Business logic
│   │   │   │   └── data/                      # Data layer (Room, DataStore)
│   │   │   ├── res/                           # Resources (strings, colors)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                              # Unit tests
│   │   └── androidTest/                       # Instrumented tests
│   └── build.gradle.kts                       # App-level Gradle
├── build.gradle.kts                           # Project-level Gradle
├── settings.gradle.kts
└── specs/001-flashcard-learning-app/          # Specification docs
```

---

## Dependencies

Key dependencies configured in `app/build.gradle.kts`:

```kotlin
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    
    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    
    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

---

## Build & Run

### Option 1: Using Android Studio

1. Select a device or emulator from the device dropdown
2. Click the **Run** button (green play icon) or press `Shift + F10`
3. App will build and launch on the selected device

### Option 2: Using Command Line

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

---

## Running Tests

### Unit Tests

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.englishflashcard.domain.SpacedRepetitionUseCaseTest"

# Run with coverage
./gradlew testDebugUnitTest --coverage
```

### Instrumented Tests (UI Tests)

```bash
# Ensure device/emulator is connected
adb devices

# Run all instrumented tests
./gradlew connectedAndroidTest

# Run specific UI test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.englishflashcard.ui.LessonListScreenTest
```

---

## Database Inspection

### Using Android Studio Database Inspector

1. Run the app on an emulator or rooted device
2. **View → Tool Windows → App Inspection**
3. Select **Database Inspector** tab
4. You'll see tables: `lessons`, `word_pairs`, `learning_progress`, etc.
5. Browse data, run queries, modify rows

### Using ADB Shell

```bash
# Access SQLite database
adb shell
cd /data/data/com.englishflashcard/databases/
sqlite3 flashcard_database.db

# Example queries
SELECT * FROM lessons;
SELECT * FROM word_pairs WHERE lesson_id = 1;
SELECT * FROM learning_progress WHERE mastery_level = 'MASTERED';
```

---

## Development Workflow

### Coding Standards (REQUIRED)

**Read the Constitution**: Before writing any code, read `.specify/memory/constitution.md` for project coding principles.

**Key Principles**:
1. **Clean Code** - Functions ≤ 30 lines, meaningful names, no magic numbers
2. **Loose Coupling** - No ViewModel cross-dependencies, parameters over direct access
3. **Simple UI** - Material3 by default, 3-color max, consistent spacing
4. **Smooth Animations** - 60fps target, meaningful transitions
5. **Quality Gates** - Lint, format, review, test before marking task complete

### 1. Create a Feature Branch

```bash
git checkout -b feature/lesson-list-ui
```

### 2. Follow TDD Approach

1. Write failing test first:
   ```kotlin
   @Test
   fun `createLesson saves lesson to database`() {
       // Test implementation
   }
   ```

2. Run test (should fail):
   ```bash
   ./gradlew test
   ```

3. Implement feature to make test pass

4. Refactor and ensure tests still pass

### 3. Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable names
- Add KDoc comments for public APIs
- Keep functions small and focused

### 4. Commit Changes

**Before Committing - Quality Checklist**:

```bash
# 1. Run lint
./gradlew lintDebug

# 2. Format code (choose one)
./gradlew ktlintFormat
# OR use Android Studio: Code → Reformat Code (Ctrl+Alt+L / Cmd+Opt+L)

# 3. Build to ensure compilation
./gradlew assembleDebug

# 4. Run tests (if you wrote any)
./gradlew test

# 5. Self-review your changes
git diff

# 6. Commit with meaningful message
git add .
git commit -m "feat(US1): implement lesson list screen with empty state"
git push origin feature/lesson-list-ui
```

**Commit Message Format**:
```
<type>(<scope>): <description>

Types: feat, fix, refactor, docs, test, chore
Scope: US1, US2, US3, US4, US5, foundation, polish

Examples:
feat(US1): add lesson creation form with validation
fix(US2): resolve flashcard flip animation jank
refactor(foundation): extract repository interface
docs(quickstart): add coding standards section
test(US3): add SM-2 algorithm unit tests
```

---

## Code Quality Tools

### Lint Configuration

Add to `app/build.gradle.kts`:
```kotlin
android {
    lint {
        abortOnError = true
        checkDependencies = true
        warningsAsErrors = false  // Optional: make warnings fail build
    }
}
```

### Code Formatting

**Option 1: ktlint (Recommended)**

Add to `build.gradle.kts`:
```kotlin
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}
```

Then run:
```bash
./gradlew ktlintCheck    # Check formatting
./gradlew ktlintFormat   # Auto-fix formatting
```

**Option 2: Android Studio Built-in**

1. **Settings → Editor → Code Style → Kotlin**
2. Click **Set from... → Kotlin style guide**
3. Apply
4. Use **Code → Reformat Code** (Ctrl+Alt+L / Cmd+Opt+L)

### Pre-commit Hook (Optional)

Create `.git/hooks/pre-commit`:
```bash
#!/bin/sh
echo "Running lint and format checks..."

# Run lint
./gradlew lintDebug --quiet
if [ $? -ne 0 ]; then
    echo "❌ Lint failed. Fix errors before committing."
    exit 1
fi

# Check formatting
./gradlew ktlintCheck --quiet
if [ $? -ne 0 ]; then
    echo "❌ Formatting issues found. Run './gradlew ktlintFormat'"
    exit 1
fi

echo "✅ All checks passed!"
exit 0
```

Make executable:
```bash
chmod +x .git/hooks/pre-commit
```

---

## Coding Guidelines

### Clean Code Examples

**❌ BAD - Magic numbers, unclear names, too long**:
```kotlin
fun calc(l: Lesson, t: Long): Int {
    var c = 0
    for (w in l.words) {
        val p = getProgress(w.id)
        if (p.nextReview != null && p.nextReview <= t) {
            c++
        }
    }
    if (c > 0) {
        // do something
        // more logic...
        // 50 more lines...
    }
    return c
}
```

**✅ GOOD - Clear names, extracted functions, constants**:
```kotlin
private const val NO_DUE_CARDS = 0

fun calculateDueCardsCount(lesson: Lesson, currentTime: Long): Int {
    return lesson.words.count { word ->
        isDueForReview(word, currentTime)
    }
}

private fun isDueForReview(word: WordPair, currentTime: Long): Boolean {
    val progress = getProgress(word.id)
    return progress.nextReviewDate?.let { it <= currentTime } ?: true
}
```

### Loose Coupling Examples

**❌ BAD - ViewModel knows about other ViewModel**:
```kotlin
class LessonDetailViewModel(
    private val practiceViewModel: PracticeViewModel  // ❌ Tight coupling!
) : ViewModel() {
    fun startPractice() {
        practiceViewModel.loadLesson(lessonId)  // ❌ Direct dependency
    }
}
```

**✅ GOOD - Navigation with parameters, no cross-dependencies**:
```kotlin
class LessonDetailViewModel : ViewModel() {
    private val _navigateToPractice = MutableStateFlow<Long?>(null)
    val navigateToPractice: StateFlow<Long?> = _navigateToPractice
    
    fun startPractice() {
        _navigateToPractice.value = lessonId  // ✅ Emit event, let navigation handle it
    }
}

// In Composable:
val navigateToPractice by viewModel.navigateToPractice.collectAsState()
navigateToPractice?.let { lessonId ->
    navController.navigate("practice/$lessonId")
}
```

### Composable Best Practices

**❌ BAD - Composable accesses ViewModel directly**:
```kotlin
@Composable
fun LessonCard(lessonId: Long) {
    val viewModel: LessonListViewModel = viewModel()  // ❌ Direct access
    val lesson = viewModel.getLesson(lessonId)  // ❌ Tight coupling
    // UI code...
}
```

**✅ GOOD - Composable receives data via parameters**:
```kotlin
@Composable
fun LessonCard(
    lesson: Lesson,
    onCardClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onCardClick(lesson.id) },
        // UI code...
    ) {
        Text(lesson.title)
        Text("${lesson.totalWords} words")
    }
}

// Usage in parent:
LessonCard(
    lesson = lesson,
    onCardClick = { lessonId -> navController.navigate("detail/$lessonId") }
)
```

### Animation Examples

**Material3 with smooth animations**:
```kotlin
@Composable
fun FlashcardView(
    word: WordPair,
    isFlipped: Boolean,
    onFlip: () -> Unit
) {
    AnimatedContent(
        targetState = isFlipped,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) + 
            scaleIn(initialScale = 0.9f) with
            fadeOut(animationSpec = tween(300)) +
            scaleOut(targetScale = 0.9f)
        }
    ) { flipped ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onFlip() }
        ) {
            Text(
                text = if (flipped) word.vietnameseMeaning else word.englishTerm,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
```

---

## Task Completion Workflow

For **EVERY task** you complete:

```
1. ✅ Write the code
2. ✅ Compile: ./gradlew assembleDebug
3. ✅ Lint: ./gradlew lintDebug
4. ✅ Format: ./gradlew ktlintFormat (or IDE formatter)
5. ✅ Review: Read your own code critically
6. ✅ Test: Write unit test for domain logic (optional for UI)
7. ✅ Commit: Use proper commit message format
8. ✅ Mark task done in tasks.md
```

**Quality Gates Must Pass**:
- ✅ Zero compilation errors
- ✅ Zero lint errors (warnings addressed or justified)
- ✅ Code follows Kotlin style guide
- ✅ Self-reviewed and satisfied with code quality

---

## Common Issues & Solutions

### Issue: Gradle Sync Failed

**Solution**: 
```bash
# Clean project
./gradlew clean

# Invalidate caches in Android Studio
File → Invalidate Caches → Invalidate and Restart
```

### Issue: Room Schema Export Error

**Solution**: Add to `app/build.gradle.kts`:
```kotlin
android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
}
```

### Issue: Compose Preview Not Working

**Solution**: 
- Ensure `@Preview` annotation is used
- Rebuild project: **Build → Rebuild Project**
- Check that preview function has no parameters

### Issue: Tests Not Finding Resources

**Solution**: Add to `app/build.gradle.kts`:
```kotlin
android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
```

---

## Debugging Tips

### Logcat Filtering

```
# Filter by tag
adb logcat -s FlashcardApp

# Filter by process
adb logcat --pid=$(adb shell pidof -s com.englishflashcard)
```

### Compose Layout Inspector

1. Run app on device/emulator
2. **Tools → Layout Inspector**
3. Inspect Composable hierarchy and properties

### Performance Profiling

1. **Run → Profile 'app'**
2. Use CPU Profiler to find bottlenecks
3. Use Memory Profiler to detect leaks

---

## Useful Commands

```bash
# List connected devices
adb devices

# Clear app data (reset database)
adb shell pm clear com.englishflashcard

# Take screenshot
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png

# View app logs in real-time
adb logcat -s FlashcardApp:V *:E
```

---

## Next Steps

1. **Review Specification**: Read [spec.md](./spec.md) for requirements
2. **Study Data Model**: Review [data-model.md](./data-model.md) for database schema
3. **Check Contracts**: See [contracts/](./contracts/) for repository and use case interfaces
4. **Run Existing Tests**: Familiarize yourself with test structure
5. **Start Coding**: Pick a user story from [spec.md](./spec.md) and implement it

---

## Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Material Design 3](https://m3.material.io/)
- [Android Testing Guide](https://developer.android.com/training/testing)

---

## Getting Help

- Check existing issues in GitHub repository
- Review specification documents in `specs/001-flashcard-learning-app/`
- Consult Android Developer documentation
- Ask team members via project communication channels

---

**Ready to Code?** 🚀

Start by running the tests to ensure everything is set up correctly:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

If all tests pass, you're ready to start implementing features!

