# Task: Implement Annotation-Based Routing System

## Overview
Transform the web-blocks framework from interface-based routing to annotation-based routing while maintaining file-system based path discovery.

## Current State
The framework currently uses three separate interfaces for different route types:
- `IPageResponseHandler` - for page routes returning HTML
- `IApiRoute` - for API routes returning JSON
- `IPageApiRoute` - for page API routes returning JSON

Routes are discovered through:
- File system structure (`pages/`, `api/` directories)
- Package/directory names (snake_case → kebab-case conversion)
- Class names for APIs (PascalCase → kebab-case, removing "Route" suffix)

## Desired State
Replace the three interfaces with:
1. A single common interface `WebBlockRoute`
2. Three annotations to mark route types: `@Page`, `@Api`, `@PageApi`

### Example of New Approach

```kotlin
// Common interface for all routes
interface WebBlockRoute {
    fun getResponse(): String
}

// In /pages/simple_page/ThePage.kt
@Page  // Just marks type, path comes from file location → /simple-page
class ThePage : WebBlockRoute {
    override fun getResponse(): String = "<h1>Hello!</h1>"
}

// In /api/UserPets.kt  
@Api  // Marks as API route, path → /api/user-pets
class UserPets : WebBlockRoute {
    override fun getResponse(): String = """{"pets": ["cat", "dog"]}"""
}

// In /pages/settings/SaveNameRoute.kt
@PageApi  // Marks as page API route, path → /page-api/settings/save-name
class SaveNameRoute : WebBlockRoute {
    override fun getResponse(): String = """{"success": true}"""
}
```

## Implementation Requirements

### 1. Create Annotations
Create three annotation classes:
```kotlin
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Page

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PageApi
```

### 2. Create Common Interface
```kotlin
/**
 * The purpose of this interface is to define the contract for all web routes
 * in the framework, regardless of whether they return HTML or JSON.
 */
interface WebBlockRoute {
    /**
     * Returns the response content as a String.
     * For @Page routes, this should return HTML.
     * For @Api and @PageApi routes, this should return JSON.
     */
    fun getResponse(): String
}
```

### 3. Update Route Discovery
Modify the route discovery mechanism to:
1. Scan for classes annotated with `@Page`, `@Api`, or `@PageApi`
2. Validate that annotated classes implement `WebBlockRoute`
3. Continue using file system structure for path determination:
   - `/pages/` directory for `@Page` and `@PageApi` routes
   - `/api/` directory for `@Api` routes
   - Maintain existing path conversion rules (snake_case → kebab-case)

### 4. Update Response Handling
Based on the annotation type, set appropriate response headers:
- `@Page` → `Content-Type: text/html`
- `@Api` → `Content-Type: application/json`
- `@PageApi` → `Content-Type: application/json`

### 5. Maintain Backward Compatibility (Optional)
Consider whether to:
- Deprecate the old interfaces gradually
- Support both approaches temporarily
- Provide migration utilities

## Key Constraints
1. **File system routing must be preserved** - Annotations do NOT define paths
2. **All existing routing rules remain unchanged**:
   - Directory structure determines URL path
   - snake_case → kebab-case conversion
   - Special handling for `landing` directory → root path
   - Prefix rules: `/api/` for API routes, `/page-api/` for page API routes
3. **Parameter handling remains the same** - Query parameters and request body as constructor arguments

## Migration Examples

### Before (Interface-based)
```kotlin
// In /pages/simple_page/ThePage.kt
class ThePage : IPageResponseHandler {
    override fun getResponse(): String = "<h1>Hello!</h1>"
}

// In /api/UserPets.kt
class UserPets : IApiRoute {
    override fun getResponse(): String = """{"pets": ["cat", "dog"]}"""
}
```

### After (Annotation-based)
```kotlin
// In /pages/simple_page/ThePage.kt
@Page
class ThePage : WebBlockRoute {
    override fun getResponse(): String = "<h1>Hello!</h1>"
}

// In /api/UserPets.kt
@Api
class UserPets : WebBlockRoute {
    override fun getResponse(): String = """{"pets": ["cat", "dog"]}"""
}
```

## Testing Requirements
1. Create unit tests for annotation discovery
2. Ensure all existing routes work with new approach
3. Test that missing `WebBlockRoute` implementation fails at compile time
4. Test that incorrect annotation usage is caught at startup
5. Verify Content-Type headers are set correctly

## Future Extensibility
This approach enables future additions like:
```kotlin
@Api(produces = "application/xml")
@Page(cacheable = true, cacheDuration = "1h")
@PageApi(authRequired = true, roles = ["ADMIN"])
```

## Success Criteria
1. All existing sample routes work with annotations
2. Compile-time safety is maintained via `WebBlockRoute` interface
3. Runtime route discovery correctly identifies annotated classes
4. No changes to URL path generation logic
5. Clear error messages for misconfigured routes

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Ensure classes are under 250 lines
- Functions under 10 lines
- Comprehensive documentation
- Extensive unit tests following testing standards

## Notes
- Start with a proof of concept in the sample application
- Consider performance implications of annotation scanning
- Ensure hot reloading continues to work in development mode