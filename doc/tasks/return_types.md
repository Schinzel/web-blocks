# Task: Implement WebBlockResponse Return Type System

## Overview
Transform the web-blocks framework from mixed return types to a unified WebBlockResponse system while maintaining all existing functionality and developer experience.

## Current State
The framework currently uses inconsistent return types across route interfaces:
- `IPageRoute` - returns `String` (compile-time type safety)
- `IApiRoute` - returns `Any` (runtime flexibility with automatic JSON serialization)
- `IPageApiRoute` - returns `Any` (runtime flexibility with automatic JSON serialization)

Response processing is handled by:
- `SendResponse.kt` - processes responses based on `ReturnTypeEnum` (HTML vs JSON)
- Automatic JSON wrapping for API routes via `ApiResponse.Success(message = response)`
- Automatic serialization of data classes/objects to JSON

## Desired State
Replace the mixed return types with:
1. A unified `WebBlockResponse` sealed interface
2. Specific response types: `HtmlResponse`, `JsonResponse`
3. Maintain all existing functionality including automatic JSON serialization

### Example of New Approach

```kotlin
// Sealed interface for all responses
sealed interface WebBlockResponse {
    val status: Int get() = 200
    val headers: Map<String, String> get() = emptyMap()
}

data class HtmlResponse(
    val content: String,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse

data class JsonResponse(
    val data: Any, // Preserves automatic serialization
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse

// Usage examples:
// In /pages/simple_page/ThePage.kt
class ThePage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse = 
        HtmlResponse("<h1>Hello!</h1>")
}

// In /api/UserPets.kt  
class UserPets : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse = 
        JsonResponse(listOf(Pet("Fluffy"), Pet("Rex"))) // Auto-serialized to JSON
}

// In /pages/settings/SaveNameRoute.kt
class SaveNameRoute : IPageApiRoute {
    override suspend fun getResponse(): WebBlockResponse = 
        JsonResponse(mapOf("success" to true, "message" to "Name saved"))
}
```

## Status Code Ownership Model

### Overview
Clear understanding of who controls HTTP status codes is critical for the framework design. This section defines the responsibility model between the framework and implementing classes.

### The Contract
**When a route successfully executes, the implementing class has FINAL authority over the status code.**

The framework:
- Reads the status from `WebBlockResponse.status`
- Applies it directly to the HTTP response
- NEVER modifies or overrides the status from a successfully returned response

### Status Code Control Matrix

| Scenario | Who Controls | Status Code | Example |
|----------|-------------|-------------|---------|
| Route not found | Framework | 404 | `GET /does-not-exist` |
| Uncaught exception in route | Framework | 500 | Route throws `RuntimeException` |
| Parameter parsing fails | Framework | 400 | Invalid query parameter format |
| Request body invalid | Framework | 400 | Malformed JSON in POST body |
| Route returns response | **Implementing Class** | **Any** | `JsonResponse(data, status = 201)` |

### Examples

```kotlin
// Framework controls - Route not found
GET /api/non-existent-endpoint → 404 (set by framework)

// Framework controls - Route throws exception
class BrokenRoute : IApiRoute {
    override fun getResponse(): WebBlockResponse = 
        throw RuntimeException("Database connection failed")
}
// → 500 (framework catches and sets)

// Implementing class controls - Explicit status
class UserRoute : IApiRoute {
    override fun getResponse(): WebBlockResponse = 
        JsonResponse(
            data = mapOf("error" to "User not found"),
            status = 404  // Route chooses 404, framework respects it
        )
}
// → 404 (route's explicit choice)

// Implementing class controls - Default status
class HealthRoute : IApiRoute {
    override fun getResponse(): WebBlockResponse = 
        JsonResponse(mapOf("status" to "healthy"))  // Defaults to 200
}
// → 200 (route's implicit choice via default)
```

### Mental Model
- **Framework handles infrastructure errors**: Route resolution, request parsing, uncaught exceptions
- **Routes handle business logic responses**: User not found, validation errors, successful operations

