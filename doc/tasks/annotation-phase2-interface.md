# Phase 2: Create WebBlockRoute Interface

**Status**: To-Do  
**Priority**: High  
**Dependencies**: Phase 1 (Annotations)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Create a unified `WebBlockRoute` interface that will replace the current three separate interfaces (`IPageRoute`, `IApiRoute`, `IPageApiRoute`) while maintaining the sophisticated WebBlockResponse system.

## Background
Currently, the framework uses three separate interfaces to identify route types:
- `IPageRoute` - for HTML pages
- `IApiRoute` - for standalone API endpoints  
- `IPageApiRoute` - for page-specific API endpoints

The new system will use a single interface with annotations to mark the route type.

## Design Decisions
- **Single Interface**: One `WebBlockRoute` interface instead of three
- **Signature**: `suspend fun getResponse(): WebBlockResponse` (maintains async support)
- **Path Resolution**: Include path resolution logic from existing `IRoute.getPath()`
- **Annotation Detection**: Interface uses annotation reflection to determine route type
- **Type Safety**: Maintains existing WebBlockResponse type system

## Implementation Requirements

### 1. Create WebBlockRoute Interface
```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import kotlin.reflect.KClass

/**
 * The purpose of this interface is to define the contract for all web routes
 * in the WebBlocks framework, regardless of whether they return HTML or JSON.
 * 
 * This unified interface replaces the separate IPageRoute, IApiRoute, and IPageApiRoute
 * interfaces. Route type is now determined by annotations (@WebBlockPage, @WebBlockApi, 
 * @WebBlockPageApi) rather than interface inheritance.
 * 
 * Route path generation follows file system structure:
 * - @WebBlockPage: /pages/simple_page/ThePage.kt → /simple-page
 * - @WebBlockApi: /api/UserPets.kt → /api/user-pets  
 * - @WebBlockPageApi: /pages/settings/SaveNameRoute.kt → /page-api/settings/save-name
 * 
 * Example usage:
 * ```kotlin
 * @WebBlockPage
 * class ThePage : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
 * }
 * 
 * @WebBlockApi
 * class UserPets : WebBlockRoute {
 *     override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
 * }
 * ```
 * 
 * Written by Claude Sonnet 4
 */
interface WebBlockRoute {
    /**
     * The purpose of this function is to generate the response content for this route.
     * 
     * The response type (HTML or JSON) and Content-Type headers are determined by
     * the route's annotation:
     * - @WebBlockPage → HtmlResponse with text/html Content-Type
     * - @WebBlockApi → JsonResponse with application/json Content-Type
     * - @WebBlockPageApi → JsonResponse with application/json Content-Type
     * 
     * @return WebBlockResponse containing the route's response data, status code, and headers
     */
    suspend fun getResponse(): WebBlockResponse

    /**
     * The purpose of this function is to get the URL path for this route
     * based on its file system location and annotation type.
     * 
     * Path generation rules:
     * - @WebBlockPage: Uses directory structure from /pages
     * - @WebBlockApi: Uses directory structure from /api + class name
     * - @WebBlockPageApi: Uses directory structure from /pages + class name with /page-api prefix
     * 
     * @return String representing the URL path for this route
     */
    fun getPath(): String {
        return RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
    }
}
```

### 2. Create Route Type Detection Utility
```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import kotlin.reflect.KClass

/**
 * The purpose of this object is to provide utilities for detecting and validating
 * WebBlock route annotations on classes.
 * 
 * Written by Claude Sonnet 4
 */
object RouteAnnotationUtil {
    
    /**
     * The purpose of this function is to detect which WebBlock route annotation
     * is present on a given class.
     * 
     * @param clazz The class to check for route annotations
     * @return The route annotation type, or null if no valid annotation found
     * @throws IllegalArgumentException if multiple route annotations are present
     */
    fun detectRouteType(clazz: KClass<*>): RouteType? {
        val hasPage = clazz.annotations.any { it is WebBlockPage }
        val hasApi = clazz.annotations.any { it is WebBlockApi }
        val hasPageApi = clazz.annotations.any { it is WebBlockPageApi }
        
        val annotationCount = listOf(hasPage, hasApi, hasPageApi).count { it }
        
        return when {
            annotationCount == 0 -> null
            annotationCount > 1 -> throw IllegalArgumentException(
                "Class ${clazz.simpleName} has multiple route annotations. " +
                "Only one of @WebBlockPage, @WebBlockApi, or @WebBlockPageApi is allowed."
            )
            hasPage -> RouteType.PAGE
            hasApi -> RouteType.API
            hasPageApi -> RouteType.PAGE_API
            else -> null
        }
    }
    
    /**
     * The purpose of this function is to validate that a class implementing 
     * WebBlockRoute has exactly one valid route annotation.
     * 
     * @param clazz The class to validate
     * @throws IllegalArgumentException if validation fails
     */
    fun validateRouteAnnotation(clazz: KClass<out WebBlockRoute>) {
        val routeType = detectRouteType(clazz)
        
        if (routeType == null) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} implements WebBlockRoute but has no route annotation. " +
                "Add @WebBlockPage, @WebBlockApi, or @WebBlockPageApi annotation."
            )
        }
    }
}

/**
 * The purpose of this enum is to represent the different types of WebBlock routes.
 * 
 * Written by Claude Sonnet 4
 */
enum class RouteType {
    PAGE,
    API,
    PAGE_API
}
```

