# English Flashcard App Constitution

## Core Principles

### I. Clean Code (NON-NEGOTIABLE)
**Code must be clean, readable, and maintainable at all times.**

Rules:
- Functions must be small and do ONE thing well
- Meaningful names for variables, functions, classes (no abbreviations)
- No magic numbers - use named constants
- Maximum function length: 30 lines (except Composables with UI layout)
- Maximum class size: 200 lines (split into smaller classes if exceeded)
- Comments explain WHY, not WHAT (code should be self-documenting)
- Delete dead code immediately - no commented-out code

Rationale: Clean code reduces bugs, speeds up onboarding, and makes maintenance easier. Code is read 10x more than written.

### II. Loose Coupling (NON-NEGOTIABLE)
**Screens and functions must be independent and reusable.**

Rules:
- ViewModels must NOT know about other ViewModels
- Composables must receive data via parameters, not direct repository/ViewModel access
- Use interfaces/contracts instead of concrete implementations
- Dependency Injection for all dependencies (use constructor injection)
- Each screen can be tested in isolation
- No circular dependencies between modules/packages

Rationale: Loose coupling enables parallel development, easier testing, and independent feature deployment.

### III. Simple UI First
**Focus on core functionality with simple, effective UI using Jetpack Compose + Material3.**

Rules:
- Use Material3 components by default (Button, Card, TextField, etc.)
- Custom components only when Material3 doesn't fit
- Maximum 3 colors in color scheme (primary, secondary, background)
- Consistent spacing using predefined dimensions (4dp, 8dp, 16dp, 24dp, 32dp)
- Focus on most important information - hide secondary details in expandable sections
- Mobile-first design - optimize for phone screens first

Rationale: Simple UI is easier to maintain, performs better, and provides better UX. Material3 provides accessibility and consistency for free.

### IV. Smooth Animations Required
**All transitions and interactions must have smooth, meaningful animations.**

Rules:
- Screen transitions must use Compose Navigation animations
- Flashcard flip must use AnimatedContent with 3D rotation effect
- Swipe gestures must have spring-based physics animations
- Loading states must show progress indicators or skeleton screens
- List additions/removals must animate with fadeIn/fadeOut + slideIn/slideOut
- Target: 60fps for all animations (no janky scrolling or transitions)

Rationale: Animations provide visual feedback, guide user attention, and create delightful experiences. They're essential for learning apps where engagement matters.

### V. Code Quality Gates
**All code must pass quality checks before considered complete.**

Gates:
1. **Compilation**: Code must compile without errors
2. **Lint**: Zero lint errors (warnings should be addressed or suppressed with justification)
3. **Format**: Code must follow Kotlin style guide (use ktlint or IntelliJ formatter)
4. **Review**: Self-review changes before marking task complete
5. **Test**: Core business logic (use cases, repositories) must have unit tests

Process:
- After completing a task: Run lint → Format code → Review changes → Fix issues
- Before commit: Ensure all quality gates pass
- Use pre-commit hooks to enforce formatting

Rationale: Quality gates catch bugs early, maintain consistency, and ensure production-ready code.

## Architecture Standards

### Clean Architecture Layers
```
UI Layer (Composables, ViewModels)
  ↓ depends on ↓
Domain Layer (Use Cases, Domain Models, Repository Interfaces)
  ↓ depends on ↓
Data Layer (Repository Implementations, DAOs, Entities, DataStore)
```

Rules:
- Dependencies flow downward only (UI → Domain → Data)
- Domain layer has NO Android dependencies (pure Kotlin)
- Data layer returns domain models, not entities
- UI layer observes StateFlow/Flow, never calls suspend functions directly in Composables

### Testing Philosophy
- **Unit Tests**: Domain layer use cases (SM-2 algorithm, quiz generation, validation)
- **Integration Tests**: Repository implementations with in-memory database
- **UI Tests**: Optional - add for critical flows only if time permits

Tests are OPTIONAL for MVP but REQUIRED for core business logic (spaced repetition algorithm).

## Development Workflow

### Task Completion Checklist
For each task marked complete:
- [ ] Code compiles without errors
- [ ] Run `./gradlew lintDebug` - fix all errors
- [ ] Run `./gradlew ktlintFormat` (or use IDE formatter)
- [ ] Self-review: Read your own code as if reviewing someone else's PR
- [ ] If it's a use case or repository: Write unit test
- [ ] Commit with meaningful message: `feat(US1): implement lesson list screen`

### Commit Message Format
```
<type>(<scope>): <description>

Types: feat, fix, refactor, docs, test, chore
Scope: US1, US2, US3, US4, US5, foundation, polish
Example: feat(US1): add lesson creation form with validation
```

## Governance

**This constitution supersedes all other coding practices.**

- All pull requests must comply with these principles
- Violations must be justified in code review or immediately fixed
- Constitution can be amended with project consensus
- Use `quickstart.md` for runtime development guidance

**Version**: 1.0.0 | **Ratified**: 2026-02-13 | **Last Amended**: 2026-02-13
