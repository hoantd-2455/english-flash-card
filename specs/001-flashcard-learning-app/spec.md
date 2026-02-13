# Feature Specification: English Vocabulary Flashcard Learning App

**Feature Branch**: `001-flashcard-learning-app`  
**Created**: February 13, 2026  
**Status**: Draft  
**Input**: User description: "English vocabulary flashcard app with spaced repetition learning for Android"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Create and Manage Vocabulary Lessons (Priority: P1)

A user wants to create personalized vocabulary lessons with English-Vietnamese word pairs to build their learning library.

**Why this priority**: This is the core content creation capability - without lessons, there's nothing to learn. This is the foundation for all other features.

**Independent Test**: Can be fully tested by creating a new lesson with title, description, and word pairs, saving it, and verifying it persists in local storage and appears in the lesson list.

**Acceptance Scenarios**:

1. **Given** the app is opened with no existing lessons, **When** user taps the "Add Lesson" button, **Then** the lesson creation form is displayed
2. **Given** the lesson creation form is open, **When** user enters title, description, and at least one word pair (English term + Vietnamese meaning), and taps "Save", **Then** the lesson is saved to local storage and appears in the lesson list
3. **Given** a lesson exists in the list, **When** user taps on it, **Then** the lesson detail screen shows the complete word list and learning progress
4. **Given** the lesson detail screen is open, **When** user taps "Edit", **Then** user can modify title, description, add/remove/edit word pairs, and save changes
5. **Given** the lesson detail screen is open in edit mode, **When** user chooses to delete the lesson, **Then** the lesson is removed from storage and the user returns to the lesson list
6. **Given** creating or editing a lesson, **When** user adds multiple word pairs, **Then** each pair is displayed as a card with two input fields (English and Vietnamese)

---

### User Story 2 - Practice with Flashcards (Priority: P2)

A user wants to study vocabulary by reviewing flashcards, flipping them to see meanings, and marking their confidence level to inform the spaced repetition algorithm.

**Why this priority**: This is the primary learning activity. Without effective practice, the app doesn't fulfill its educational purpose.