This separation ensures routes have full control over their business logic responses while the framework handles infrastructure concerns consistently.

## Implementation Requirements

### 1. Create WebBlockResponse Interface
```kotlin
/**
 * The purpose of this interface is to define the contract for all web route responses
 * in the framework, providing type safety and consistent response handling.
 */
sealed interface WebBlockResponse {
    val status: Int get() = 200
    val headers: Map<String, String> get() = emptyMap()
}

/**
 * The purpose of this class is to represent HTML responses from page routes.
 */
data class HtmlResponse(
    val content: String,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse

/**
 * The purpose of this class is to represent JSON responses from API routes,
 * preserving automatic serialization of data objects.
 */
data class JsonResponse(
    val data: Any,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse
```

### 2. Update Existing Interfaces
Modify all three interfaces to return `WebBlockResponse`:
- `IPageRoute.getResponse(): WebBlockResponse`
- `IApiRoute.getResponse(): WebBlockResponse`
- `IPageApiRoute.getResponse(): WebBlockResponse`

### 3. Update Response Processing
Modify `SendResponse.kt` to handle `WebBlockResponse` objects:
- Process `HtmlResponse` → set `Content-Type: text/html`, write content
- Process `JsonResponse` → maintain existing `ApiResponse.Success` wrapping, serialize to JSON
- Handle status codes and custom headers from response objects
- Set appropriate Content-Type headers based on response type

### 4. Update All Sample Implementations
Migrate all sample routes to use new response types:
- Page routes: return `HtmlResponse(content)`
- API routes: return `JsonResponse(data)` where data is the object/list to serialize
- Page API routes: return `JsonResponse(data)`

### 5. Add Convenience Functions
To improve developer experience and maintain "low barrier to entry", add convenience functions:

```kotlin
/**
 * The purpose of these functions is to provide a concise, readable way
 * to create response objects with minimal boilerplate.
 */
// Top-level convenience functions
fun html(content: String, status: Int = 200, headers: Map<String, String> = emptyMap()) = 
    HtmlResponse(content, status, headers)

fun json(data: Any, status: Int = 200, headers: Map<String, String> = emptyMap()) = 
    JsonResponse(data, status, headers)

// Usage examples - Clean and intuitive
class SimplePage : IPageRoute {
    override fun getResponse() = html("<h1>Welcome!</h1>")
}

class UserApi : IApiRoute {
    override fun getResponse() = json(User("John", "Doe"))
}

class CreateUserApi : IApiRoute {
    override fun getResponse() = json(
        data = CreatedUser(id = 123),
        status = 201  // Created
    )
}
```

## Key Constraints
1. **Preserve automatic JSON serialization** - `JsonResponse(data)` must maintain current behavior
2. **Maintain API response wrapping** - JSON responses should still be wrapped in `ApiResponse.Success`
3. **No breaking changes to URLs** - All existing paths and routing rules remain unchanged
4. **Preserve developer experience** - API routes can still return data classes/objects for auto-serialization

## Migration Examples

### Before (Mixed Return Types)
```kotlin
// Page route
class ThePage : IPageRoute {
    override suspend fun getResponse(): String = "<h1>Hello!</h1>"
}

// API route
class UserPets : IApiRoute {
    override suspend fun getResponse(): Any = listOf(Pet("Fluffy"), Pet("Rex"))
}

// Page API route
class SaveName : IPageApiRoute {
    override suspend fun getResponse(): Any = "Name saved successfully"
}
```

### After (WebBlockResponse)
```kotlin
// Page route
class ThePage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse = 
        HtmlResponse("<h1>Hello!</h1>")
}

// API route
class UserPets : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse = 
        JsonResponse(listOf(Pet("Fluffy"), Pet("Rex")))
}

// Page API route
class SaveName : IPageApiRoute {
    override suspend fun getResponse(): WebBlockResponse = 
        JsonResponse("Name saved successfully")
}
```

