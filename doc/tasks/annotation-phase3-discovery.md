# Phase 3: Update Route Discovery System

**Status**: To-Do  
**Priority**: High  
**Dependencies**: Phase 1 (Annotations), Phase 2 (Interface)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Replace the interface-based route discovery system with annotation-based route identification while maintaining the existing file-system based path generation. This creates a simpler, cleaner system without the complexity of supporting dual systems.

## Background
The current route discovery system uses reflection to find classes implementing `IPageRoute`, `IApiRoute`, and `IPageApiRoute`. The new system will:
1. Find classes annotated with `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi`
2. Validate that annotated classes implement `WebBlockRoute`
3. Maintain existing path generation logic
4. Completely replace the old interface-based system

## Current System Analysis
- **`RouteDescriptorRegistry`**: Currently stores route descriptors mapped to interface types - will be simplified
- **`FindRoutes`**: Currently scans for classes implementing route interfaces - will be updated for annotations
- **Route Descriptors**: Current `PageRouteDescriptor`, `ApiRouteDescriptor`, `PageApiRouteDescriptor` will be replaced
- **Path Generation**: Uses file system structure with snake_case → kebab-case conversion - will be preserved

## Implementation Requirements

### 1. Create Annotation-Based Route Descriptors

#### WebBlockPageRouteDescriptor
```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockPage.
 * 
 * Page routes use directory structure from /pages directory with special handling
 * for the landing page which maps to root path.
 * 
 * Written by Claude Sonnet 4
 */
class WebBlockPageRouteDescriptor(
    private val endpointPackage: String
) : IRouteDescriptor<WebBlockRoute> {
    
    private val systemPaths = listOf("api", "page-api", "static")

    override fun getRoutePath(clazz: KClass<out WebBlockRoute>): String {
        // Validate annotation
        RouteAnnotationUtil.validateRouteAnnotation(clazz)
        
        // Ensure class has @WebBlockPage annotation
        if (RouteAnnotationUtil.detectRouteType(clazz) != RouteTypeEnum.PAGE) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockPage"
            )
        }
        
        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val returnPath = if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
        
        systemPaths.forEach { systemPath ->
            if (returnPath.startsWith(systemPath)) {
                throw IllegalArgumentException(
                    "Page path cannot start with '$systemPath'. Page path: '$returnPath'"
                )
            }
        }
        
        return returnPath
    }

    override fun getTypeName() = "WebBlockPageRoute"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}
```

#### WebBlockApiRouteDescriptor
```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockApi.
 * 
 * API routes use directory structure from /api directory plus the class name
 * with kebab-case conversion and "Route" suffix removal.
 * 
 * Written by Claude Sonnet 4
 */
class WebBlockApiRouteDescriptor(
    private val endpointPackage: String
) : IRouteDescriptor<WebBlockRoute> {

    override fun getRoutePath(clazz: KClass<out WebBlockRoute>): String {
        // Validate annotation
        RouteAnnotationUtil.validateRouteAnnotation(clazz)
        
        // Ensure class has @WebBlockApi annotation
        if (RouteAnnotationUtil.detectRouteType(clazz) != RouteTypeEnum.API) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockApi"
            )
        }
        
        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val classNameKebabCase = RouteUtil
            .removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "WebBlockApiRoute"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}
```

#### WebBlockPageApiRouteDescriptor
```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockPageApi.
 * 
 * Page API routes use directory structure from /pages directory plus the class name
 * with kebab-case conversion and "Route" suffix removal, prefixed with "page-api".
 * 
 * Written by Claude Sonnet 4
 */
class WebBlockPageApiRouteDescriptor(
    private val endpointPackage: String
) : IRouteDescriptor<WebBlockRoute> {

    override fun getRoutePath(clazz: KClass<out WebBlockRoute>): String {
        // Validate annotation
        RouteAnnotationUtil.validateRouteAnnotation(clazz)
        
        // Ensure class has @WebBlockPageApi annotation
        if (RouteAnnotationUtil.detectRouteType(clazz) != RouteTypeEnum.PAGE_API) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockPageApi"
            )
        }
        
        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = RouteUtil
            .removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "WebBlockPageApiRoute"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}
```

### 2. Update RouteDescriptorRegistry

```kotlin
package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this class is to store and manage route descriptors for
 * annotation-based route discovery system.
 * 
 * Simplified to support only the new annotation-based routes.
 * 
 * Written by Claude Sonnet 4
 */
object RouteDescriptorRegistry {
    private val annotationDescriptors = 
        mutableMapOf<RouteTypeEnum, IRouteDescriptor<WebBlockRoute>>()

    /**
     * Register a descriptor for annotation-based routes
     */
    fun registerAnnotation(
        routeType: RouteTypeEnum,
        descriptor: IRouteDescriptor<WebBlockRoute>
    ) {
        annotationDescriptors[routeType] = descriptor
    }

    /**
     * Get route descriptor for annotation-based route class
     */
    @Suppress("UNCHECKED_CAST")
    fun getRouteDescriptor(clazz: KClass<out WebBlockRoute>): IRouteDescriptor<WebBlockRoute> {
        val routeType = RouteAnnotationUtil.detectRouteType(clazz)
        
        if (routeType == RouteTypeEnum.UNKNOWN) {
            throw IllegalArgumentException(
                "Route class ${clazz.simpleName} has no WebBlock annotation"
            )
        }
        
        return annotationDescriptors[routeType]
            ?: throw IllegalArgumentException(
                "No descriptor registered for route type $routeType"
            )
    }
}
```

