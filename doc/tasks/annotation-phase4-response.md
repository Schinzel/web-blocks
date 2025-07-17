# Phase 4: Update Response Processing

**Status**: To-Do  
**Priority**: Medium  
**Dependencies**: Phase 1 (Annotations), Phase 2 (Interface), Phase 3 (Discovery)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Update the response processing system to handle annotation-based routes by setting appropriate Content-Type headers based on route annotations while maintaining existing status code and custom header support.

## Background
The current response processing system works with the typed `WebBlockResponse` system (`HtmlResponse`, `JsonResponse`). The new annotation-based system needs to:
1. Detect route type from annotations (`@WebBlockPage`, `@WebBlockApi`, `@WebBlockPageApi`)
2. Set appropriate Content-Type headers based on annotation type
3. Maintain existing status code and custom header functionality
4. Support both old and new route systems during migration

## Current Response System Analysis
- **`WebBlockResponse`**: Sealed interface with `HtmlResponse` and `JsonResponse`
- **Response Builders**: `HtmlResponseBuilder` and `JsonResponseBuilder` for Java compatibility
- **Convenience Functions**: `html()` and `json()` functions
- **Headers**: Custom headers supported through response constructors
- **Status Codes**: Custom status codes supported through response constructors

## Implementation Requirements

### 1. Create Response Processor for Annotation-Based Routes

```kotlin
package io.schinzel.web_blocks.web.response

import io.schinzel.web_blocks.web.routes.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.RouteTypeEnum
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import kotlin.reflect.KClass

/**
 * The purpose of this class is to process responses from annotation-based routes
 * and ensure proper Content-Type headers are set based on the route annotation.
 * 
 * Written by Claude Sonnet 4
 */
class AnnotationResponseProcessor {
    
    /**
     * Process a response from an annotation-based route
     * and ensure it has the correct Content-Type header based on the route's annotation.
     * 
     * @param response The response from the route
     * @param routeClass The class of the route that generated the response
     * @return Processed response with correct Content-Type header
     */
    fun processResponse(
        response: WebBlockResponse,
        routeClass: KClass<out WebBlockRoute>
    ): WebBlockResponse {
        val routeType = RouteAnnotationUtil.detectRouteType(routeClass)
        
        if (routeType == RouteTypeEnum.UNKNOWN) {
            throw IllegalArgumentException(
                "Route class ${routeClass.simpleName} has no WebBlock annotation"
            )
        }
        
        val expectedContentType = routeType.contentType
        
        // Validate response type matches annotation
        validateResponseType(response, routeType, routeClass)
        
        // Add or update Content-Type header
        return addContentTypeHeader(response, expectedContentType)
    }
    
    
    /**
     * Validate that the response type
     * matches the expected type based on the route annotation.
     * 
     * @param response The response to validate
     * @param routeType The route type from annotation
     * @param routeClass The route class for error messages
     */
    private fun validateResponseType(
        response: WebBlockResponse,
        routeType: RouteTypeEnum,
        routeClass: KClass<out WebBlockRoute>
    ) {
        if (!routeType.isValidResponseType(response)) {
            val expectedType = routeType.getExpectedResponseType()
            val actualType = response::class.simpleName
            
            throw IllegalArgumentException(
                "Route ${routeClass.simpleName} with annotation @WebBlock${routeType.name.lowercase().replaceFirstChar { it.uppercase() }} " +
                "returned $actualType but expected $expectedType"
            )
        }
    }
    
    /**
     * Add or update the Content-Type header
     * on a response while preserving existing headers.
     * 
     * @param response The original response
     * @param contentType The Content-Type header value to set
     * @return Response with updated Content-Type header
     */
    private fun addContentTypeHeader(
        response: WebBlockResponse,
        contentType: String
    ): WebBlockResponse {
        val updatedHeaders = response.headers.toMutableMap()
        updatedHeaders["Content-Type"] = contentType
        
        return response.copy(headers = updatedHeaders)
    }
}
```

