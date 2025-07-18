# Annotation-Based Routing Implementation Overview

## Project Summary
Transform the web-blocks framework from interface-based routing to annotation-based routing while maintaining the sophisticated WebBlockResponse system and file-system based path discovery.

## Key Design Decisions
- **Annotation Naming**: Use `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi` (prefixed to avoid naming collisions)
- **Response System**: Maintain existing `WebBlockResponse` with `HtmlResponse`/`JsonResponse` (not raw strings)
- **Interface Unification**: Single `WebBlockRoute` interface instead of three separate interfaces
- **Path Generation**: Annotations mark type only, paths still come from file/directory structure
- **Path Collision Prevention**: `@WebBlockPageApi` uses `/page-api/` prefix to avoid conflicts with `/api/` routes
- **Block Architecture**: Pages contain blocks (components), some blocks need API endpoints for operations
- **Backward Compatibility**: Support migration from existing interface-based approach

## Task Status and Navigation

| Task | Status | Description | Date Completed |
|------|--------|-------------|----------------|
| **[Phase 1: Annotations](annotation-phase1-annotations.md)** | ✅ Complete | Create `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi` annotations | 2025-07-18 |
| **[Phase 2: Interface](annotation-phase2-interface.md)** | ✅ Complete | Create unified `WebBlockRoute` interface | 2025-07-18 |
| **[Phase 3: Discovery](annotation-phase3-discovery.md)** | ✅ Complete | Update route discovery system for annotations | 2025-07-18 |
| **[Phase 4: Response](annotation-phase4-response.md)** | ⏭️ Skipped (Premature) | Update response processing for annotation-based headers | 2025-07-18 |
| **[Phase 5: Migration](annotation-phase5-migration.md)** | ✅ Complete | Convert all sample routes to new annotation system | 2025-07-18 |
| **[Phase 6: Testing](annotation-phase6-testing.md)** | ✅ Complete | Create comprehensive test suite for annotation system | 2025-07-18 |
| **[Phase 7: Documentation](annotation-phase7-documentation.md)** | ✅ Complete | Update user documentation | 2025-07-18 |

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

// In /pages/user_profile/SaveNameRoute.kt - Block API for name update form
@WebBlockPageApi
class SaveNameRoute : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(mapOf("success" to true))
}
```

## Benefits of This Approach

### WebBlockRoute Interface Benefits
1. **Interface Unification**: Single `WebBlockRoute` interface instead of three separate interfaces

### Annotation System Benefits
1. **Future Extensibility**: Enables future annotation parameters like `@WebBlockPage(cacheable = true)`
2. **Clear Route Types**: Distinguishes between pages, standalone APIs, and block APIs
3. **Path Collision Prevention**: Separate namespaces prevent conflicts between API types
4. **Block Architecture**: Supports component-based page development with block-specific APIs

## Phase Completion Guidelines

When completing each phase, update the documentation as follows:

### 1. Update Overview Document (`annotation-implementation-overview.md`)
- Change status from "To-Do" to "✅ Complete" in the Task Status table
- Add completion date in YYYY-MM-DD format
- Update any affected sections or examples

### 2. Update Individual Phase Documents
- Change status from "To-Do" to "✅ Complete" at the top of the document
- Add completion date and brief completion summary
- Check all boxes under "Acceptance Criteria" section (✅)
- Check all boxes under "Testing Requirements" section (✅)
- Add any notes about implementation decisions or deviations

### 3. Code Standards Compliance
- Read the [user doc](../user_doc/0_index.md) to understand the structure of the framework
- Ensure all code follows standards in [code standards](../code_standards/_index.md)`
- Run ktlint to verify formatting compliance
- Update any related tests or documentation
- Verify all acceptance criteria are met before marking complete

### 4. Documentation Quality
- Maintain consistent formatting across all phase documents
- Include specific examples and code snippets where helpful
- Update cross-references between documents as needed
- Keep the overview document as the single source of truth for project status