### 3. Update ReturnTypeEnum
```kotlin
package io.schinzel.web_blocks.web.routes

/**
 * The purpose of this enum is to represent the different return types
 * that WebBlock routes can produce.
 * 
 * Written by Claude Sonnet 4
 */
enum class ReturnTypeEnum {
    HTML,
    JSON;
    
    companion object {
        /**
         * The purpose of this function is to get the return type based on route annotation.
         * 
         * @param routeType The route type from annotation detection
         * @return The corresponding return type
         */
        fun fromRouteType(routeType: RouteType): ReturnTypeEnum {
            return when (routeType) {
                RouteType.PAGE -> HTML
                RouteType.API -> JSON
                RouteType.PAGE_API -> JSON
            }
        }
        
        /**
         * The purpose of this function is to get the Content-Type header value
         * for this return type.
         * 
         * @return String representing the Content-Type header value
         */
        fun getContentType(): String {
            return when (this) {
                HTML -> "text/html"
                JSON -> "application/json"
            }
        }
    }
}
```

## File Structure
```
src/main/kotlin/io/schinzel/web_blocks/web/routes/
├── WebBlockRoute.kt
├── RouteAnnotationUtil.kt
└── ReturnTypeEnum.kt (updated)
```

## Acceptance Criteria
- [ ] `WebBlockRoute` interface created with proper documentation
- [ ] Interface signature matches existing `IRoute` but unified
- [ ] Path resolution logic included from existing `IRoute.getPath()`
- [ ] Route type detection utility created
- [ ] Validation logic for route annotations
- [ ] ReturnTypeEnum updated to work with new annotation system
- [ ] All code follows WebBlocks coding standards
- [ ] Classes are under 250 lines each
- [ ] Functions are under 10 lines each
- [ ] Comprehensive documentation with examples

## Testing Requirements
- [ ] Unit tests for `RouteAnnotationUtil.detectRouteType()`
- [ ] Unit tests for `RouteAnnotationUtil.validateRouteAnnotation()`
- [ ] Tests for error cases (no annotation, multiple annotations)
- [ ] Tests for `ReturnTypeEnum.fromRouteType()`
- [ ] Tests for `ReturnTypeEnum.getContentType()`
- [ ] Mock tests for `WebBlockRoute.getPath()`

## Integration Points
- **Phase 1**: Uses annotations created in Phase 1
- **Phase 3**: Route discovery system will use `RouteAnnotationUtil`
- **Phase 4**: Response processing will use `ReturnTypeEnum.getContentType()`
- **Existing System**: Maintains compatibility with `RouteDescriptorRegistry`

## Migration Strategy
1. Create new `WebBlockRoute` interface alongside existing interfaces
2. Update `RouteDescriptorRegistry` to work with both systems
3. Gradually migrate routes from old interfaces to new interface
4. Deprecate old interfaces once migration is complete

## JVM Language Compatibility
- Interface works identically across Kotlin, Java, Scala, and Clojure
- No Kotlin-specific features used
- Annotation detection uses standard reflection APIs
- Suspend functions supported in all JVM languages via coroutines

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Include "The purpose of this interface/class/function is to..." documentation
- Use US English in documentation
- Include "Written by Claude Sonnet 4" in class documentation
- Comprehensive comments explaining annotation-based route detection
- Defensive programming with proper validation and error handling

## Dependencies
- **Phase 1**: Requires annotation classes
- **Existing**: Uses `RouteDescriptorRegistry` and `WebBlockResponse`
- **Future**: Will be used by all subsequent phases

## Notes
- Single interface approach simplifies the route system
- Annotation-based type detection provides flexibility
- Maintains async support with suspend functions
- Validation ensures proper annotation usage
- Path resolution logic preserved from existing system