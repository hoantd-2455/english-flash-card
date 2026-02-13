# Tasks: English Vocabulary Flashcard Learning App

**Input**: Design documents from `/specs/001-flashcard-learning-app/`  
**Prerequisites**: plan.md ✅, spec.md ✅, research.md ✅, data-model.md ✅, contracts/ ✅

**Tests**: Tests are OPTIONAL in this project. We focus on core functionality first, tests can be added later as quality improvements.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `- [ ] [ID] [P?] [Story?] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

Android project structure (from plan.md):
- **Main source**: `app/src/main/java/com/englishflashcard/`
- **Resources**: `app/src/main/res/`
- **Unit tests**: `app/src/test/java/com/englishflashcard/`
- **UI tests**: `app/src/androidTest/java/com/englishflashcard/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create Android project structure per plan.md (app module with Kotlin DSL)
- [ ] T002 Initialize Gradle dependencies (Compose BOM, Room, DataStore, Navigation, Coroutines)
- [ ] T003 [P] Configure build.gradle.kts with KSP for Room annotation processing
- [ ] T004 [P] Setup Material3 theme in app/src/main/java/com/englishflashcard/ui/theme/
- [ ] T005 Create AndroidManifest.xml with app permissions and MainActivity declaration
- [ ] T006 [P] Setup ktlint plugin in build.gradle.kts for code formatting
- [ ] T007 [P] Configure lint options in app/build.gradle.kts (abortOnError = true)
- [ ] T008 [P] Define spacing dimensions in res/values/dimens.xml (4dp, 8dp, 16dp, 24dp, 32dp)

**Checkpoint**: Project setup complete with quality tools configured

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Database Foundation

- [ ] T006 Create Room database class in app/src/main/java/com/englishflashcard/data/local/database/FlashcardDatabase.kt
- [ ] T007 [P] Create Lesson entity in app/src/main/java/com/englishflashcard/data/local/entity/LessonEntity.kt
- [ ] T008 [P] Create WordPair entity in app/src/main/java/com/englishflashcard/data/local/entity/WordPairEntity.kt
- [ ] T009 [P] Create LearningProgress entity in app/src/main/java/com/englishflashcard/data/local/entity/LearningProgressEntity.kt
- [ ] T010 [P] Create PracticeSession entity in app/src/main/java/com/englishflashcard/data/local/entity/PracticeSessionEntity.kt
- [ ] T011 [P] Create QuizResult entity in app/src/main/java/com/englishflashcard/data/local/entity/QuizResultEntity.kt
- [ ] T012 Create LessonDao in app/src/main/java/com/englishflashcard/data/local/database/LessonDao.kt
- [ ] T013 [P] Create WordPairDao in app/src/main/java/com/englishflashcard/data/local/database/WordPairDao.kt
- [ ] T014 [P] Create LearningProgressDao in app/src/main/java/com/englishflashcard/data/local/database/LearningProgressDao.kt
- [ ] T015 [P] Create PracticeSessionDao in app/src/main/java/com/englishflashcard/data/local/database/PracticeSessionDao.kt
- [ ] T016 [P] Create QuizResultDao in app/src/main/java/com/englishflashcard/data/local/database/QuizResultDao.kt

### DataStore Foundation

- [ ] T017 Create PreferencesDataStore in app/src/main/java/com/englishflashcard/data/local/preferences/PreferencesDataStore.kt
- [ ] T018 Define UserPreferences data class in app/src/main/java/com/englishflashcard/domain/model/UserPreferences.kt

### Domain Models

- [ ] T019 [P] Create Lesson domain model in app/src/main/java/com/englishflashcard/domain/model/Lesson.kt
- [ ] T020 [P] Create WordPair domain model in app/src/main/java/com/englishflashcard/domain/model/WordPair.kt
- [ ] T021 [P] Create LearningProgress domain model in app/src/main/java/com/englishflashcard/domain/model/LearningProgress.kt
- [ ] T022 [P] Create PracticeSession domain model in app/src/main/java/com/englishflashcard/domain/model/PracticeSession.kt
- [ ] T023 [P] Create QuizResult domain model in app/src/main/java/com/englishflashcard/domain/model/QuizResult.kt
- [ ] T024 [P] Create enum classes (MasteryLevel, DifficultyRating, TextSize) in app/src/main/java/com/englishflashcard/domain/model/

