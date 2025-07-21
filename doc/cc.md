
Ta bort gamla tasks

IApiRoute
IHtmlRoute
Is this consistent

Om tog bort getPath från IRoute. Kan vi då göra auto doc?


modules/logic/db/src/test/kotlin/
├── unit/                    # Mocked repository tests
│   └── WorkflowServiceTest.kt
├── integration/             # Real database tests
│   ├── WorkflowDaoTest.kt
│   └── DatabaseTestBase.kt  # Shared setup
└── fixtures/                # Test data builders
└── WorkflowFixtures.kt

Mermaid diagram, from request to response.




# Redesign
Summary of ZenMCP Discussion 🎯
After thorough architectural analysis, Option B is clearly superior. Here's the recommended approach:

## Core Design:
```kotlin
// HTML domain responses
sealed interface HtmlFlowResponse : WebBlockResponse
data class HtmlContentResponse(val content: String) : HtmlFlowResponse
data class RedirectResponse(val location: String) : HtmlFlowResponse
data class HtmlErrorResponse(val statusCode: Int, val message: String) : HtmlFlowResponse

// API domain responses
sealed interface JsonResponse : WebBlockResponse
data class SuccessJsonResponse(val data: Any) : JsonResponse
data class ErrorJsonResponse(val statusCode: Int, val code: String, val message: String) : JsonResponse
```

## Response Types:
```kotlin
// HTML domain responses
sealed interface HtmlFlowResponse : WebBlockResponse
data class HtmlContentResponse(val content: String) : HtmlFlowResponse
data class RedirectResponse(val location: String) : HtmlFlowResponse
data class HtmlErrorResponse(val statusCode: Int, val message: String) : HtmlFlowResponse

// API domain responses
sealed interface JsonResponse : WebBlockResponse
data class SuccessJsonResponse(val data: Any) : JsonResponse
data class ErrorJsonResponse(val statusCode: Int, val code: String, val message: String) : JsonResponse
```

Key Benefits ✅:
- Compile-time type safety - Pages can't accidentally return JSON
- JVM language compatibility - Works with Java, Scala, Clojure
- Clear contracts - Interface names match expected behavior
- Defensive programming - Catches errors at compile time
- Low barrier to entry - IDE guides correct usage

Edge Cases Handled:
- Form submissions: RedirectResponse in HtmlFlowResponse
- AJAX from pages: Separate IApiRoute endpoints
- PageApi routes: Implement IApiRoute (they return JSON)
- Error handling: Domain-specific error types