**Independent Test**: Can be tested by selecting a lesson, starting practice mode, viewing flashcards, flipping them, and swiping left (don't know) or right (know) to progress through the deck.

**Acceptance Scenarios**:

1. **Given** a lesson with word pairs exists, **When** user taps the "Study" button on lesson detail screen, **Then** practice mode begins showing flashcards
2. **Given** practice mode is active, **When** flashcard is displayed, **Then** one side shows (either English or Vietnamese, can be randomized)
3. **Given** a flashcard is displayed, **When** user taps on it, **Then** the card flips to reveal the other side (translation)
4. **Given** user has reviewed a flashcard, **When** user swipes left, **Then** the card is marked as "don't know" and next card appears
5. **Given** user has reviewed a flashcard, **When** user swipes right, **Then** the card is marked as "know" and next card appears
6. **Given** all cards in the lesson have been reviewed, **When** the last card is swiped, **Then** practice session summary is displayed showing progress
7. **Given** practice mode starts, **When** flashcards are presented, **Then** they can be shown in shuffled order to prevent memorization by position

---

### User Story 3 - Spaced Repetition Scheduling (Priority: P3)

A user wants the system to intelligently schedule reviews based on their confidence ratings to optimize long-term retention.

**Why this priority**: This enhances learning effectiveness by implementing proven spaced repetition principles, but basic practice can function without it.

**Independent Test**: Can be tested by practicing a lesson, rating cards with different confidence levels (Again/Hard/Good/Easy), and verifying that cards are scheduled for review at appropriate intervals.

**Acceptance Scenarios**:

1. **Given** a flashcard is being reviewed, **When** user marks it with a confidence rating (Again/Hard/Good/Easy), **Then** the system calculates the next review date based on spaced repetition algorithm
2. **Given** multiple lessons exist with different review schedules, **When** user opens the app, **Then** a banner suggests which lesson to study based on review schedule and progress
3. **Given** lessons have been practiced with varying confidence ratings, **When** viewing lesson detail, **Then** progress metrics show number of cards due for review, mastered cards, and learning cards
4. **Given** user practices a lesson, **When** cards are due for review based on their schedule, **Then** those cards are included in the practice session with higher priority

---

### User Story 4 - Test Knowledge with Mixed Quiz Formats (Priority: P4)

A user wants to test their vocabulary knowledge through mixed assessments combining multiple-choice and typing questions to validate learning.

**Why this priority**: This provides comprehensive assessment capability with flexibility for lessons of any size, but is secondary to the core practice functionality.

**Independent Test**: Can be tested by selecting a lesson, starting test mode, and answering a mix of multiple-choice questions (4 options) and typing questions (free text input), with immediate feedback on correctness.

**Acceptance Scenarios**:

1. **Given** a lesson with word pairs exists, **When** user taps "Test" button on lesson detail screen, **Then** quiz mode begins with mixed question formats
2. **Given** quiz mode is active, **When** a multiple-choice question is displayed, **Then** it shows an English term with 4 options (A/B/C/D) of Vietnamese meanings
3. **Given** a multiple-choice question is displayed, **When** options are generated, **Then** they include the correct answer plus 3 incorrect answers from other words in the lesson (shuffled)
4. **Given** quiz mode is active, **When** a typing question is displayed, **Then** it shows an English term with a text input field for user to type the Vietnamese meaning
5. **Given** user is answering a typing question, **When** user types their answer and submits, **Then** the system validates the answer against the correct Vietnamese meaning
6. **Given** a lesson has fewer than 4 words, **When** quiz mode starts, **Then** all questions are presented in typing format (no multiple-choice)
7. **Given** a lesson has 4 or more words, **When** quiz generates questions, **Then** it mixes both multiple-choice and typing questions in random order
8. **Given** user answers any question (multiple-choice or typing), **When** they submit, **Then** immediate feedback shows if correct or incorrect with the right answer displayed
9. **Given** all questions are answered, **When** quiz completes, **Then** a results summary shows overall score, breakdown by question type, and performance metrics

---

### User Story 5 - Customize App Settings (Priority: P5)

A user wants to personalize their learning experience by adjusting visual settings like flashcard colors and text size.

**Why this priority**: Customization improves user experience but is not essential for core functionality.

**Independent Test**: Can be tested by opening settings from the top-right corner, changing flashcard color and font size, and verifying changes are reflected in practice mode.

**Acceptance Scenarios**:

1. **Given** the app is open, **When** user taps the settings button in the top-right corner, **Then** settings screen is displayed
2. **Given** settings screen is open, **When** user changes flashcard color, **Then** the new color is applied to flashcards in practice mode
3. **Given** settings screen is open, **When** user adjusts text size, **Then** flashcard text displays at the selected size
4. **Given** settings are modified, **When** user closes settings, **Then** preferences are saved and persist across app sessions

---

### Edge Cases

- What happens when user tries to save a lesson without entering any word pairs?
- What happens when user swipes in practice mode but accidentally swipes in wrong direction?
- What happens when user starts practice on a lesson but exits mid-session - is progress saved?
- What happens when storage is full and user tries to save a new lesson?
- What happens when user tries to delete the last/only lesson in their library?
- What happens when user taps "Study" on a lesson where all cards are already mastered?
- What happens when user types an answer with minor spelling differences (accents, capitalization, extra spaces) in typing questions?
- What happens when user types a partially correct answer (synonyms or alternative translations)?
- What happens when user leaves a typing question blank and tries to submit?

## Requirements *(mandatory)*

### Functional Requirements

**Lesson Management**:
- **FR-001**: System MUST allow users to create lessons with title, description, and multiple word pairs
- **FR-002**: System MUST provide input fields for each word pair with English term and Vietnamese meaning
- **FR-003**: System MUST save lessons to local device storage
- **FR-004**: System MUST display all saved lessons in a list view
- **FR-005**: System MUST show empty state with "Add Lesson" button when no lessons exist
- **FR-006**: System MUST allow users to edit existing lessons (modify title, description, word pairs)
- **FR-007**: System MUST allow users to delete lessons with confirmation
- **FR-008**: System MUST allow users to add or remove word pairs within a lesson during editing

**Practice Mode**:
- **FR-009**: System MUST display flashcards showing either English or Vietnamese side initially
- **FR-010**: System MUST flip flashcard to reveal translation when user taps on it
- **FR-011**: System MUST advance to next flashcard when user swipes left (don't know)
- **FR-012**: System MUST advance to next flashcard when user swipes right (know)
- **FR-013**: System MUST support shuffled order for flashcards in practice mode
- **FR-014**: System MUST track which cards were marked as "know" vs "don't know" during session
- **FR-015**: System MUST display practice session summary upon completion

**Spaced Repetition**:
- **FR-016**: System MUST allow users to rate flashcard difficulty with four levels: Again, Hard, Good, Easy
- **FR-017**: System MUST calculate next review date for each card based on difficulty rating
- **FR-018**: System MUST track learning progress per lesson (cards due, learning, mastered)
- **FR-019**: System MUST display suggestion banner recommending which lesson to study based on due cards and progress
- **FR-020**: System MUST prioritize cards that are due for review in practice sessions

**Quiz Mode**:
- **FR-021**: System MUST generate multiple-choice questions with 4 options (A/B/C/D) when lesson has 4 or more words
- **FR-022**: System MUST generate typing questions (free text input) for all quiz modes
- **FR-023**: System MUST display English term as question with Vietnamese meanings as answer choices for multiple-choice
- **FR-024**: System MUST display English term as question with text input field for typing questions
- **FR-025**: System MUST shuffle answer options including correct answer and 3 incorrect options from same lesson for multiple-choice
- **FR-026**: System MUST validate typed answers against correct Vietnamese meaning with flexible matching (ignoring case, extra spaces)
- **FR-027**: System MUST use only typing format when lesson has fewer than 4 words
- **FR-028**: System MUST mix multiple-choice and typing questions randomly when lesson has 4 or more words
- **FR-029**: System MUST provide immediate feedback on answer correctness for both question types
- **FR-030**: System MUST display correct answer when user's response is incorrect
- **FR-031**: System MUST display quiz results summary showing overall score, breakdown by question type (multiple-choice vs typing), and performance metrics

**Lesson Detail View**:
- **FR-032**: System MUST display complete word list for selected lesson
- **FR-033**: System MUST show learning progress metrics (cards mastered, learning, due for review)
- **FR-034**: System MUST provide "Edit" button to modify lesson
- **FR-035**: System MUST provide "Study" button to start practice mode
- **FR-036**: System MUST provide "Test" button to start quiz mode

**Settings**:
- **FR-037**: System MUST display settings button in top-right corner of main screens
- **FR-038**: System MUST allow users to customize flashcard background color
- **FR-039**: System MUST allow users to adjust text size for flashcards
- **FR-040**: System MUST persist user preferences across app sessions

**Data Persistence**:
- **FR-041**: System MUST store all lesson data on local device storage
- **FR-042**: System MUST store user progress and review schedules locally
- **FR-043**: System MUST store user preferences locally
- **FR-044**: System MUST maintain data integrity when app is closed and reopened

### Key Entities

- **Lesson**: Represents a vocabulary learning unit with title, description, creation date, and collection of word pairs
- **Word Pair**: Individual vocabulary item containing English term and Vietnamese meaning/translation
- **Learning Progress**: Tracks user's mastery status for each word pair including difficulty rating, review count, next review date, and mastery level
- **Practice Session**: Records a learning session with timestamp, cards reviewed, confidence ratings, and completion status
- **User Preferences**: Stores customization settings including flashcard color scheme, text size, and display preferences
- **Quiz Result**: Records test performance including score, questions answered, correct/incorrect answers, and completion timestamp

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can create a new lesson with title, description, and at least 10 word pairs in under 5 minutes
- **SC-002**: Users can complete a practice session of 20 flashcards in under 3 minutes
- **SC-003**: System accurately tracks and displays learning progress showing percentage of mastered cards per lesson
- **SC-004**: 90% of users successfully create their first lesson and complete at least one practice session within first use
- **SC-005**: Spaced repetition algorithm schedules cards for review with intervals that increase based on mastery level (e.g., 1 day for "Hard", 3 days for "Good", 7 days for "Easy")
- **SC-006**: Users can locate and start studying recommended lessons within 2 taps from the home screen
- **SC-007**: Quiz results accurately reflect user knowledge with scores correlating to practice session performance
- **SC-008**: All user data (lessons, progress, settings) persists correctly with 100% data retention across app restarts
- **SC-009**: Flashcard visual customizations (color, text size) apply immediately and are visible in all practice modes
- **SC-010**: Users can navigate between lesson list, lesson details, practice, quiz, and settings within 1-2 taps each

## Assumptions

1. Users have basic familiarity with flashcard learning concepts
2. Android device has sufficient storage for reasonable number of lessons (assuming 100-1000 word pairs)
3. App operates entirely offline without requiring internet connection
4. Vietnamese language uses standard Unicode characters supported by Android
5. Users will primarily study one lesson at a time rather than mixing multiple lessons
6. Spaced repetition algorithm follows standard SM-2 or similar proven methodology with intervals: Again (1 day), Hard (3 days), Good (7 days), Easy (14 days)
7. Quiz mode adapts based on lesson size: typing-only format for lessons with fewer than 4 words, mixed multiple-choice and typing format for lessons with 4+ words
8. Typing question validation uses flexible matching: case-insensitive comparison, ignoring leading/trailing spaces, and accepting exact matches only (no synonym matching in initial version)
9. Mixed quiz format uses approximately 50/50 split between multiple-choice and typing questions when lesson has sufficient words
10. Empty state UI will guide new users to create their first lesson
11. Default flashcard color is neutral (white or light gray) with dark text for readability
12. Default text size is medium (16sp for Android) with options for small, medium, large, extra-large
13. Swipe gestures use standard Android swipe sensitivity thresholds
14. Practice session tracks both individual card performance and overall session statistics
15. Users can exit practice or quiz mode at any time without losing overall progress (but current session may not be saved)
16. Lesson deletion requires confirmation to prevent accidental data loss
