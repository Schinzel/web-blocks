# Phase 1: Create Annotation Classes

**Status**: To-Do  
**Priority**: High  
**Dependencies**: None  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Create the three annotation classes `@WebBlockPage`, `@WebBlockApi`, and `@WebBlockPageApi` that will replace the current interface-based route type identification system.

## Background
The current system uses three separate interfaces (`IPageRoute`, `IApiRoute`, `IPageApiRoute`) to identify route types. The new system will use a single `WebBlockRoute` interface with annotations to mark the route type.

## Design Decisions
- **Naming**: Use `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi` (prefixed to avoid naming collisions)
- **Package**: Place in `io.schinzel.web_blocks.web.routes.annotations`
- **Target**: Classes only (`@Target(AnnotationTarget.CLASS)`)
- **Retention**: Runtime (`@Retention(AnnotationRetention.RUNTIME)`)

## Implementation Requirements

### 1. Create Annotation Package
Create package: `io.schinzel.web_blocks.web.routes.annotations`

### 2. Create @WebBlockPage Annotation
```kotlin
package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlocks page route
 * that returns HTML content for web pages.
 * 
 * Page routes are discovered from the /pages directory structure and generate
 * HTML responses with Content-Type: text/html.
 * 
 * Path generation follows file system structure:
 * - /pages/simple_page/ThePage.kt → /simple-page
 * - /pages/landing/LandingPage.kt → / (root)
 * - snake_case directories become kebab-case URLs
 * 
 * Example:
 * @WebBlockPage
 * class ThePage : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
 * }
 * 
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockPage
```

### 3. Create @WebBlockApi Annotation
```kotlin
package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlocks API route
 * that returns JSON content for standalone API endpoints.
 * 
 * API routes are discovered from the /api directory structure and generate
 * JSON responses with Content-Type: application/json.
 * 
 * Path generation follows file system structure with class name:
 * - /api/UserPets.kt → /api/user-pets
 * - /api/user_management/UserInfo.kt → /api/user-management/user-info
 * - PascalCase class names become kebab-case, "Route" suffix removed
 * 
 * Example:
 * @WebBlockApi
 * class UserPets : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
 * }
 * 
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockApi
```

### 4. Create @WebBlockPageApi Annotation
```kotlin
package io.schinzel.web_blocks.web.routes.annotations

/**
 * The purpose of this annotation is to mark a class as a WebBlocks page API route
 * that returns JSON content for page-specific API endpoints.
 * 
 * Page API routes are discovered from the /pages directory structure and generate
 * JSON responses with Content-Type: application/json. These are typically used
 * for saving form data, updating page elements, or other page-specific operations.
 * 
 * Path generation follows file system structure with class name:
 * - /pages/settings/SaveNameRoute.kt → /page-api/settings/save-name
 * - /pages/user_profile/UpdateEmailRoute.kt → /page-api/user-profile/update-email
 * - PascalCase class names become kebab-case, "Route" suffix removed
 * 
 * Example:
 * @WebBlockPageApi
 * class SaveNameRoute : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(mapOf("success" to true))
 * }
 * 
 * Written by Claude Sonnet 4
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("unused")
annotation class WebBlockPageApi
```

## File Structure
```
src/main/kotlin/io/schinzel/web_blocks/web/routes/annotations/
├── WebBlockPage.kt
├── WebBlockApi.kt
└── WebBlockPageApi.kt
```

## Acceptance Criteria
- [ ] Three annotation classes created with proper documentation
- [ ] Annotations use correct Target and Retention settings
- [ ] Package structure follows WebBlocks conventions
- [ ] Documentation includes examples and path generation rules
- [ ] All annotations follow code standards (comments, naming, etc.)
- [ ] Classes are under 100 lines each
- [ ] Comprehensive documentation explains purpose and usage

## Testing Requirements
- [ ] Unit tests verify annotation metadata (Target, Retention)
- [ ] Tests confirm annotations can be applied to classes
- [ ] Tests verify annotations are discoverable at runtime
- [ ] Tests ensure annotations cannot be applied to methods/fields

## Future Extensibility
These annotations are designed to support future parameters:
```kotlin
@WebBlockPage(cacheable = true, cacheDuration = "1h")
@WebBlockApi(authRequired = true, roles = ["ADMIN"])
@WebBlockPageApi(rateLimit = 100)
```

## JVM Language Compatibility
- Annotations work identically across Kotlin, Java, Scala, and Clojure
- No Kotlin-specific features used
- Runtime retention ensures reflection works in all JVM languages

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Include "The purpose of this annotation is to..." documentation
- Use US English in documentation
- Include "Written by Claude Sonnet 4" in class documentation
- Comprehensive comments explaining path generation rules

## Dependencies
- None - annotations are self-contained
- Will be used by Phase 2 (WebBlockRoute interface)
- Will be used by Phase 3 (route discovery system)

## Notes
- WebBlock prefix chosen to avoid naming collisions with other libraries
- Runtime retention required for reflection-based route discovery
- Class-only targeting prevents misuse on methods or fields
- Documentation includes specific examples of path generation for clarity