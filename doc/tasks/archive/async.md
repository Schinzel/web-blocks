# Task: Implement Async Rendering for Page Elements

## Overview
Convert the framework to use async/suspend functions throughout, enabling parallel rendering of page elements for better performance.

## Goal
- Enable parallel rendering of page elements
- Use Kotlin coroutines (suspend functions) instead of threads
- No blocking operations - async all the way from routes to page elements
- Single API (no separate sync/async versions)
- Default 1 second timeout per element to encourage snappy UIs

## Changes Required

### 1. Update Core Interfaces

#### IRoute
```kotlin
interface IRoute {
    suspend fun getResponse(): Any  // Add suspend
}
```

#### IPageRoute
```kotlin
interface IPageRoute : IRoute {
    override suspend fun getResponse(): String  // Add suspend
}
```

#### IApiRoute
```kotlin
interface IApiRoute : IRoute {
    override suspend fun getResponse(): Any  // Add suspend
}
```

#### IPageApiRoute
```kotlin
interface IPageApiRoute : IRoute {
    override suspend fun getResponse(): Any  // Add suspend
}
```

#### IPageElement
The base interface for page elements:
```kotlin
interface IPageElement {
    suspend fun getHtml(): String  // Add suspend
    
    // Default timeout of 1 second to encourage snappy UIs
    // Elements can override for special cases (external APIs, etc.)
    val timeoutMs: Long
        get() = 1_000
}
```

#### ObservablePageElement
The abstract class that most page elements extend (provides observable functionality):
```kotlin
abstract class ObservablePageElement : IPageApiRoute, IPageElement {
    // Override getHtml() to be suspend and wrap getResponse()
    override suspend fun getHtml(): String {
        // Get the HTML of the page element
        val pageElementHtml = this.getResponse()  // Now suspend
        // Get the id of the observers
        val observersIdsAsString: String = observers.joinToString(",") { it.guid }
        // ... rest of the wrapping logic remains the same
        return "<div id=\"$guid\" ...>$pageElementHtml</div>"
    }
    
    // getResponse() comes from IPageApiRoute and is now suspend
}

### 2. Update PageBuilder, Row, and Column

Maintain the clean delegation pattern while adding async support. Each class handles its own level of parallelization.

#### PageBuilder
```kotlin
class PageBuilder {
    suspend fun getHtml(): String = supervisorScope {
        // Use supervisorScope for error isolation at row level
        // If one row fails, other rows continue rendering
        val rowsHtml = rows
            .map { async { it.getHtml() } }
            .awaitAll()
            .joinToString("\n")
        
        return@supervisorScope TemplateProcessor(this@PageBuilder)
            .addData("title", title)
            .addData("content", rowsHtml)
            .processTemplate("html/page-template.html")
    }
}
```

#### Row
```kotlin
class Row {
    val columns: MutableList<Column> = mutableListOf()

    suspend fun getHtml(): String = supervisorScope {
        // Use supervisorScope for error isolation at column level
        // If one column fails, other columns continue rendering
        val columnsHtml = columns
            .map { async { it.getHtml() } }
            .awaitAll()
            .joinToString("\n")
        
        """
        |<div class="row">
        |  $columnsHtml
        |</div>
        """.trimMargin()
    }
}
```

#### Column
```kotlin
class Column(val span: Int) {
    val elements: MutableList<ObservablePageElement> = mutableListOf()

