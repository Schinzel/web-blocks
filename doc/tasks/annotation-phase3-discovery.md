# Phase 3: Update Route Discovery System

**Status**: To-Do  
**Priority**: High  
**Dependencies**: Phase 1 (Annotations), Phase 2 (Interface)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Update the route discovery system to work with annotation-based route identification while maintaining the existing file-system based path generation and supporting both old and new route systems during transition.

## Background
The current route discovery system uses reflection to find classes implementing `IPageRoute`, `IApiRoute`, and `IPageApiRoute`. The new system needs to:
1. Find classes annotated with `@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi`
2. Validate that annotated classes implement `WebBlockRoute`
3. Maintain existing path generation logic
4. Support both old and new systems during migration

## Current System Analysis
- **`RouteDescriptorRegistry`**: Stores route descriptors mapped to interface types
- **`FindRoutes`**: Scans for classes implementing route interfaces
- **Route Descriptors**: `PageRouteDescriptor`, `ApiRouteDescriptor`, `PageApiRouteDescriptor`
- **Path Generation**: Uses file system structure with snake_case → kebab-case conversion

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
        if (RouteAnnotationUtil.detectRouteType(clazz) != RouteType.PAGE) {
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
        if (RouteAnnotationUtil.detectRouteType(clazz) != RouteType.API) {
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
        if (RouteAnnotationUtil.detectRouteType(clazz) != RouteType.PAGE_API) {
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
 * The purpose of this class is to store and manage route descriptors for both
 * interface-based and annotation-based route discovery systems.
 * 
 * Updated to support both legacy interfaces and new annotation-based routes
 * during the migration period.
 * 
 * Written by Claude Sonnet 4
 */
object RouteDescriptorRegistry {
    private val interfaceDescriptors = 
        mutableMapOf<KClass<out IRoute>, IRouteDescriptor<out IRoute>>()
    
    private val annotationDescriptors = 
        mutableMapOf<RouteType, IRouteDescriptor<WebBlockRoute>>()

    /**
     * Register a descriptor for interface-based routes (legacy system)
     */
    fun <T : IRoute> registerInterface(
        processorType: KClass<T>,
        descriptor: IRouteDescriptor<T>
    ) {
        interfaceDescriptors[processorType] = descriptor
    }
    
    /**
     * Register a descriptor for annotation-based routes (new system)
     */
    fun registerAnnotation(
        routeType: RouteType,
        descriptor: IRouteDescriptor<WebBlockRoute>
    ) {
        annotationDescriptors[routeType] = descriptor
    }

    /**
     * Get route descriptor for any route class (interface or annotation based)
     */
    @Suppress("UNCHECKED_CAST")
    fun getRouteDescriptor(clazz: KClass<out IRoute>): IRouteDescriptor<IRoute> {
        // First try annotation-based system
        if (WebBlockRoute::class.java.isAssignableFrom(clazz.java)) {
            val webBlockRouteClass = clazz as KClass<out WebBlockRoute>
            val routeType = RouteAnnotationUtil.detectRouteType(webBlockRouteClass)
            
            if (routeType != null) {
                return annotationDescriptors[routeType] as? IRouteDescriptor<IRoute>
                    ?: throw IllegalArgumentException(
                        "No annotation-based descriptor registered for route type $routeType"
                    )
            }
        }
        
        // Fall back to interface-based system
        return interfaceDescriptors.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }
            ?.value as? IRouteDescriptor<IRoute>
            ?: throw IllegalArgumentException(
                "No route descriptor registered for ${clazz.simpleName}"
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
 * The purpose of this class is to discover and register both interface-based
 * and annotation-based routes in the WebBlocks framework.
 * 
 * Updated to support both legacy interfaces and new annotation-based routes
 * during the migration period.
 * 
 * Written by Claude Sonnet 4
 */
class FindRoutes(private val endpointPackage: String) {
    
    private val reflections = Reflections(endpointPackage)
    
    /**
     * Discover and register all routes (both interface and annotation based)
     */
    fun registerRoutes() {
        registerInterfaceBasedRoutes()
        registerAnnotationBasedRoutes()
    }
    
    /**
     * Register legacy interface-based routes
     */
    private fun registerInterfaceBasedRoutes() {
        // Register descriptors for legacy interfaces
        RouteDescriptorRegistry.registerInterface(
            IPageRoute::class,
            PageRouteDescriptor(endpointPackage)
        )
        RouteDescriptorRegistry.registerInterface(
            IApiRoute::class,
            ApiRouteDescriptor(endpointPackage)
        )
        RouteDescriptorRegistry.registerInterface(
            IPageApiRoute::class,
            PageApiRouteDescriptor(endpointPackage)
        )
    }
    
    /**
     * Register new annotation-based routes
     */
    private fun registerAnnotationBasedRoutes() {
        // Register descriptors for annotation-based routes
        RouteDescriptorRegistry.registerAnnotation(
            RouteType.PAGE,
            WebBlockPageRouteDescriptor(endpointPackage)
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteType.API,
            WebBlockApiRouteDescriptor(endpointPackage)
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteType.PAGE_API,
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
    
    /**
     * Get all legacy interface-based route classes
     */
    fun getInterfaceBasedRoutes(): List<KClass<out IRoute>> {
        val routes = mutableListOf<KClass<out IRoute>>()
        
        routes.addAll(getSubtypesOf<IPageRoute>())
        routes.addAll(getSubtypesOf<IApiRoute>())
        routes.addAll(getSubtypesOf<IPageApiRoute>())
        
        return routes
    }
    
    /**
     * Get subtypes of a specific interface
     */
    private inline fun <reified T : IRoute> getSubtypesOf(): List<KClass<out T>> {
        return reflections.getSubTypesOf(T::class.java)
            .filter { clazz ->
                !clazz.isInterface && !clazz.isAbstract
            }
            .map { clazz ->
                @Suppress("UNCHECKED_CAST")
                clazz.kotlin as KClass<out T>
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
├── RouteDescriptorRegistry.kt (updated)
└── FindRoutes.kt (updated)
```

## Acceptance Criteria
- [ ] Three new route descriptors created for annotation-based routes
- [ ] `RouteDescriptorRegistry` updated to support both interface and annotation-based routes
- [ ] `FindRoutes` updated to discover annotation-based routes
- [ ] Validation ensures annotated classes implement `WebBlockRoute`
- [ ] Path generation logic preserved from existing descriptors
- [ ] All code follows WebBlocks coding standards
- [ ] Classes are under 250 lines each
- [ ] Functions are under 10 lines each
- [ ] Comprehensive documentation with examples

## Testing Requirements
- [ ] Unit tests for each new route descriptor
- [ ] Tests for path generation with annotation-based routes
- [ ] Tests for route validation (annotation present, implements interface)
- [ ] Tests for error cases (missing annotation, wrong annotation type)
- [ ] Tests for `RouteDescriptorRegistry` with both systems
- [ ] Tests for `FindRoutes` discovering annotation-based routes
- [ ] Integration tests ensuring both systems work together

## Migration Strategy
1. **Phase 1**: Create new descriptors alongside existing ones
2. **Phase 2**: Update registry to support both systems
3. **Phase 3**: Update discovery to find both route types
4. **Phase 4**: Migrate routes one by one from interface to annotation
5. **Phase 5**: Deprecate interface-based system once migration complete

## Integration Points
- **Phase 1**: Uses annotations created in Phase 1
- **Phase 2**: Uses `WebBlockRoute` interface and `RouteAnnotationUtil`
- **Phase 4**: Response processing will use new descriptors
- **Phase 5**: Sample migration will use this discovery system

## Error Handling
- Validation ensures annotated classes implement `WebBlockRoute`
- Clear error messages for missing or incorrect annotations
- Graceful fallback to interface-based system during migration
- Proper exception handling for reflection operations

## Performance Considerations
- Route discovery happens at startup, not runtime
- Caching of discovered routes to avoid repeated reflection
- Efficient annotation detection using reflection API
- Minimal overhead for existing interface-based routes

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

## Notes
- Dual system support allows gradual migration
- Annotation validation ensures proper usage
- Path generation logic preserved from existing system
- Clear separation between interface and annotation-based discovery
- Error handling provides clear guidance for developers