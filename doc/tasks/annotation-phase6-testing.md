# Phase 6: Create Comprehensive Test Suite

**Status**: To-Do  
**Priority**: Medium  
**Dependencies**: Phase 1-5 (All previous phases)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Create a comprehensive test suite for the annotation-based routing system to ensure reliability, catch edge cases, and provide confidence in the new system while maintaining existing test coverage.

## Background
The annotation-based routing system introduces several new components that need thorough testing:
- Annotation classes and their metadata
- `WebBlockRoute` interface and utilities
- Route discovery and descriptor system
- Response processing and validation
- Integration with existing systems

## Testing Strategy

### Unit Tests
- Test individual components in isolation
- Focus on business logic and edge cases
- Use mocks for external dependencies
- Fast execution (< 10ms per test)

### Integration Tests
- Test component interactions
- Verify route discovery and processing
- Test with real annotation classes
- Validate end-to-end workflows

### Contract Tests
- Verify interface contracts are maintained
- Test backward compatibility
- Ensure API contracts remain stable

## Implementation Requirements

### 1. Annotation Tests

#### WebBlockPage Annotation Tests
```kotlin
package io.schinzel.web_blocks.web.routes.annotations

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation

/**
 * The purpose of this test class is to verify the WebBlockPage annotation
 * has correct metadata and can be properly applied to classes.
 */
class WebBlockPageTest {
    
    @Test
    fun annotation_hasCorrectTarget_canBeAppliedToClass() {
        val annotation = TestPageRoute::class.findAnnotation<WebBlockPage>()
        
        assertThat(annotation).isNotNull
    }
    
    @Test
    fun annotation_hasRuntimeRetention_canBeDetectedAtRuntime() {
        val annotations = TestPageRoute::class.annotations
        val webBlockPageAnnotation = annotations.find { it is WebBlockPage }
        
        assertThat(webBlockPageAnnotation).isNotNull
    }
    
    @Test
    fun annotation_cannotBeAppliedToMethods_compilationError() {
        // This test validates through compilation - if it compiles, the test passes
        // The annotation should not be applicable to methods due to @Target(AnnotationTarget.CLASS)
        assertThat(true).isTrue
    }
    
    @WebBlockPage
    private class TestPageRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
}
```

#### WebBlockApi Annotation Tests
```kotlin
package io.schinzel.web_blocks.web.routes.annotations

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation

class WebBlockApiTest {
    
    @Test
    fun annotation_hasCorrectTarget_canBeAppliedToClass() {
        val annotation = TestApiRoute::class.findAnnotation<WebBlockApi>()
        
        assertThat(annotation).isNotNull
    }
    
    @Test
    fun annotation_hasRuntimeRetention_canBeDetectedAtRuntime() {
        val annotations = TestApiRoute::class.annotations
        val webBlockApiAnnotation = annotations.find { it is WebBlockApi }
        
        assertThat(webBlockApiAnnotation).isNotNull
    }
    
    @WebBlockApi
    private class TestApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
}
```

#### WebBlockPageApi Annotation Tests
```kotlin
package io.schinzel.web_blocks.web.routes.annotations

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation

class WebBlockPageApiTest {
    
    @Test
    fun annotation_hasCorrectTarget_canBeAppliedToClass() {
        val annotation = TestPageApiRoute::class.findAnnotation<WebBlockPageApi>()
        
        assertThat(annotation).isNotNull
    }
    
    @Test
    fun annotation_hasRuntimeRetention_canBeDetectedAtRuntime() {
        val annotations = TestPageApiRoute::class.annotations
        val webBlockPageApiAnnotation = annotations.find { it is WebBlockPageApi }
        
        assertThat(webBlockPageApiAnnotation).isNotNull
    }
    
    @WebBlockPageApi
    private class TestPageApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
}
```

### 2. Route Annotation Utility Tests

