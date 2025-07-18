# Phase 5: Convert Sample Routes to Annotation System

**Status**: ✅ Complete  
**Priority**: Medium  
**Dependencies**: Phase 1-4 (All previous phases)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)
**Completed**: 2025-07-18

## Objective
Convert all sample routes in the WebBlocks framework from the interface-based system to the new annotation-based system while ensuring all functionality remains unchanged and URL paths stay the same.

## Background
The sample application contains various route types that demonstrate WebBlocks functionality:
- Page routes in `/pages` directory
- API routes in `/api` directory  
- Page API routes in `/pages` directory
- Each route currently implements one of: `IPageRoute`, `IApiRoute`, `IPageApiRoute`

## Current Sample Routes Analysis

### Page Routes
- `/pages/simple_page/ThePage.kt` → `/simple-page`
- `/pages/landing/LandingPage.kt` → `/` (root)
- `/pages/page_with_block/ThePage.kt` → `/page-with-block`
- `/pages/page_with_blocks_and_page_api_route/WelcomePage.kt` → `/page-with-blocks-and-page-api-route`
- `/pages/page_with_custom_status/ThePageWithStatus.kt` → `/page-with-custom-status`
- `/pages/page_with_headers/ThePageWithHeaders.kt` → `/page-with-headers`
- `/pages/java_style_page/JavaStylePage.kt` → `/java-style-page`

### API Routes
- `/api/UserPets.kt` → `/api/user-pets`
- `/api/UserInformationEndpoint.kt` → `/api/user-information-endpoint`
- `/api/ApiRouteThatThrowsError.kt` → `/api/api-route-that-throws-error`
- `/api/UserApiWithHeaders.kt` → `/api/user-api-with-headers`

### Page API Routes
- `/pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt` → `/page-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name`

## Implementation Requirements

### 1. Update Page Routes

#### Simple Page
```kotlin
// Before: /pages/simple_page/ThePage.kt
package io.schinzel.sample.pages.simple_page

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class ThePage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return html("""
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Hello World</title>
           |</head>
           |<body>
           |    <h1>Hello World</h1>
           |</body>
           |</html>
        """.trimMargin())
    }
}

// After: /pages/simple_page/ThePage.kt
package io.schinzel.sample.pages.simple_page

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

@WebBlockPage
@Suppress("unused")
class ThePage : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return html("""
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Hello World</title>
           |</head>
           |<body>
           |    <h1>Hello World</h1>
           |</body>
           |</html>
        """.trimMargin())
    }
}
```

#### Page with Custom Status
```kotlin
// Before: /pages/page_with_custom_status/ThePageWithStatus.kt
package io.schinzel.sample.pages.page_with_custom_status

import io.schinzel.web_blocks.web.response.HtmlResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class ThePageWithStatus : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return HtmlResponse(
            content = """
                |<!DOCTYPE html>
                |<html lang="en">
                |<head>
                |    <title>Custom Status Page</title>
                |</head>
                |<body>
                |    <h1>Page with Custom Status Code</h1>
                |    <p>This page returns a 201 Created status code.</p>
                |</body>
                |</html>
            """.trimMargin(),
            status = 201
        )
    }
}

// After: /pages/page_with_custom_status/ThePageWithStatus.kt
package io.schinzel.sample.pages.page_with_custom_status

import io.schinzel.web_blocks.web.response.HtmlResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

@WebBlockPage
@Suppress("unused")
class ThePageWithStatus : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return HtmlResponse(
            content = """
                |<!DOCTYPE html>
                |<html lang="en">
                |<head>
                |    <title>Custom Status Page</title>
                |</head>
                |<body>
                |    <h1>Page with Custom Status Code</h1>
                |    <p>This page returns a 201 Created status code.</p>
                |</body>
                |</html>
            """.trimMargin(),
            status = 201
        )
    }
}
```

### 2. Update API Routes

#### Simple API Route
```kotlin
// Before: /api/UserPets.kt
package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class UserPets : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return json(listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        ))
    }

    data class Pet(val name: String, val type: String)
}

// After: /api/UserPets.kt
package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi

@WebBlockApi
@Suppress("unused")
class UserPets : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return json(listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        ))
    }

    data class Pet(val name: String, val type: String)
}
```

#### API Route with Headers
```kotlin
// Before: /api/UserApiWithHeaders.kt
package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.JsonResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class UserApiWithHeaders(private val userId: String) : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return JsonResponse(
            data = mapOf(
                "userId" to userId,
                "name" to "John Doe",
                "email" to "john@example.com"
            ),
            headers = mapOf(
                "X-Custom-Header" to "Custom Value",
                "X-Request-ID" to "req-123456"
            )
        )
    }
}

// After: /api/UserApiWithHeaders.kt
package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.JsonResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi

@WebBlockApi
@Suppress("unused")
class UserApiWithHeaders(private val userId: String) : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return JsonResponse(
            data = mapOf(
                "userId" to userId,
                "name" to "John Doe",
                "email" to "john@example.com"
            ),
            headers = mapOf(
                "X-Custom-Header" to "Custom Value",
                "X-Request-ID" to "req-123456"
            )
        )
    }
}
```

### 3. Update Page API Routes