    suspend fun getHtml(): String = supervisorScope {
        // Use supervisorScope for error isolation at element level
        // If one element fails/times out, other elements continue rendering
        val elementsHtml = elements
            .map { element -> 
                async { 
                    try {
                        withTimeout(element.timeoutMs) { // Use element's timeout
                            element.getHtml() 
                        }
                    } catch (e: TimeoutCancellationException) {
                        "<div class='error timeout'>Content loading too slow. Please try refreshing.</div>"
                    } catch (e: Exception) {
                        "<div class='error exception'>An unexpected error occurred: ${e.message}</div>"
                    }
                }
            }
            .awaitAll()
            .joinToString("\n")
        
        """
        |<div class="col-$span">
        |  $elementsHtml
        |</div>
        """.trimMargin()
    }
}
```

This approach:
- Maintains the existing clean architecture
- Each class is responsible for its own HTML generation
- Parallel rendering happens at each level (rows, columns, elements)
- Error handling is isolated to the element level
- No large monolithic functions

### 3. Implement Request Scope Management

Create proper request-scoped coroutine management instead of using GlobalScope:

```kotlin
// Extension function to create/get request-scoped CoroutineScope
fun Context.requestScope(): CoroutineScope =
    attribute("requestScope") ?: CoroutineScope(Dispatchers.IO + SupervisorJob()).also {
        attribute("requestScope", it)
        onComplete { it.cancel() } // Cancel scope when request completes
    }
```

### 4. Update RequestHandler

Update RequestHandler to handle async routes using proper structured concurrency:

```kotlin
fun getHandler(): (Context) -> Unit {
    return { ctx: Context ->
        // Use request-scoped coroutine instead of GlobalScope
        val completableFuture = ctx.requestScope().future {
            val returnType = routeMapping.returnType
            val logEntry = LogEntry(
                localTimeZone = webAppConfig.localTimezone,
                routeType = routeMapping.type,
                httpMethod = ctx.method().toString()
            )
            
            val startTime = System.currentTimeMillis()
            logEntry.requestLog.path = routeMapping.path
            logEntry.requestLog.requestBody = ctx.body()
            
            val hasNoArguments = routeMapping.parameters.isEmpty()
            val route: IRoute = when {
                hasNoArguments -> routeMapping.routeClass.createInstance()
                else -> createRoute(routeMapping, ctx, logEntry)
            }
            
            // Now this is a suspend call within the request scope
            sendResponse(ctx, route, logEntry, returnType, webAppConfig.prettyFormatHtml)
            
            logEntry.responseLog.statusCode = ctx.statusCode()
            logEntry.executionTimeInMs = System.currentTimeMillis() - startTime
            webAppConfig.logger.log(logEntry)
        }
        
        // Javalin will wait for this CompletableFuture to complete
        ctx.result(completableFuture)
    }
}
```

### 4. Update SendResponse

Make sendResponse a suspend function:

```kotlin
suspend fun sendResponse(
    ctx: Context,
    route: IRoute,
    logEntry: LogEntry,
    returnType: ReturnTypeEnum,
    prettyFormatHtml: Boolean
) {
    // Now awaits the suspend function
    val response: Any = route.getResponse()
    
    when (returnType) {
        ReturnTypeEnum.HTML -> {
            val formattedHtml = if (prettyFormatHtml) {
                prettyFormatHtml(response as String)
            } else {
                response as String
            }
            ctx.html(formattedHtml)
        }
        ReturnTypeEnum.JSON -> {
            val responseObject = ApiResponse.Success(message = response)
            ctx.json(responseObject)
            logEntry.responseLog.response = responseObject
        }
    }
}
```

### 5. Add Required Dependencies

Ensure these are in pom.xml:
```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx-coroutines-core</artifactId>
    <version>1.7.3</version>
</dependency>
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx-coroutines-jdk8</artifactId>
    <version>1.7.3</version>
</dependency>
```

### 6. Update All Implementations

Update all existing implementations to use suspend:

Example for a simple page element:
```kotlin
class HeaderElement : ObservablePageElement() {
    override suspend fun getResponse(): String {
        return "<header>My Site</header>"  // No actual async needed
    }
}
```

Example for a data-fetching page element:
```kotlin
class UserDataElement(private val userId: Int) : ObservablePageElement() {
    override suspend fun getResponse(): String = coroutineScope {
        // Fetch data in parallel
        val user = async { userDao.getUser(userId) }
        val posts = async { postDao.getUserPosts(userId) }
        
        renderTemplate(user.await(), posts.await())
    }
}
```

Example for an element that needs more time:
```kotlin
class WeatherApiElement : ObservablePageElement() {
    // Override default timeout for slow external API
    override val timeoutMs: Long = 5_000
    