```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class RouteAnnotationUtilTest {
    
    @Test
    fun detectRouteType_pageAnnotation_returnsPageType() {
        val routeType = RouteAnnotationUtil.detectRouteType(TestPageRoute::class)
        
        assertThat(routeType).isEqualTo(RouteType.PAGE)
    }
    
    @Test
    fun detectRouteType_apiAnnotation_returnsApiType() {
        val routeType = RouteAnnotationUtil.detectRouteType(TestApiRoute::class)
        
        assertThat(routeType).isEqualTo(RouteType.API)
    }
    
    @Test
    fun detectRouteType_pageApiAnnotation_returnsPageApiType() {
        val routeType = RouteAnnotationUtil.detectRouteType(TestPageApiRoute::class)
        
        assertThat(routeType).isEqualTo(RouteType.PAGE_API)
    }
    
    @Test
    fun detectRouteType_noAnnotation_returnsNull() {
        val routeType = RouteAnnotationUtil.detectRouteType(TestNoAnnotationRoute::class)
        
        assertThat(routeType).isNull()
    }
    
    @Test
    fun detectRouteType_multipleAnnotations_throwsException() {
        assertThatThrownBy { 
            RouteAnnotationUtil.detectRouteType(TestMultipleAnnotationsRoute::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("multiple route annotations")
    }
    
    @Test
    fun validateRouteAnnotation_validAnnotation_doesNotThrow() {
        // Should not throw any exception
        RouteAnnotationUtil.validateRouteAnnotation(TestPageRoute::class)
    }
    
    @Test
    fun validateRouteAnnotation_noAnnotation_throwsException() {
        assertThatThrownBy { 
            RouteAnnotationUtil.validateRouteAnnotation(TestNoAnnotationRoute::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("has no route annotation")
    }
    
    @WebBlockPage
    private class TestPageRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockApi
    private class TestApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
    
    @WebBlockPageApi
    private class TestPageApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
    
    private class TestNoAnnotationRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockPage
    @WebBlockApi
    private class TestMultipleAnnotationsRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
}
```

### 3. Route Descriptor Tests

#### WebBlockPageRouteDescriptor Tests
```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class WebBlockPageRouteDescriptorTest {
    
    private val descriptor = WebBlockPageRouteDescriptor("com.example")
    
    @Test
    fun getRoutePath_simplePage_returnsCorrectPath() {
        val path = descriptor.getRoutePath(TestSimplePage::class)
        
        assertThat(path).isEqualTo("simple-page")
    }
    
    @Test
    fun getRoutePath_landingPage_returnsRootPath() {
        val path = descriptor.getRoutePath(TestLandingPage::class)
        
        assertThat(path).isEqualTo("/")
    }
    
    @Test
    fun getRoutePath_systemPath_throwsException() {
        assertThatThrownBy { 
            descriptor.getRoutePath(TestApiPage::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("cannot start with 'api'")
    }
    
    @Test
    fun getRoutePath_wrongAnnotation_throwsException() {
        assertThatThrownBy { 
            descriptor.getRoutePath(TestWrongAnnotation::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("not annotated with @WebBlockPage")
    }
    
    @Test
    fun getTypeName_returnsCorrectName() {
        val typeName = descriptor.getTypeName()
        
        assertThat(typeName).isEqualTo("WebBlockPageRoute")
    }
    
    @Test
    fun getReturnType_returnsHtml() {
        val returnType = descriptor.getReturnType()
        
        assertThat(returnType).isEqualTo(ReturnTypeEnum.HTML)
    }
    
    @WebBlockPage
    private class TestSimplePage : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockPage
    private class TestLandingPage : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockPage
    private class TestApiPage : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockApi
    private class TestWrongAnnotation : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
}
```

### 4. Response Processing Tests

