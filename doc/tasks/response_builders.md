# Task: Implement Response Builders for Cross-JVM Language Support

## Overview
Add builder pattern to `HtmlResponse` and `JsonResponse` classes to provide better cross-JVM language support and more readable code when setting optional parameters like status codes and headers.

## Problem Statement
The current convenience functions (`html()` and `json()`) use Kotlin default parameters, which creates awkward usage in other JVM languages:

```java
// Java - Must provide ALL parameters in order
return ResponseBuildersKt.html(
    "<h1>Created</h1>", 
    201,                          // status
    Collections.emptyMap()        // headers (required even if empty!)
);
```

This violates the framework's principle of being accessible to "any JVM language - Kotlin, Java, Scala, Clojure, and more."

## Solution: Builder Pattern
Implement builder classes for both `HtmlResponse` and `JsonResponse` that provide a fluent, self-documenting API that works identically across all JVM languages.

## Implementation Requirements

### 1. Create HtmlResponseBuilder
```kotlin
/**
 * The purpose of this class is to provide a fluent builder for creating HtmlResponse
 * objects that works consistently across all JVM languages.
 *
 * Written by Claude Sonnet 4
 */
class HtmlResponseBuilder {
    private var content: String = ""
    private var status: Int = 200
    private val headers: MutableMap<String, String> = mutableMapOf()
    
    /**
     * Sets the HTML content for the response. Replaces any previously set content.
     */
    fun setContent(content: String) = apply { this.content = content }
    
    /**
     * Sets the HTTP status code. Replaces any previously set status.
     */
    fun setStatus(status: Int) = apply { this.status = status }
    
    /**
     * Adds a single header to the response. Multiple calls add multiple headers.
     */
    fun addHeader(key: String, value: String) = apply { headers[key] = value }
    
    /**
     * Adds multiple headers to the response. Merges with existing headers.
     */
    fun addHeaders(headers: Map<String, String>) = apply { this.headers.putAll(headers) }
    
    /**
     * Builds the final HtmlResponse object.
     */
    fun build(): HtmlResponse {
        require(content.isNotEmpty()) { "HTML content cannot be empty" }
        return HtmlResponse(content, status, headers.toMap())
    }
}
```

### 2. Create JsonResponseBuilder
```kotlin
/**
 * The purpose of this class is to provide a fluent builder for creating JsonResponse
 * objects that works consistently across all JVM languages.
 *
 * Written by Claude Sonnet 4
 */
class JsonResponseBuilder {
    private var data: Any? = null
    private var status: Int = 200
    private val headers: MutableMap<String, String> = mutableMapOf()
    
    /**
     * Sets the data to be serialized as JSON. Replaces any previously set data.
     */
    fun setData(data: Any) = apply { this.data = data }
    
    /**
     * Sets the HTTP status code. Replaces any previously set status.
     */
    fun setStatus(status: Int) = apply { this.status = status }
    
    /**
     * Adds a single header to the response. Multiple calls add multiple headers.
     */
    fun addHeader(key: String, value: String) = apply { headers[key] = value }
    
    /**
     * Adds multiple headers to the response. Merges with existing headers.
     */
    fun addHeaders(headers: Map<String, String>) = apply { this.headers.putAll(headers) }
    
    /**
     * Builds the final JsonResponse object.
     */
    fun build(): JsonResponse {
        requireNotNull(data) { "JSON data cannot be null" }
        return JsonResponse(data!!, status, headers.toMap())
    }
}
```

### 3. Add Static Factory Methods to Response Classes
Update both response classes to include builder factory methods:

```kotlin
data class HtmlResponse(
    val content: String,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse {
    companion object {
        /**
         * Creates a new HtmlResponseBuilder for constructing HtmlResponse objects.
         */
        @JvmStatic
        fun builder(): HtmlResponseBuilder = HtmlResponseBuilder()
    }
}

data class JsonResponse(
    val data: Any,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse {
    companion object {
        /**
         * Creates a new JsonResponseBuilder for constructing JsonResponse objects.
         */
        @JvmStatic
        fun builder(): JsonResponseBuilder = JsonResponseBuilder()
    }
}
```

## Naming Conventions
Use clear, action-based prefixes:
- **`set`** - Methods that replace/set a single value (e.g., `setContent`, `setStatus`)
- **`add`** - Methods that add to a collection (e.g., `addHeader`, `addHeaders`)

This convention makes the behavior immediately clear and prevents confusion about whether calling a method multiple times replaces or accumulates values.

## Usage Examples

### Java
```java
// HTML Response with custom status
return HtmlResponse.builder()
    .setContent("<h1>User Created</h1>")
    .setStatus(201)
    .addHeader("X-User-Id", "123")
    .build();

// JSON Response with multiple headers
return JsonResponse.builder()
    .setData(new User("John", "Doe"))
    .setStatus(200)
    .addHeader("X-Total-Count", "42")
    .addHeader("X-Page", "1")
    .build();
```

### Kotlin
```kotlin
// Can use builder for complex cases
return HtmlResponse.builder()
    .setContent("<h1>User Created</h1>")
    .setStatus(201)
    .addHeader("X-User-Id", "123")
    .build()

// Or continue using convenience functions for simple cases
return html("<h1>Hello</h1>")
return json(userData, status = 201)
```