    override suspend fun getResponse(): String {
        val weather = fetchWeatherFromExternalApi()
        return renderWeatherWidget(weather)
    }
}
```

## Testing

1. Create test pages with multiple slow page elements to verify parallel rendering
2. Test error handling when page elements timeout or throw exceptions
3. Verify that the observer pattern still works correctly with async updates
4. Performance test to ensure async overhead is minimal for simple elements

## Structured Concurrency Best Practices

### 1. Scope Hierarchy and Error Isolation

The async implementation follows a clear hierarchy for proper structured concurrency:

```
Request Scope (SupervisorJob)
├── PageBuilder (supervisorScope)
│   ├── Row 1 (supervisorScope) 
│   │   ├── Column 1 (supervisorScope)
│   │   │   ├── Element 1 (with timeout)
│   │   │   └── Element 2 (with timeout)
│   │   └── Column 2 (supervisorScope)
│   └── Row 2 (supervisorScope)
└── [Request completes → All scopes cancelled]
```

### 2. Key Principles

- **Request-scoped lifecycle**: All coroutines are tied to the HTTP request and cancelled when it completes
- **Error isolation**: `supervisorScope` at each level ensures failures don't cascade upwards
- **Timeout handling**: Each page element has individual timeout protection
- **Graceful degradation**: Failed elements show error divs while others continue rendering

### 3. Page Element Internal Parallelism

Page elements can spawn their own child coroutines for internal parallel work:

```kotlin
class ComplexPageElement : ObservablePageElement() {
    override suspend fun getResponse(): String = supervisorScope {
        // Internal parallel data fetching
        val userDataDeferred = async { fetchUserData() }
        val orderHistoryDeferred = async { fetchOrderHistory() }
        val recommendationsDeferred = async { fetchRecommendations() }

        val userData = userDataDeferred.await()
        val orderHistory = orderHistoryDeferred.await()
        val recommendations = recommendationsDeferred.await()

        // Combine data into HTML
        renderTemplate(userData, orderHistory, recommendations)
    }
}
```

### 4. Context Propagation

Ensure request-scoped data (user session, request ID, locale) is properly passed down:

```kotlin
// Pass context through the rendering pipeline
suspend fun getHtml(context: Context): String = supervisorScope {
    // Access request-specific data through context parameter
    val userId = context.sessionAttribute<String>("userId")
    // ... use in rendering
}
```

## Important Considerations

1. **Import statements**: Add these imports where needed:
   ```kotlin
   import kotlinx.coroutines.*
   import kotlinx.coroutines.future.future
   ```

2. **Error handling**: Each page element should handle its own errors gracefully with different strategies for timeouts vs exceptions

3. **Timeouts**: Default timeout is 1_000ms (1 second) to encourage snappy UIs. Elements that need more time (external APIs, complex calculations) should override the `timeoutMs` property.

4. **Backwards compatibility**: This is a breaking change - all existing routes and page elements must be updated

5. **Thread pool management**: Monitor `Dispatchers.IO` and `Dispatchers.Default` thread pool usage under load

6. **Cancellation handling**: Suspend functions are cancellable - ensure proper cleanup in finally blocks if needed

## Success Criteria

- All page elements on a page render in parallel
- No thread blocking with `runBlocking` (except at the very top level if needed)
- Page with 3 elements that each take 100ms should render in ~100ms, not 300ms
- Default timeout of 1_000ms encourages fast page elements
- Elements can override timeout when needed (external APIs, etc.)
- Clean, consistent API using suspend functions throughout
- Existing clean architecture is maintained - each class (PageBuilder, Row, Column) handles its own concerns
- Build succeeds: `mvn compile -DskipTests`
- All tests pass: `mvn test`