### Repository Implementations

- [ ] T025 Implement LessonRepository in app/src/main/java/com/englishflashcard/data/repository/LessonRepositoryImpl.kt
- [ ] T026 [P] Implement WordPairRepository in app/src/main/java/com/englishflashcard/data/repository/WordPairRepositoryImpl.kt
- [ ] T027 [P] Implement LearningProgressRepository in app/src/main/java/com/englishflashcard/data/repository/LearningProgressRepositoryImpl.kt
- [ ] T028 [P] Implement PracticeSessionRepository in app/src/main/java/com/englishflashcard/data/repository/PracticeSessionRepositoryImpl.kt
- [ ] T029 [P] Implement QuizResultRepository in app/src/main/java/com/englishflashcard/data/repository/QuizResultRepositoryImpl.kt
- [ ] T030 [P] Implement PreferencesRepository in app/src/main/java/com/englishflashcard/data/repository/PreferencesRepositoryImpl.kt

### Navigation Setup

- [ ] T031 Create Navigation graph in app/src/main/java/com/englishflashcard/ui/navigation/NavGraph.kt
- [ ] T032 Define Screen sealed class for navigation routes in app/src/main/java/com/englishflashcard/ui/navigation/Screen.kt
- [ ] T033 Setup MainActivity with NavHost in app/src/main/java/com/englishflashcard/MainActivity.kt

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Create and Manage Vocabulary Lessons (Priority: P1) 🎯 MVP

**Goal**: Enable users to create, view, edit, and delete vocabulary lessons with English-Vietnamese word pairs

**Independent Test**: Create a lesson with title, description, and 5 word pairs → Save → View in lesson list → Edit lesson → Delete lesson. All data persists correctly.

### Implementation for User Story 1

#### Lesson List Screen (Empty State + List View)

- [ ] T034 [P] [US1] Create LessonListViewModel in app/src/main/java/com/englishflashcard/ui/screens/lessonlist/LessonListViewModel.kt
- [ ] T035 [P] [US1] Create LessonListUiState in app/src/main/java/com/englishflashcard/ui/screens/lessonlist/LessonListUiState.kt
- [ ] T036 [US1] Implement LessonListScreen composable in app/src/main/java/com/englishflashcard/ui/screens/lessonlist/LessonListScreen.kt
- [ ] T037 [US1] Create LessonCard composable for displaying lesson items in app/src/main/java/com/englishflashcard/ui/screens/lessonlist/LessonCard.kt
- [ ] T038 [US1] Create EmptyState composable with "Add Lesson" button in app/src/main/java/com/englishflashcard/ui/screens/lessonlist/EmptyState.kt

#### Lesson Create/Edit Screen

- [ ] T039 [P] [US1] Create LessonEditViewModel in app/src/main/java/com/englishflashcard/ui/screens/lessonedit/LessonEditViewModel.kt
- [ ] T040 [P] [US1] Create LessonEditUiState in app/src/main/java/com/englishflashcard/ui/screens/lessonedit/LessonEditUiState.kt
- [ ] T041 [US1] Implement LessonEditScreen composable in app/src/main/java/com/englishflashcard/ui/screens/lessonedit/LessonEditScreen.kt
- [ ] T042 [US1] Create WordPairInputCard composable (2 text fields: English + Vietnamese) in app/src/main/java/com/englishflashcard/ui/screens/lessonedit/WordPairInputCard.kt
- [ ] T043 [US1] Add validation logic in ViewModel (title not empty, at least 1 word pair)
- [ ] T044 [US1] Implement Save functionality with repository call

#### Lesson Detail Screen

- [ ] T045 [P] [US1] Create LessonDetailViewModel in app/src/main/java/com/englishflashcard/ui/screens/lessondetail/LessonDetailViewModel.kt
- [ ] T046 [P] [US1] Create LessonDetailUiState in app/src/main/java/com/englishflashcard/ui/screens/lessondetail/LessonDetailUiState.kt
- [ ] T047 [US1] Implement LessonDetailScreen composable in app/src/main/java/com/englishflashcard/ui/screens/lessondetail/LessonDetailScreen.kt
- [ ] T048 [US1] Display word list with English-Vietnamese pairs
- [ ] T049 [US1] Add Edit and Delete buttons with confirmation dialog for delete
- [ ] T050 [US1] Show progress metrics placeholder (will be populated by US3)

