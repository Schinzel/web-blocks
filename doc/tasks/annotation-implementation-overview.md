# Annotation-Based Routing Implementation Overview

## Project Summary
Transform the web-blocks framework from interface-based routing to annotation-based routing while maintaining the sophisticated WebBlockResponse system and file-system based path discovery.

## Key Design Decisions
- **Annotation Naming**: Use `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi` (prefixed to avoid naming collisions)
- **Response System**: Maintain existing `WebBlockResponse` with `HtmlResponse`/`JsonResponse` (not raw strings)
- **Interface Unification**: Single `WebBlockRoute` interface instead of three separate interfaces
- **Path Generation**: Annotations mark type only, paths still come from file/directory structure
- **Backward Compatibility**: Support migration from existing interface-based approach

## Task Status and Navigation

| Task | Status | Description | Date Completed |
|------|--------|-------------|----------------|
| **[Phase 1: Annotations](annotation-phase1-annotations.md)** | To-Do | Create `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi` annotations | - |
| **[Phase 2: Interface](annotation-phase2-interface.md)** | To-Do | Create unified `WebBlockRoute` interface | - |
| **[Phase 3: Discovery](annotation-phase3-discovery.md)** | To-Do | Update route discovery system for annotations | - |
| **[Phase 4: Response](annotation-phase4-response.md)** | To-Do | Update response processing for annotation-based headers | - |
| **[Phase 5: Migration](annotation-phase5-migration.md)** | To-Do | Convert all sample routes to new annotation system | - |
| **[Phase 6: Testing](annotation-phase6-testing.md)** | To-Do | Create comprehensive test suite for annotation system | - |
| **[Phase 7: Documentation](annotation-phase7-documentation.md)** | To-Do | Update user documentation | - |

## Migration Example

### Before (Interface-based)
```kotlin
// In /pages/simple_page/ThePage.kt
class ThePage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
}

// In /api/UserPets.kt
class UserPets : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
}
```

### After (Annotation-based)
```kotlin
// In /pages/simple_page/ThePage.kt
@WebBlockPage
class ThePage : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
}

// In /api/UserPets.kt
@WebBlockApi
class UserPets : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
}
```

## Benefits of This Approach

### WebBlockRoute Interface Benefits
1. **Interface Unification**: Single `WebBlockRoute` interface instead of three separate interfaces

### Annotation System Benefits
1. **Future Extensibility**: Enables future annotation parameters like `@WebBlockPage(cacheable = true)`

## Notes & Decisions Log

- **2025-01-17**: Decided on WebBlock prefix for annotations to avoid naming collisions
- **2025-01-17**: Chose to maintain WebBlockResponse system instead of raw strings for better type safety
- **2025-01-17**: Created phased approach with 7 distinct tasks for better project management