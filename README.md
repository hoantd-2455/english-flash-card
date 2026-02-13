# English Flashcard Learning App 📚

A modern Android vocabulary learning application built with Kotlin and Jetpack Compose, featuring interactive flashcards and spaced repetition learning.

## 🎯 Features

### ✅ Implemented (MVP)

#### User Story 1: Lesson Management
- Create custom vocabulary lessons with English-Vietnamese word pairs
- View all lessons in organized list
- Edit existing lessons
- Delete lessons with confirmation
- View lesson details with word pairs
- Form validation (title required, minimum 1 word pair)

#### User Story 2: Practice Mode
- Interactive flashcard practice
- Tap-to-flip animation (smooth 400ms transition)
- Know/Don't Know tracking
- Real-time progress indicator
- Session summary with statistics
- Card shuffling support
- Practice restart option

### 🚧 Coming Soon

- **User Story 3**: Spaced Repetition with SM-2 algorithm
- **User Story 4**: Mixed Quiz Mode (Multiple-choice + Typing)
- **User Story 5**: Customizable Settings (Theme, Text Size)
- **Polish**: Performance optimization, Accessibility, Tests

## 🏗️ Tech Stack

- **Language**: Kotlin 1.9.20
- **UI**: Jetpack Compose (Material3)
- **Architecture**: MVVM + Clean Architecture
- **Database**: Room (SQLite)
- **Storage**: DataStore (Preferences)
- **Async**: Kotlin Coroutines + Flow
- **Navigation**: Compose Navigation
- **Build**: Gradle 8.5 + KSP

## 📋 Requirements

- **Android Studio**: Hedgehog (2023.1.1) or later
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Java**: JDK 17 (Amazon Corretto 17.0.8 recommended)
- **Gradle**: 8.5+

**⚠️ Important**: This project requires **Java 17**. Java 21 is not compatible with Android Gradle Plugin 8.1.4.

## 🏛️ Architecture

```
app/
├── di/                          # Dependency Injection
│   └── AppContainer.kt          # DI container
├── domain/                      # Business Logic Layer
│   ├── model/                   # Domain models
│   └── repository/              # Repository interfaces
├── data/                        # Data Layer
│   ├── local/
│   │   ├── database/           # Room Database + DAOs
│   │   ├── entity/             # Database entities
│   │   └── preferences/        # DataStore
│   └── repository/             # Repository implementations
└── ui/                          # Presentation Layer
    ├── theme/                   # Material3 theme
    ├── navigation/              # Navigation
    ├── components/              # Reusable UI components
    └── screens/
        ├── lessonlist/         # Lesson list screen
        ├── lessonedit/         # Create/Edit lesson
        ├── lessondetail/       # Lesson details
        └── practice/           # Flashcard practice
```

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │
│  Screens, ViewModels, Composables   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Domain Layer (Pure Kotlin)    │
│    Models, Repository Interfaces     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Data Layer                   │
│  Room, DataStore, Implementations    │
└─────────────────────────────────────┘
```

## 📱 Screenshots

*(Screenshots will be added after UI implementation)*

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run lint checks
./gradlew lintDebug

# Run code formatting
./gradlew ktlintFormat
```

## 🎨 Design Principles

Following the project constitution:

1. **Clean Code**
   - Functions ≤ 30 lines
   - Classes ≤ 200 lines
   - Meaningful names
   - No magic numbers

2. **Loose Coupling**
   - ViewModels are independent
   - Composables receive data via parameters
   - Interface-based design

3. **Simple UI**
   - Material3 components by default
   - 3-color scheme maximum
   - Consistent spacing (4dp, 8dp, 16dp, 24dp, 32dp)

4. **Smooth Animations**
   - 60fps target
   - Spring-based transitions
   - Optimized rendering

## 📊 Project Status

| Component | Status | Tasks |
|-----------|--------|-------|
| Foundation | ✅ Complete | 28/28 (100%) |
| User Story 1 (Lessons) | ✅ Complete | 17/21 (81%) |
| User Story 2 (Practice) | ✅ Complete | 10/14 (71%) |
| User Story 3 (Spaced Rep) | ⏳ Planned | 0/19 (0%) |
| User Story 4 (Quiz) | ⏳ Planned | 0/18 (0%) |
| User Story 5 (Settings) | ⏳ Planned | 0/14 (0%) |
| Polish & Tests | ⏳ Planned | 0/40 (0%) |

**Overall Progress**: 55/162 tasks (34%)

## 📝 Development Workflow

1. **Pick a task** from `specs/001-flashcard-learning-app/tasks.md`
2. **Implement** following the architecture layers
3. **Test** manually and with unit tests
4. **Lint** and format code
5. **Commit** with proper message format
6. **Mark task** as done in tasks.md

## 🤝 Contributing

1. Follow the coding guidelines in `specs/001-flashcard-learning-app/coding-guidelines.md`
2. Read the constitution in `.specify/memory/constitution.md`
3. Keep functions small and focused
4. Add KDoc comments for public APIs
5. Write tests for business logic

## 📄 License

*(Add your license here)*

## 👥 Authors

- Your Name - *Initial work*

## 🙏 Acknowledgments

- Jetpack Compose team for the modern UI toolkit
- Room persistence library
- Material Design 3 guidelines
- Kotlin Coroutines team

---

**Built with ❤️ using Kotlin and Jetpack Compose**