### 2. Update Response Handler Integration

```kotlin
package io.schinzel.web_blocks.web.response

import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import kotlin.reflect.KClass

/**
 * The purpose of this class is to handle response processing for both
 * interface-based and annotation-based routes.
 * 
 * Written by Claude Sonnet 4
 */
class ResponseHandler {
    
    private val annotationProcessor = AnnotationResponseProcessor()
    
    /**
     * Process a response from any route type
     * and ensure it has appropriate headers based on the route system used.
     * 
     * @param response The response from the route
     * @param routeClass The class of the route that generated the response
     * @return Processed response with appropriate headers
     */
    fun processResponse(
        response: WebBlockResponse,
        routeClass: KClass<out IRoute>
    ): WebBlockResponse {
        return when {
            // New annotation-based system
            WebBlockRoute::class.java.isAssignableFrom(routeClass.java) -> {
                val webBlockRouteClass = routeClass as KClass<out WebBlockRoute>
                annotationProcessor.processResponse(response, webBlockRouteClass)
            }
            
            // Legacy interface-based system - already has correct headers
            else -> response
        }
    }
}
```

### 3. Create Response Validation Utilities

```kotlin
package io.schinzel.web_blocks.web.response

import io.schinzel.web_blocks.web.routes.RouteTypeEnum

/**
 * The purpose of this object is to provide utilities for validating responses
 * and their compatibility with different route types.
 * 
 * Written by Claude Sonnet 4
 */
object ResponseValidationUtil {
    
    /**
     * Check if a response type is compatible
     * with a given route type.
     * 
     * @param response The response to check
     * @param routeType The route type to check against
     * @return true if response is compatible with route type
     */
    fun isResponseCompatible(response: WebBlockResponse, routeType: RouteTypeEnum): Boolean {
        return routeType.isValidResponseType(response)
    }
    
    /**
     * Get the expected response type name
     * for a given route type.
     * 
     * @param routeType The route type
     * @return The expected response type name
     */
    fun getExpectedResponseType(routeType: RouteTypeEnum): String {
        return routeType.getExpectedResponseType()
    }
    
    /**
     * Get the expected Content-Type header
     * for a given route type.
     * 
     * @param routeType The route type
     * @return The expected Content-Type header value
     */
    fun getExpectedContentType(routeType: RouteTypeEnum): String {
        return routeType.contentType
    }
}
```

### 4. Update HTTP Response Integration

```kotlin
package io.schinzel.web_blocks.web.http

import io.schinzel.web_blocks.web.response.ResponseHandler
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IRoute
import kotlin.reflect.KClass

/**
 * The purpose of this class is to convert WebBlockResponse objects to HTTP responses
 * with proper headers and status codes.
 * 
 * Updated to work with both interface-based and annotation-based routes.
 * 
 * Written by Claude Sonnet 4
 */
class HttpResponseBuilder {
    
    private val responseHandler = ResponseHandler()
    
    /**
     * Build an HTTP response from a WebBlockResponse
     * ensuring proper headers are set based on the route type.
     * 
     * @param response The WebBlockResponse from the route
     * @param routeClass The class of the route that generated the response
     * @return HTTP response with proper headers and status code
     */
    fun buildHttpResponse(
        response: WebBlockResponse,
        routeClass: KClass<out IRoute>
    ): HttpResponse {
        // Process response to ensure correct headers
        val processedResponse = responseHandler.processResponse(response, routeClass)
        
        return HttpResponse(
            statusCode = processedResponse.status,
            headers = processedResponse.headers,
            body = extractResponseBody(processedResponse)
        )
    }
    
    /**
     * Extract the response body content
     * from a WebBlockResponse.
     * 
     * @param response The WebBlockResponse
     * @return String representation of the response body
     */
    private fun extractResponseBody(response: WebBlockResponse): String {
        return when (response) {
            is HtmlResponse -> response.content
            is JsonResponse -> serializeToJson(response.data)
        }
    }
    
    /**
     * Serialize data to JSON format.
     * 
     * @param data The data to serialize
     * @return JSON string representation
     */
    private fun serializeToJson(data: Any): String {
        // Implementation depends on JSON library used
        // This is a placeholder for the actual serialization logic
        return JsonSerializer.serialize(data)
    }
}

/**
 * The purpose of this data class is to represent an HTTP response
 * with status code, headers, and body.
 * 
 * Written by Claude Sonnet 4
 */
data class HttpResponse(
    val statusCode: Int,
    val headers: Map<String, String>,
    val body: String
)
```