#### Page API Route
```kotlin
// Before: /pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt
package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.IPageApiRoute

@Suppress("unused")
class UpdateFirstNameRoute(private val firstName: String) : IPageApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        // Update the name in the DAO
        NameDao.updateFirstName(firstName)
        
        return json(mapOf(
            "success" to true,
            "message" to "Name updated successfully",
            "newName" to firstName
        ))
    }
}

// After: /pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt
package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi

@WebBlockPageApi
@Suppress("unused")
class UpdateFirstNameRoute(private val firstName: String) : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse {
        // Update the name in the DAO
        NameDao.updateFirstName(firstName)
        
        return json(mapOf(
            "success" to true,
            "message" to "Name updated successfully",
            "newName" to firstName
        ))
    }
}
```

## Migration Checklist

### Page Routes to Convert
- [x] `/pages/simple_page/ThePage.kt` → `@WebBlockPage`
- [x] `/pages/landing/LandingPage.kt` → `@WebBlockPage`
- [x] `/pages/page_with_block/ThePage.kt` → `@WebBlockPage`
- [x] `/pages/page_with_blocks_and_page_api_route/WelcomePage.kt` → `@WebBlockPage`
- [x] `/pages/page_with_custom_status/ThePageWithStatus.kt` → `@WebBlockPage`
- [x] `/pages/page_with_headers/ThePageWithHeaders.kt` → `@WebBlockPage`
- [x] `/pages/java_style_page/JavaStylePage.kt` → `@WebBlockPage`

### API Routes to Convert
- [x] `/api/UserPets.kt` → `@WebBlockApi`
- [x] `/api/UserInformationEndpoint.kt` → `@WebBlockApi`
- [x] `/api/ApiRouteThatThrowsError.kt` → `@WebBlockApi`
- [x] `/api/UserApiWithHeaders.kt` → `@WebBlockApi`

### Page API Routes to Convert
- [x] `/pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt` → `@WebBlockPageApi`

## Migration Steps

### For Each Route:
1. **Add annotation import**: Import the appropriate annotation
2. **Add annotation**: Add `@WebBlockPage`, `@WebBlockApi`, or `@WebBlockPageApi`
3. **Update interface**: Change from `IPageRoute`/`IApiRoute`/`IPageApiRoute` to `WebBlockRoute`
4. **Update imports**: Remove old interface import, add new interface import
5. **Verify functionality**: Test that the route works identically
6. **Verify path**: Ensure URL path remains unchanged

### General Migration Pattern:
```kotlin
// Before
import io.schinzel.web_blocks.web.routes.IPageRoute

class MyRoute : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse = ...
}

// After  
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

@WebBlockPage
class MyRoute : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = ...
}
```

## Testing Requirements

### Functional Testing
- [x] All converted routes return the same responses as before
- [x] All URL paths remain unchanged
- [x] Custom status codes preserved
- [x] Custom headers preserved
- [x] Parameter handling works identically
- [x] Error handling works identically

### Integration Testing
- [x] Routes discovered by new annotation-based system
- [x] Content-Type headers set correctly
- [x] Web application starts without errors
- [x] All sample demonstrations work

### Regression Testing
- [x] Compare before/after responses for each route
- [x] Verify no breaking changes in API contracts
- [x] Test with various parameter combinations
- [x] Verify block system still works with page API routes

## Acceptance Criteria
- [x] All sample routes converted to annotation system
- [x] All routes use `IWebBlockRoute` interface
- [x] Appropriate annotations added to all routes
- [x] No changes to response content or behavior
- [x] All URL paths remain identical
- [x] Custom status codes and headers preserved
- [x] All imports updated correctly
- [x] Code follows WebBlocks coding standards
- [x] No compilation errors
- [x] All tests pass

## Validation Steps

### Before Migration
1. **Document current behavior**: Record response content, headers, status codes
2. **Test all endpoints**: Verify all routes work correctly
3. **Record URL mappings**: Document current path mappings

### After Migration
1. **Verify identical behavior**: Compare responses with documented behavior
2. **Test all endpoints**: Ensure all routes still work
3. **Verify URL mappings**: Ensure paths remain unchanged
4. **Test edge cases**: Parameter handling, error cases, etc.

## Risk Assessment

### Low Risk
- Simple routes with no parameters
- Routes using basic `html()` and `json()` functions
- Routes with standard responses

### Medium Risk
- Routes with custom status codes
- Routes with custom headers
- Routes with constructor parameters

### High Risk
- Routes with complex parameter handling
- Routes with special business logic
- Page API routes with observer patterns

## Rollback Plan
If issues arise during migration:
1. **Revert individual routes**: Change back to interface-based system
2. **Maintain dual support**: Keep both systems running
3. **Fix issues before continuing**: Address problems before proceeding
4. **Document issues**: Record any unexpected behavior

## JVM Language Compatibility
- Annotations work identically across Kotlin, Java, Scala, Clojure
- No Kotlin-specific features used in migration
- Interface changes are compatible with all JVM languages
- Error messages clear across all languages

## Code Standards Compliance
- Follow all standards in `doc/code_standards/`
- Maintain existing code structure and formatting
- Update imports cleanly
- Preserve existing documentation
- Keep classes under 250 lines
- Keep functions under 10 lines

## Dependencies
- **Phase 1**: Requires annotation classes
- **Phase 2**: Requires `WebBlockRoute` interface
- **Phase 3**: Requires updated route discovery
- **Phase 4**: Requires updated response processing
- **Sample App**: All sample routes must be converted

## Notes
- Migration should be done incrementally
- Test each route after conversion
- Maintain identical functionality
- Document any unexpected behavior
- URL paths must remain unchanged
- Custom headers and status codes must be preserved