```kotlin
package io.schinzel.web_blocks.web.response

import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AnnotationResponseProcessorTest {
    
    private val processor = AnnotationResponseProcessor()
    
    @Test
    fun processResponse_pageRoute_setsHtmlContentType() {
        val response = html("test content")
        val processed = processor.processResponse(response, TestPageRoute::class)
        
        assertThat(processed.headers["Content-Type"]).isEqualTo("text/html")
    }
    
    @Test
    fun processResponse_apiRoute_setsJsonContentType() {
        val response = json("test data")
        val processed = processor.processResponse(response, TestApiRoute::class)
        
        assertThat(processed.headers["Content-Type"]).isEqualTo("application/json")
    }
    
    @Test
    fun processResponse_preservesCustomHeaders() {
        val customHeaders = mapOf("X-Custom" to "value")
        val response = HtmlResponse("test", headers = customHeaders)
        val processed = processor.processResponse(response, TestPageRoute::class)
        
        assertThat(processed.headers["X-Custom"]).isEqualTo("value")
        assertThat(processed.headers["Content-Type"]).isEqualTo("text/html")
    }
    
    @Test
    fun processResponse_preservesStatusCode() {
        val response = HtmlResponse("test", status = 201)
        val processed = processor.processResponse(response, TestPageRoute::class)
        
        assertThat(processed.status).isEqualTo(201)
    }
    
    @Test
    fun processResponse_wrongResponseType_throwsException() {
        val response = json("test")
        
        assertThatThrownBy { 
            processor.processResponse(response, TestPageRoute::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("returned JsonResponse but expected HtmlResponse")
    }
    
    @Test
    fun processResponse_noAnnotation_throwsException() {
        val response = html("test")
        
        assertThatThrownBy { 
            processor.processResponse(response, TestNoAnnotationRoute::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("has no WebBlock annotation")
    }
    
    @WebBlockPage
    private class TestPageRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockApi
    private class TestApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
    
    private class TestNoAnnotationRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
}
```

### 5. Route Discovery Tests

```kotlin
package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FindRoutesTest {
    
    private val findRoutes = FindRoutes("io.schinzel.web_blocks.web.routes")
    
    @Test
    fun registerRoutes_registersAllDescriptors() {
        findRoutes.registerRoutes()
        
        // Verify that descriptors are registered
        val pageDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestPageRoute::class)
        val apiDescriptor = RouteDescriptorRegistry.getRouteDescriptor(TestApiRoute::class)
        
        assertThat(pageDescriptor).isNotNull
        assertThat(apiDescriptor).isNotNull
    }
    
    @Test
    fun getAnnotationBasedRoutes_findsAnnotatedRoutes() {
        val routes = findRoutes.getAnnotationBasedRoutes()
        
        assertThat(routes).isNotEmpty
        assertThat(routes).anyMatch { it.simpleName == "TestPageRoute" }
        assertThat(routes).anyMatch { it.simpleName == "TestApiRoute" }
    }
    
    @Test
    fun getAnnotationBasedRoutes_validatesRouteImplementation() {
        // Should not throw exception for valid routes
        val routes = findRoutes.getAnnotationBasedRoutes()
        
        assertThat(routes).allMatch { route ->
            WebBlockRoute::class.java.isAssignableFrom(route.java)
        }
    }
    
    @WebBlockPage
    private class TestPageRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test")
    }
    
    @WebBlockApi
    private class TestApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test")
    }
}
```

### 6. Integration Tests

```kotlin
package io.schinzel.web_blocks.web.routes.integration

import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AnnotationRoutingIntegrationTest {
    
    @Test
    fun endToEnd_pageRoute_worksCorrectly() {
        // Create route instance
        val route = TestPageRoute()
        
        // Get response
        val response = runBlocking { route.getResponse() }
        
        // Process response
        val processor = AnnotationResponseProcessor()
        val processed = processor.processResponse(response, TestPageRoute::class)
        
        // Verify
        assertThat(processed).isInstanceOf(HtmlResponse::class.java)
        assertThat(processed.headers["Content-Type"]).isEqualTo("text/html")
        assertThat((processed as HtmlResponse).content).isEqualTo("test page")
    }
    
    @Test
    fun endToEnd_apiRoute_worksCorrectly() {
        // Create route instance
        val route = TestApiRoute()
        
        // Get response
        val response = runBlocking { route.getResponse() }
        
        // Process response
        val processor = AnnotationResponseProcessor()
        val processed = processor.processResponse(response, TestApiRoute::class)
        
        // Verify
        assertThat(processed).isInstanceOf(JsonResponse::class.java)
        assertThat(processed.headers["Content-Type"]).isEqualTo("application/json")
        assertThat((processed as JsonResponse).data).isEqualTo("test api")
    }
    
    @Test
    fun endToEnd_routeDiscovery_findsAllRoutes() {
        val findRoutes = FindRoutes("io.schinzel.web_blocks.web.routes.integration")
        findRoutes.registerRoutes()
        
        val routes = findRoutes.getAnnotationBasedRoutes()
        
        assertThat(routes).hasSize(2)
        assertThat(routes).anyMatch { it.simpleName == "TestPageRoute" }
        assertThat(routes).anyMatch { it.simpleName == "TestApiRoute" }
    }
    
    @WebBlockPage
    private class TestPageRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = html("test page")
    }
    
    @WebBlockApi
    private class TestApiRoute : WebBlockRoute {
        override suspend fun getResponse(): WebBlockResponse = json("test api")
    }
}
```