### Scala
```scala
HtmlResponse.builder()
  .setContent("<h1>Success</h1>")
  .setStatus(200)
  .addHeader("Cache-Control", "no-cache")
  .build()
```

### Clojure
```clojure
(-> (HtmlResponse/builder)
    (.setContent "<h1>Welcome</h1>")
    (.setStatus 200)
    (.addHeader "X-Powered-By" "web-blocks")
    (.build))
```

## Testing Requirements

### 1. Builder Functionality Tests
Test each builder's core functionality:
- Setting and retrieving values
- Default values (status = 200, empty headers)
- Validation (empty content, null data)
- Immutability of built objects

### 2. Cross-Language Simulation Tests
Simulate usage patterns from other JVM languages:
- Test calling without named parameters
- Test method chaining
- Test that headers accumulate correctly
- Test that set methods replace values

### 3. Example Test Structure
```kotlin
class HtmlResponseBuilderTest {
    @Test
    fun `setContent _ called twice _ second call replaces first`() {
        val response = HtmlResponse.builder()
            .setContent("<h1>First</h1>")
            .setContent("<h1>Second</h1>")
            .build()
            
        assertThat(response.content).isEqualTo("<h1>Second</h1>")
    }
    
    @Test
    fun `addHeader _ called multiple times _ all headers are included`() {
        val response = HtmlResponse.builder()
            .setContent("<p>Test</p>")
            .addHeader("X-First", "1")
            .addHeader("X-Second", "2")
            .build()
            
        val headers = response.headers
        assertThat(headers).hasSize(2)
        assertThat(headers["X-First"]).isEqualTo("1")
        assertThat(headers["X-Second"]).isEqualTo("2")
    }
    
    @Test
    fun `build _ content not set _ throws IllegalArgumentException`() {
        val builder = HtmlResponse.builder()
            .setStatus(200)
            
        assertThatThrownBy { builder.build() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("HTML content cannot be empty")
    }
}
```

## Migration Guide for Sample Code
Create separate sample routes to demonstrate different response patterns:

### 1. Keep Existing Simple Examples
No changes needed to existing simple examples - they continue using convenience functions:
```kotlin
// In: pages/simple_page/ThePage.kt
class ThePage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return html("<h1>Hello World</h1>")
    }
}
```

### 2. Add New Builder Examples
Create new sample routes that demonstrate builder usage:

```kotlin
// In: pages/page_with_custom_status/ThePageWithStatus.kt
class ThePageWithStatus : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return HtmlResponse.builder()
            .setContent("<h1>Resource Created Successfully</h1>")
            .setStatus(201)
            .build()
    }
}

// In: pages/page_with_headers/ThePageWithHeaders.kt
class ThePageWithHeaders : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return HtmlResponse.builder()
            .setContent("<h1>Advanced Page</h1>")
            .setStatus(200)
            .addHeader("X-Page-Type", "advanced")
            .addHeader("Cache-Control", "no-cache")
            .build()
    }
}

// In: api/UserApiWithHeaders.kt
class UserApiWithHeaders(val userId: Int) : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        val user = fetchUser(userId)
        return JsonResponse.builder()
            .setData(user)
            .setStatus(200)
            .addHeader("X-Total-Count", "1")
            .addHeader("X-Rate-Limit-Remaining", "99")
            .build()
    }
}
```

### 3. Add Cross-Language Examples
Create examples specifically for Java developers:

```kotlin
// In: pages/java_style_page/JavaStylePage.kt
/**
 * The purpose of this class is to demonstrate builder usage
 * that Java developers would find familiar.
 */
class JavaStylePage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        // This style works identically in Java
        return HtmlResponse.builder()
            .setContent(generateContent())
            .setStatus(200)
            .addHeaders(getDefaultHeaders())
            .build()
    }
    
    private fun generateContent(): String {
        return "<h1>Java-Friendly Example</h1>"
    }
    
    private fun getDefaultHeaders(): Map<String, String> {
        return mapOf(
            "X-Framework" to "web-blocks",
            "X-Language-Friendly" to "true"
        )
    }
}
```

## Documentation Updates
1. Update `doc/user_doc/1_getting_started.md` to mention both convenience functions and builders
2. Add a new section in `doc/user_doc/2_routes.md` about response builders:
   - Explain when to use convenience functions vs builders
   - Show examples for each JVM language
   - Reference the new sample routes that demonstrate each approach
3. Update the sample README to list the new builder example routes

## Benefits
1. **True JVM Language Compatibility** - Works identically in Java, Scala, Clojure, etc.
2. **Self-Documenting** - Method names clearly indicate behavior
3. **Flexible** - Only set what you need, in any order
4. **Type-Safe** - Compile-time validation
5. **Maintains Simplicity** - Kotlin users can still use convenience functions

## Success Criteria
1. Builders work identically across Java, Kotlin, Scala, and Clojure
2. All builder methods are fluent (return `this`)
3. Validation prevents invalid states (empty content, null data)
4. Comprehensive test coverage including cross-language scenarios
5. Documentation updated with examples for multiple JVM languages
6. Existing convenience functions remain unchanged

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Builder classes should be under 100 lines
- Each method has clear documentation
- Add "Written by Claude Sonnet 4" to AI-generated code
- Extensive unit tests following testing standards