### 3. Update Route Discovery (FindRoutes)

```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import org.reflections.Reflections
import kotlin.reflect.KClass

/**
 * The purpose of this class is to discover and register annotation-based routes
 * in the WebBlocks framework.
 * 
 * Simplified to support only the new annotation-based system.
 * 
 * Written by Claude Sonnet 4
 */
class FindRoutes(private val endpointPackage: String) {
    
    private val reflections = Reflections(endpointPackage)
    
    /**
     * Discover and register all annotation-based routes
     */
    fun registerRoutes() {
        // Register descriptors for annotation-based routes
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE,
            WebBlockPageRouteDescriptor(endpointPackage)
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.API,
            WebBlockApiRouteDescriptor(endpointPackage)
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE_API,
            WebBlockPageApiRouteDescriptor(endpointPackage)
        )
    }
    
    /**
     * Get all annotation-based route classes
     */
    fun getAnnotationBasedRoutes(): List<KClass<out WebBlockRoute>> {
        val routes = mutableListOf<KClass<out WebBlockRoute>>()
        
        // Find classes annotated with WebBlock route annotations
        routes.addAll(findAnnotatedRoutes<WebBlockPage>())
        routes.addAll(findAnnotatedRoutes<WebBlockApi>())
        routes.addAll(findAnnotatedRoutes<WebBlockPageApi>())
        
        return routes
    }
    
    /**
     * Find classes annotated with a specific route annotation
     */
    private inline fun <reified T : Annotation> findAnnotatedRoutes(): 
        List<KClass<out WebBlockRoute>> {
        
        return reflections.getTypesAnnotatedWith(T::class.java)
            .filter { clazz ->
                // Validate that annotated class implements WebBlockRoute
                WebBlockRoute::class.java.isAssignableFrom(clazz)
            }
            .map { clazz ->
                @Suppress("UNCHECKED_CAST")
                clazz.kotlin as KClass<out WebBlockRoute>
            }
            .also { routes ->
                // Validate each route
                routes.forEach { route ->
                    RouteAnnotationUtil.validateRouteAnnotation(route)
                }
            }
    }
}
```

## File Structure
```
src/main/kotlin/io/schinzel/web_blocks/web/routes/
├── WebBlockPageRouteDescriptor.kt
├── WebBlockApiRouteDescriptor.kt
├── WebBlockPageApiRouteDescriptor.kt
├── RouteDescriptorRegistry.kt (simplified)
└── FindRoutes.kt (simplified)
```

## Acceptance Criteria
- [ ] Three new route descriptors created for annotation-based routes
- [ ] `RouteDescriptorRegistry` simplified to support only annotation-based routes
- [ ] `FindRoutes` simplified to discover only annotation-based routes
- [ ] Validation ensures annotated classes implement `WebBlockRoute`
- [ ] Path generation logic preserved from existing descriptors
- [ ] Old interface-based system completely removed
- [ ] All code follows WebBlocks coding standards
- [ ] Classes are under 250 lines each
- [ ] Functions are under 10 lines each
- [ ] Comprehensive documentation with examples

## Testing Requirements
- [ ] Unit tests for each new route descriptor
- [ ] Tests for path generation with annotation-based routes
- [ ] Tests for route validation (annotation present, implements interface)
- [ ] Tests for error cases (missing annotation, wrong annotation type)
- [ ] Tests for simplified `RouteDescriptorRegistry`
- [ ] Tests for `FindRoutes` discovering annotation-based routes
- [ ] Integration tests ensuring annotation system works correctly

## Migration Strategy
1. **Phase 1**: Create new descriptors to replace existing ones
2. **Phase 2**: Update registry to support only annotation-based routes
3. **Phase 3**: Update discovery to find only annotation-based routes
4. **Phase 4**: Convert all existing routes from interface to annotation in one focused effort
5. **Phase 5**: Remove old interface-based system completely

## Integration Points
- **Phase 1**: Uses annotations created in Phase 1
- **Phase 2**: Uses `WebBlockRoute` interface and `RouteAnnotationUtil`
- **Phase 4**: Response processing will use new descriptors
- **Phase 5**: Route conversion will migrate all existing routes at once

## Error Handling
- Validation ensures annotated classes implement `WebBlockRoute`
- Clear error messages for missing or incorrect annotations
- Proper exception handling for reflection operations
- Fail-fast approach when annotations are missing or invalid

## Performance Considerations
- Route discovery happens at startup, not runtime
- Caching of discovered routes to avoid repeated reflection
- Efficient annotation detection using reflection API
- Improved startup time with single discovery mechanism

## JVM Language Compatibility
- Reflection works identically across all JVM languages
- No Kotlin-specific features used
- Annotation detection uses standard Java reflection
- Error messages are clear across all JVM languages

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Include "The purpose of this class/function is to..." documentation
- Use US English in documentation
- Include "Written by Claude Sonnet 4" in class documentation
- Comprehensive error handling with descriptive messages
- Defensive programming with proper validation

## Dependencies
- **Phase 1**: Requires annotation classes
- **Phase 2**: Requires `WebBlockRoute` interface and utilities
- **Existing**: Uses `RouteUtil`, `IRouteDescriptor`, `ReturnTypeEnum`
- **Reflection**: Uses Reflections library for class scanning
- **Migration**: Requires conversion of all existing routes

## Notes
- Simplified single-system approach reduces complexity
- Annotation validation ensures proper usage
- Path generation logic preserved from existing system
- Clean, focused implementation with no legacy baggage
- Error handling provides clear guidance for developers