## Testing Requirements
1. Unit tests for `WebBlockResponse` types
2. Unit tests for response processing with new types
3. Integration tests ensuring all sample routes work correctly
4. Verify Content-Type headers are set correctly
5. Verify JSON serialization and wrapping behavior is preserved
6. Verify status codes and custom headers work properly

## Benefits
1. **Unified Response Model**: All routes use the same response contract
2. **Enhanced Type Safety**: Compile-time guarantees about response types
3. **Better Developer Experience**: Clear, explicit response objects with IDE support
4. **Extensibility**: Easy to add new response types in the future
5. **Status Code Support**: Built-in support for custom status codes and headers
6. **Maintained Functionality**: All existing automatic behaviors preserved

## Success Criteria
1. All existing sample routes work with new response types
2. No changes to URL generation or routing behavior
3. JSON serialization and wrapping behavior preserved
4. Content-Type headers set correctly for all response types
5. Status codes and custom headers work properly
6. No performance regressions
7. Clear migration path documented
8. Comprehensive test coverage

## Future Extensibility
This approach enables future additions like:
```kotlin
data class RedirectResponse(
    val location: String,
    override val status: Int = 302
) : WebBlockResponse

data class BinaryResponse(
    val data: ByteArray,
    val contentType: String,
    override val status: Int = 200
) : WebBlockResponse

data class ErrorResponse(
    val message: String,
    val errorCode: String? = null,
    override val status: Int = 500
) : WebBlockResponse
```

## Phase 2 Considerations: Annotation-Based Routing

When implementing Phase 2 (annotation-based routing with `@Page`, `@Api`, `@PageApi`), there are important considerations for JVM language compatibility:

### Annotation Processing Options
1. **Runtime Annotation Scanning (Reflection)**
   - ✅ **JVM Language Agnostic**: Works with Java, Scala, Clojure, and other JVM languages
   - ✅ **Hot Reloading Compatible**: Changes are detected immediately during development
   - ⚠️ **Performance**: Slight startup cost for classpath scanning (mitigated with caching)
   - ✅ **Implementation Simplicity**: Straightforward to implement and maintain

2. **Compile-time Processing (KSP)**
   - ❌ **Kotlin-Only**: KSP (Kotlin Symbol Processing) only works with Kotlin code
   - ❌ **JVM Language Compatibility**: Java, Scala, and other JVM language users would be excluded
   - ✅ **Performance**: Faster runtime startup, compile-time validation
   - ⚠️ **Hot Reloading**: More complex development workflow, requires recompilation

### Framework Design Principle Impact
Given the framework's core principle of supporting **"any JVM language - Kotlin, Java, Scala, Clojure, and more"**, KSP would fundamentally violate this design goal by making the annotation system Kotlin-exclusive.

### Recommendation for Phase 2
**Use Runtime Annotation Scanning** to maintain the framework's JVM language agnostic design:
- Preserves compatibility with Java, Scala, Clojure, and other JVM languages
- Maintains the "low barrier to entry" principle for developers using any JVM language
- Supports hot reloading for excellent development experience
- Performance impact is minimal with proper caching implementation

This ensures the framework remains true to its founding principle of JVM language inclusivity while still providing modern annotation-based routing capabilities.

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Ensure classes are under 250 lines
- Functions under 10 lines
- Comprehensive documentation with "The purpose of this [class/interface/...] is to [...]"
- Extensive unit tests following testing standards
- Add "Written by Claude Sonnet 4" to AI-generated code

## Migration Strategy
1. **Create WebBlockResponse interfaces** first
2. **Update existing interfaces** to return WebBlockResponse
3. **Update response processing** in SendResponse.kt
4. **Migrate sample implementations** one by one
5. **Add comprehensive tests** at each step
6. **Update documentation** with new patterns
7. **Validate no regressions** before completion

## Notes
- This is Phase 1 of a larger refactoring effort
- Phase 2 will add annotation-based routing (@Page, @Api, @PageApi)
- Focus on maintaining backward compatibility and existing functionality
- Ensure hot reloading continues to work in development mode