#### Use Cases for Lesson Management

- [ ] T051 [P] [US1] Implement LessonManagementUseCase in app/src/main/java/com/englishflashcard/domain/usecase/LessonManagementUseCase.kt
- [ ] T052 [US1] Add createLessonWithWords method with transaction handling
- [ ] T053 [US1] Add updateLessonWithWords method
- [ ] T054 [US1] Add deleteLesson method with cascade delete

**Checkpoint**: At this point, User Story 1 should be fully functional - users can create, view, edit, and delete lessons

---

## Phase 4: User Story 2 - Practice with Flashcards (Priority: P2)

**Goal**: Enable users to study vocabulary through interactive flashcards with swipe gestures (left = don't know, right = know)

**Independent Test**: Select a lesson → Start practice → See flashcard → Tap to flip → Swipe left/right → See next card → Complete session → View summary

### Implementation for User Story 2

#### Practice Screen

- [ ] T055 [P] [US2] Create PracticeViewModel in app/src/main/java/com/englishflashcard/ui/screens/practice/PracticeViewModel.kt
- [ ] T056 [P] [US2] Create PracticeUiState in app/src/main/java/com/englishflashcard/ui/screens/practice/PracticeUiState.kt
- [ ] T057 [US2] Implement PracticeScreen composable in app/src/main/java/com/englishflashcard/ui/screens/practice/PracticeScreen.kt
- [ ] T058 [US2] Create FlashcardView composable with tap-to-flip animation in app/src/main/java/com/englishflashcard/ui/components/FlashcardView.kt
- [ ] T059 [US2] Implement swipe gesture detection (SwipeToDismiss or custom gesture)
- [ ] T060 [US2] Add visual feedback for swipe direction (left = red, right = green)
- [ ] T061 [US2] Implement card progression logic (advance to next card on swipe)
- [ ] T062 [US2] Create SessionSummary composable showing cards reviewed, known, unknown in app/src/main/java/com/englishflashcard/ui/screens/practice/SessionSummary.kt

#### Use Cases for Practice

- [ ] T063 [P] [US2] Implement PracticeFlowUseCase in app/src/main/java/com/englishflashcard/domain/usecase/PracticeFlowUseCase.kt
- [ ] T064 [US2] Add getPracticeCards method with shuffle support
- [ ] T065 [US2] Add recordCardReview method to track practice session
- [ ] T066 [US2] Integrate with PracticeSessionRepository to save session data

#### Practice Button in Lesson Detail

- [ ] T067 [US2] Add "Study" button to LessonDetailScreen
- [ ] T068 [US2] Navigate to PracticeScreen with lessonId parameter

**Checkpoint**: At this point, User Stories 1 AND 2 should both work - users can create lessons and practice them

---

## Phase 5: User Story 3 - Spaced Repetition Scheduling (Priority: P3)

**Goal**: Implement intelligent review scheduling based on SM-2 algorithm with 4 difficulty levels (Again/Hard/Good/Easy)

**Independent Test**: Practice a lesson → Rate cards with different difficulty levels → Check that nextReviewDate is calculated correctly → Verify due cards appear in recommended banner

### Implementation for User Story 3

#### Enhanced Practice with Difficulty Rating

- [ ] T069 [US3] Add difficulty rating buttons (Again/Hard/Good/Easy) to PracticeScreen
- [ ] T070 [US3] Update PracticeUiState to include difficulty rating UI
- [ ] T071 [US3] Modify swipe gestures to trigger difficulty rating selection instead of binary know/don't know
- [ ] T072 [US3] Create DifficultyRatingButtons composable in app/src/main/java/com/englishflashcard/ui/components/DifficultyRatingButtons.kt

#### SM-2 Algorithm Implementation

- [ ] T073 [P] [US3] Implement SpacedRepetitionUseCase in app/src/main/java/com/englishflashcard/domain/usecase/SpacedRepetitionUseCase.kt
- [ ] T074 [US3] Implement calculateNextReview method with SM-2 algorithm logic
- [ ] T075 [US3] Add tests for SM-2 algorithm calculations (Again: 1 day, Hard: ×1.2, Good: ×easiness, Easy: ×easiness×1.3)
- [ ] T076 [US3] Implement mastery level transitions (NEW → LEARNING → MASTERED)

#### Progress Tracking

- [ ] T077 [US3] Update PracticeViewModel to record difficulty ratings with SpacedRepetitionUseCase
- [ ] T078 [US3] Create LearningProgress records on first practice of each word pair
- [ ] T079 [US3] Update progress metrics in LessonDetailScreen (due cards, learning, mastered)
- [ ] T080 [US3] Implement LessonStatsUseCase in app/src/main/java/com/englishflashcard/domain/usecase/LessonStatsUseCase.kt

#### Recommended Lesson Banner

- [ ] T081 [US3] Add suggestion banner to LessonListScreen showing recommended lesson
- [ ] T082 [US3] Implement getRecommendedLesson method in SpacedRepetitionUseCase
- [ ] T083 [US3] Display lesson with most due cards in banner
- [ ] T084 [US3] Add quick "Start Practice" button in banner

#### Due Cards Priority

- [ ] T085 [US3] Modify getPracticeCards to prioritize due cards first
- [ ] T086 [US3] Sort due cards by nextReviewDate (earliest first)
- [ ] T087 [US3] Display badge on lesson cards showing number of due reviews

**Checkpoint**: All core learning features complete - spaced repetition working, progress tracked, recommendations shown

---

## Phase 6: User Story 4 - Test Knowledge with Mixed Quiz Formats (Priority: P4)

**Goal**: Enable users to test their knowledge through adaptive quizzes mixing multiple-choice (4 options) and typing questions

**Independent Test**: Select lesson → Start quiz → Answer mix of MC and typing questions → Get immediate feedback → See results summary with breakdown by question type

### Implementation for User Story 4

#### Quiz Screen

- [ ] T088 [P] [US4] Create QuizViewModel in app/src/main/java/com/englishflashcard/ui/screens/quiz/QuizViewModel.kt
- [ ] T089 [P] [US4] Create QuizUiState in app/src/main/java/com/englishflashcard/ui/screens/quiz/QuizUiState.kt
- [ ] T090 [US4] Implement QuizScreen composable in app/src/main/java/com/englishflashcard/ui/screens/quiz/QuizScreen.kt
- [ ] T091 [US4] Create MultipleChoiceQuestion composable with A/B/C/D options in app/src/main/java/com/englishflashcard/ui/screens/quiz/MultipleChoiceQuestion.kt
- [ ] T092 [US4] Create TypingQuestion composable with text input field in app/src/main/java/com/englishflashcard/ui/screens/quiz/TypingQuestion.kt
- [ ] T093 [US4] Implement immediate feedback UI (correct/incorrect with answer shown)
- [ ] T094 [US4] Create QuizResults composable showing score, MC/typing breakdown in app/src/main/java/com/englishflashcard/ui/screens/quiz/QuizResults.kt

#### Quiz Generation Logic

- [ ] T095 [P] [US4] Implement QuizGenerationUseCase in app/src/main/java/com/englishflashcard/domain/usecase/QuizGenerationUseCase.kt
- [ ] T096 [US4] Add generateQuiz method with adaptive format logic (< 4 words = typing only, ≥ 4 words = 50/50 mix)
- [ ] T097 [US4] Implement multiple-choice option generation (1 correct + 3 random wrong from same lesson)
- [ ] T098 [US4] Implement typing answer validation (case-insensitive, trimmed comparison)
- [ ] T099 [US4] Shuffle questions to mix MC and typing randomly

#### Quiz Results Tracking

- [ ] T100 [US4] Save QuizResult to database after completion
- [ ] T101 [US4] Display quiz history in LessonDetailScreen
- [ ] T102 [US4] Calculate and display average score per lesson

#### Test Button in Lesson Detail

- [ ] T103 [US4] Add "Test" button to LessonDetailScreen
- [ ] T104 [US4] Navigate to QuizScreen with lessonId parameter
- [ ] T105 [US4] Disable test button if lesson has < 1 word (show tooltip)

**Checkpoint**: Assessment features complete - users can test knowledge with adaptive quiz formats

---

## Phase 7: User Story 5 - Customize App Settings (Priority: P5)

**Goal**: Allow users to personalize flashcard appearance (color, text size) via settings screen

**Independent Test**: Open settings → Change flashcard color → Change text size → Close settings → Verify changes persist and apply in practice mode

### Implementation for User Story 5

#### Settings Screen

- [ ] T106 [P] [US5] Create SettingsViewModel in app/src/main/java/com/englishflashcard/ui/screens/settings/SettingsViewModel.kt
- [ ] T107 [P] [US5] Create SettingsUiState in app/src/main/java/com/englishflashcard/ui/screens/settings/SettingsUiState.kt
- [ ] T108 [US5] Implement SettingsScreen composable in app/src/main/java/com/englishflashcard/ui/screens/settings/SettingsScreen.kt
- [ ] T109 [US5] Create ColorPicker composable for flashcard background color in app/src/main/java/com/englishflashcard/ui/screens/settings/ColorPicker.kt
- [ ] T110 [US5] Create TextSizeSelector composable with options (Small/Medium/Large/Extra Large) in app/src/main/java/com/englishflashcard/ui/screens/settings/TextSizeSelector.kt
- [ ] T111 [US5] Add toggle for practice shuffle setting

#### Settings Use Case

- [ ] T112 [P] [US5] Implement SettingsUseCase in app/src/main/java/com/englishflashcard/domain/usecase/SettingsUseCase.kt
- [ ] T113 [US5] Add validation for hex color format (#RRGGBB)
- [ ] T114 [US5] Implement resetToDefaults method

#### Apply Settings in Practice Mode

- [ ] T115 [US5] Update FlashcardView to use preferences for background color
- [ ] T116 [US5] Update FlashcardView to use preferences for text size
- [ ] T117 [US5] Ensure settings persist across app restarts via DataStore

#### Settings Button in Top-Right

- [ ] T118 [US5] Add settings icon button to LessonListScreen top bar
- [ ] T119 [US5] Navigate to SettingsScreen on button click

**Checkpoint**: All user stories complete - full app functionality delivered

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories and final quality checks

### Code Quality & Constitution Compliance (PRIORITY)

- [ ] T120 Run lint on entire codebase and fix ALL errors: `./gradlew lintDebug`
- [ ] T121 Format entire codebase: `./gradlew ktlintFormat`
- [ ] T122 [P] Review all ViewModels - ensure NO cross-dependencies between VMs
- [ ] T123 [P] Review all Composables - ensure data passed via parameters, not direct access
- [ ] T124 [P] Check function lengths - split any function > 30 lines (except UI layouts)
- [ ] T125 [P] Check class sizes - refactor any class > 200 lines
- [ ] T126 [P] Find and remove all magic numbers - replace with named constants
- [ ] T127 [P] Add meaningful names - refactor any unclear variable/function names
- [ ] T128 Verify Clean Architecture layers - no upward dependencies (Data → Domain → UI)
- [ ] T129 Verify Domain layer has zero Android imports (pure Kotlin only)

### UI/UX Polish

- [ ] T130 [P] Add loading states to all screens (shimmer effect or progress indicators)
- [ ] T131 [P] Add error handling UI (toast messages, error screens)
- [ ] T132 [P] Improve animations and transitions between screens (Navigation animations)
- [ ] T133 [P] Add haptic feedback for swipe gestures in practice mode
- [ ] T134 Implement proper back button handling and navigation stack
- [ ] T135 [P] Verify all animations run at 60fps (use GPU profiler)
- [ ] T136 [P] Test flashcard flip animation smoothness on low-end device

### Data Validation & Edge Cases

- [ ] T137 [P] Handle empty lesson (prevent saving without word pairs)
- [ ] T138 [P] Handle quiz with < 4 words (typing-only mode validation)
- [ ] T139 [P] Handle deletion confirmation dialogs
- [ ] T140 Implement proper error messages for all validation failures

### Performance & Optimization

- [ ] T141 [P] Add database indexes verification (check plan from data-model.md)
- [ ] T142 [P] Optimize Room queries with pagination for large datasets
- [ ] T143 [P] Test app performance with 100+ lessons and 1000+ word pairs
- [ ] T144 Verify 60fps animations in practice mode (GPU rendering profiler)

### Resources & Material3 Compliance

- [ ] T145 [P] Move hardcoded strings to res/values/strings.xml
- [ ] T146 [P] Add content descriptions for accessibility
- [ ] T147 [P] Verify Material3 color scheme - maximum 3 colors used
- [ ] T148 [P] Verify spacing uses dimens.xml values (4dp, 8dp, 16dp, 24dp, 32dp)
- [ ] T149 [P] Add app icon and splash screen

### Documentation & Cleanup

- [ ] T150 [P] Add KDoc comments to all public APIs
- [ ] T151 [P] Code cleanup and remove unused imports
- [ ] T152 [P] Delete all dead/commented-out code
- [ ] T153 Run quickstart.md validation (setup project fresh and verify all steps work)

### Final Testing & Constitution Verification

- [ ] T154 Test complete user journey: Create lesson → Practice → Review progress → Take quiz → Customize settings
- [ ] T155 Test data persistence across app kill and restart
- [ ] T156 Test Vietnamese Unicode rendering and input
- [ ] T157 Verify all success criteria from spec.md are met
- [ ] T158 **Final Constitution Check**: Review `.specify/memory/constitution.md` and verify ALL principles followed
- [ ] T159 Run full quality gate: `./gradlew clean lintDebug ktlintCheck assembleDebug test`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - **BLOCKS all user stories**
- **User Story 1 (Phase 3)**: Depends on Foundational - Can start immediately after Phase 2
- **User Story 2 (Phase 4)**: Depends on Foundational + US1 completion (needs lesson detail screen)
- **User Story 3 (Phase 5)**: Depends on Foundational + US2 completion (enhances practice mode)
- **User Story 4 (Phase 6)**: Depends on Foundational + US1 completion (needs lesson detail screen for test button)
- **User Story 5 (Phase 7)**: Depends on Foundational + US2 completion (applies to practice mode)
- **Polish (Phase 8)**: Depends on all desired user stories being complete

### User Story Independence

- **US1 (Lessons)**: Fully independent - can be completed and tested alone ✅ MVP
- **US2 (Practice)**: Integrates with US1 but has independent value
- **US3 (Spaced Repetition)**: Enhances US2 but practice works without it
- **US4 (Quiz)**: Independent test mode, separate from practice
- **US5 (Settings)**: Independent customization feature

### Within Each User Story

- ViewModels and UiStates can be created in parallel [P]
- Models before repositories
- Repositories before use cases
- Use cases before ViewModels
- ViewModels before UI screens
- Core screen before navigation integration

### Parallel Opportunities

**Phase 1 (Setup)**:
- T003, T004 can run in parallel

**Phase 2 (Foundational)**:
- All entity creations (T007-T011) can run in parallel
- All DAO creations (T013-T016) can run in parallel after database setup
- All domain models (T019-T024) can run in parallel
- All repository implementations (T026-T030) can run in parallel after DAOs ready

**Phase 3 (US1)**:
- T034 + T035 can start together
- T039 + T040 can start together
- T045 + T046 can start together
- T051 can run in parallel with screens

**Phase 4-7**: Similar parallel opportunities for ViewModel + UiState pairs

**Phase 8 (Polish)**: Most tasks are independent and can run in parallel

---

## Parallel Example: Foundational Phase (Database Setup)

```bash
# Start together after T006 (database class) completes:
[P] T007: Create Lesson entity
[P] T008: Create WordPair entity
[P] T009: Create LearningProgress entity
[P] T010: Create PracticeSession entity
[P] T011: Create QuizResult entity

# Then start together after entities complete:
[P] T013: Create WordPairDao
[P] T014: Create LearningProgressDao
[P] T015: Create PracticeSessionDao
[P] T016: Create QuizResultDao
```

---

## Implementation Strategy

### MVP First (Recommended)

**Goal**: Deliver working flashcard app ASAP

1. ✅ Complete Phase 1: Setup
2. ✅ Complete Phase 2: Foundational (28 tasks)
3. ✅ Complete Phase 3: User Story 1 (21 tasks)
4. **STOP and VALIDATE**: Test lesson CRUD independently
5. Deploy/Demo MVP: Users can create and manage lessons

**MVP Deliverable**: Basic lesson management - users can create, view, edit, delete lessons with word pairs

### Incremental Delivery

**Iteration 1** (MVP): Setup + Foundational + US1 = Lesson Management (49 tasks)

**Iteration 2**: Add US2 = Practice Mode (14 tasks)  
→ Users can now practice flashcards!

**Iteration 3**: Add US3 = Spaced Repetition (19 tasks)  
→ Intelligent review scheduling!

**Iteration 4**: Add US4 = Quiz Mode (18 tasks)  
→ Assessment capability!

**Iteration 5**: Add US5 = Settings (14 tasks)  
→ Personalization!

**Final**: Polish (24 tasks)  
→ Production-ready app!

### Parallel Team Strategy

With 3 developers after Foundational phase:

- **Developer A**: User Story 1 (Lesson Management)
- **Developer B**: User Story 4 (Quiz Mode - independent of practice)
- **Developer C**: Setup infrastructure for US2 & US5

After US1 completes:
- **Developer A**: User Story 2 (Practice)
- **Developer B**: Continue US4
- **Developer C**: User Story 5 (Settings)

After US2 completes:
- **Developer A**: User Story 3 (Spaced Repetition - enhances practice)

---

## Task Count Summary

- **Phase 1 (Setup)**: 8 tasks (was 5)
- **Phase 2 (Foundational)**: 28 tasks ⚠️ BLOCKING
- **Phase 3 (US1 - Lessons)**: 21 tasks 🎯 MVP
- **Phase 4 (US2 - Practice)**: 14 tasks
- **Phase 5 (US3 - Spaced Repetition)**: 19 tasks
- **Phase 6 (US4 - Quiz)**: 18 tasks
- **Phase 7 (US5 - Settings)**: 14 tasks
- **Phase 8 (Polish)**: 40 tasks (was 24) ⭐ Quality Focus

**Total**: 162 tasks (was 143)

**MVP Milestone** (Phases 1-3): 57 tasks (was 54)  
**Full Feature Set** (Phases 1-7): 122 tasks (was 119)  
**Production Ready** (All phases): 162 tasks (was 143)

**New in this version**: 
- +3 quality setup tasks (Phase 1)
- +16 code quality & constitution compliance tasks (Phase 8)

---

## Success Criteria Mapping

From spec.md, all success criteria are addressed:

- ✅ **SC-001** (Create lesson in < 5 min): US1 tasks cover lesson creation UI
- ✅ **SC-002** (Practice 20 cards in < 3 min): US2 tasks implement efficient flashcard UX
- ✅ **SC-003** (Track progress %): US3 tasks implement progress metrics
- ✅ **SC-004** (90% first-time completion): US1 empty state + guided UX
- ✅ **SC-005** (Spaced repetition intervals): US3 T073-T076 implement SM-2 algorithm
- ✅ **SC-006** (Find lesson in 2 taps): US3 T081-T084 implement recommendation banner
- ✅ **SC-007** (Quiz accuracy): US4 T095-T099 implement quiz logic
- ✅ **SC-008** (100% data retention): Phase 2 foundational database setup
- ✅ **SC-009** (Settings apply immediately): US5 T115-T117 apply preferences
- ✅ **SC-010** (1-2 tap navigation): T031-T033 navigation setup

---

## Notes

- **READ CONSTITUTION FIRST**: Before starting any task, read `.specify/memory/constitution.md`
- Tasks marked [P] can run in parallel (different files, no blocking dependencies)
- [US#] labels map tasks to user stories from spec.md for traceability
- Each user story should be independently testable at its checkpoint
- Tests are OPTIONAL - focus on implementation first, add tests as needed
- **AFTER EVERY TASK**: Run lint → Format code → Self-review → Fix issues before marking done
- Commit after each task or logical group of related tasks
- Stop at any checkpoint to validate story works independently
- Foundation phase (28 tasks) must complete before ANY user story work begins

**Coding Principles (NON-NEGOTIABLE)**:
- ✅ Clean Code: Functions ≤ 30 lines, meaningful names, no magic numbers
- ✅ Loose Coupling: No ViewModel cross-dependencies, parameters over direct access
- ✅ Simple UI: Material3 by default, 3-color max, consistent spacing (dimens.xml)
- ✅ Smooth Animations: 60fps target for all animations
- ✅ Quality Gates: Zero lint errors, proper formatting, self-review

**Task Completion Workflow**:
```
1. Write code
2. Compile: ./gradlew assembleDebug
3. Lint: ./gradlew lintDebug
4. Format: ./gradlew ktlintFormat
5. Review: Read your own code
6. Test: Unit test for domain logic (optional)
7. Commit: Use proper format (feat/fix/refactor)
8. Mark task done in tasks.md
```

**Ready to implement!** Start with Phase 1 and work through sequentially, or parallelize within phases where marked [P].