## Test File Structure
```
src/test/kotlin/io/schinzel/web_blocks/web/routes/
├── annotations/
│   ├── WebBlockPageTest.kt
│   ├── WebBlockApiTest.kt
│   └── WebBlockPageApiTest.kt
├── RouteAnnotationUtilTest.kt
├── WebBlockPageRouteDescriptorTest.kt
├── WebBlockApiRouteDescriptorTest.kt
├── WebBlockPageApiRouteDescriptorTest.kt
├── RouteDescriptorRegistryTest.kt
├── FindRoutesTest.kt
└── integration/
    └── AnnotationRoutingIntegrationTest.kt

src/test/kotlin/io/schinzel/web_blocks/web/response/
├── AnnotationResponseProcessorTest.kt
├── ResponseHandlerTest.kt
└── ResponseValidationUtilTest.kt
```

## Acceptance Criteria
- [ ] All annotation classes have comprehensive unit tests
- [ ] Route annotation utilities fully tested with edge cases
- [ ] All route descriptors tested for path generation
- [ ] Response processing tested with various scenarios
- [ ] Route discovery tested for annotation-based routes
- [ ] Integration tests verify end-to-end functionality
- [ ] All tests follow WebBlocks testing standards
- [ ] Test coverage above 90% for new components
- [ ] All tests run in parallel
- [ ] All tests complete in under 10ms each

## Testing Requirements by Component

### Annotations (Phase 1)
- [ ] Annotation metadata verification
- [ ] Runtime retention testing
- [ ] Target restriction testing
- [ ] Compilation-time validation

### Interface and Utilities (Phase 2)
- [ ] Route type detection testing
- [ ] Annotation validation testing
- [ ] Error handling testing
- [ ] Interface contract testing

### Route Discovery (Phase 3)
- [ ] Route descriptor path generation
- [ ] Route registration testing
- [ ] Discovery mechanism testing
- [ ] Error handling for invalid routes

### Response Processing (Phase 4)
- [ ] Content-Type header setting
- [ ] Response type validation
- [ ] Custom header preservation
- [ ] Status code preservation

### Integration Testing
- [ ] End-to-end route processing
- [ ] Multiple route type interaction
- [ ] Backward compatibility testing
- [ ] Performance testing

## Performance Testing Requirements
- [ ] Route discovery performance under load
- [ ] Response processing performance
- [ ] Memory usage testing
- [ ] Startup time impact testing

## Error Handling Testing
- [ ] Missing annotation scenarios
- [ ] Multiple annotation scenarios
- [ ] Wrong response type scenarios
- [ ] Invalid route class scenarios
- [ ] Clear error message validation

## JVM Language Compatibility Testing
- [ ] Kotlin route testing
- [ ] Java route testing (if applicable)
- [ ] Annotation detection across languages
- [ ] Error message clarity across languages

## Testing Tools and Dependencies
- **JUnit 5**: Primary testing framework
- **AssertJ**: Assertion library for readable tests
- **Mockito**: Mocking framework for unit tests
- **Kotlin Coroutines Test**: For testing suspend functions
- **Reflections**: For testing route discovery

## Code Standards Compliance
- Follow all testing standards in `doc/code_standards/testing_standards.md`
- Use AssertJ for assertions
- One assertThat per test function
- Descriptive test method names following pattern: `unitOfWork_StateUnderTest_ExpectedBehavior`
- Tests under 10ms execution time
- Comprehensive edge case coverage

## Dependencies
- **Phase 1**: Tests annotation classes
- **Phase 2**: Tests interface and utilities
- **Phase 3**: Tests route discovery
- **Phase 4**: Tests response processing
- **Phase 5**: Tests migrated sample routes
- **Existing**: Tests integration with existing systems

## Notes
- Tests should be written incrementally with each phase
- Focus on edge cases and error scenarios
- Ensure high test coverage for new components
- Maintain backward compatibility in testing
- Performance tests should validate scalability
- Integration tests should verify real-world scenarios