## File Structure
```
src/main/kotlin/io/schinzel/web_blocks/web/response/
├── AnnotationResponseProcessor.kt
├── ResponseHandler.kt
├── ResponseValidationUtil.kt
└── HttpResponseBuilder.kt (updated)
```

## Acceptance Criteria
- [ ] `AnnotationResponseProcessor` created with proper header handling
- [ ] `ResponseHandler` supports both interface and annotation-based routes
- [ ] Response validation ensures correct response types for annotations
- [ ] Content-Type headers set correctly based on route annotations
- [ ] Existing status code and custom header functionality preserved
- [ ] Error handling provides clear validation messages
- [ ] All code follows WebBlocks coding standards
- [ ] Classes are under 250 lines each
- [ ] Functions are under 10 lines each
- [ ] Comprehensive documentation with examples

## Testing Requirements
- [ ] Unit tests for `AnnotationResponseProcessor.processResponse()`
- [ ] Unit tests for response type validation
- [ ] Unit tests for Content-Type header setting
- [ ] Unit tests for `ResponseHandler` with both route types
- [ ] Unit tests for `ResponseValidationUtil` functions
- [ ] Integration tests for HTTP response building
- [ ] Tests for error cases (wrong response type, missing annotation)
- [ ] Tests for custom headers and status codes preservation

## Integration Points
- **Phase 1**: Uses annotations for route type detection
- **Phase 2**: Uses `RouteAnnotationUtil` for annotation detection
- **Phase 3**: Works with annotation-based route descriptors
- **Phase 5**: Will be used during sample route migration
- **Existing**: Maintains compatibility with existing response system

## Error Handling
- Clear validation errors for response type mismatches
- Helpful error messages indicating expected vs actual response types
- Graceful handling of missing annotations
- Preservation of existing error handling for interface-based routes

## Performance Considerations
- Response processing happens per request
- Minimal overhead for annotation detection
- Efficient header manipulation without unnecessary copying
- Caching of route type detection results could be added

## Backward Compatibility
- Interface-based routes continue to work unchanged
- Existing response builders remain functional
- Custom headers and status codes fully supported
- No breaking changes to existing API

## JVM Language Compatibility
- Response processing works identically across all JVM languages
- No Kotlin-specific features used
- Standard reflection APIs for annotation detection
- Clear error messages across all JVM languages

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Include "The purpose of this class/function is to..." documentation
- Use US English in documentation
- Include "Written by Claude Sonnet 4" in class documentation
- Comprehensive error handling with descriptive messages
- Defensive programming with proper validation

## Dependencies
- **Phase 1**: Requires annotation classes
- **Phase 2**: Requires `RouteAnnotationUtil` and `RouteTypeEnum`
- **Existing**: Uses `WebBlockResponse`, `HtmlResponse`, `JsonResponse`
- **JSON**: Depends on JSON serialization library

## Migration Strategy
1. **Phase 1**: Create response processor alongside existing system
2. **Phase 2**: Update HTTP response builder to use new processor
3. **Phase 3**: Test with annotation-based routes
4. **Phase 4**: Ensure backward compatibility maintained
5. **Phase 5**: Use during sample route migration

## Notes
- Content-Type headers set based on annotation type, not response type
- Response type validation ensures consistency
- Custom headers and status codes fully preserved
- Clear error messages help developers debug issues
- Efficient processing with minimal overhead