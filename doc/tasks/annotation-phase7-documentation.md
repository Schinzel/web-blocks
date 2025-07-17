# Phase 7: Update Documentation and Create Migration Guides

**Status**: To-Do  
**Priority**: Low  
**Dependencies**: Phase 1-6 (All previous phases)  
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)

## Objective
Update all user documentation to reflect the new annotation-based routing system and ensure developers have clear guidance on using the new system.

## Background
The WebBlocks framework documentation currently describes the interface-based routing system. With the introduction of annotation-based routing, all documentation needs to be updated to:
1. Explain the new annotation-based approach
2. Document the reasoning behind the WebBlock prefix decision
3. Update all examples and tutorials

## Current Documentation to Update

### User Documentation
- `/doc/user_doc/1_getting_started.md` - Update examples to use annotations
- `/doc/user_doc/2_routes.md` - Update route type descriptions
- `/doc/user_doc/4_page_builder.md` - Update block examples if affected
- `/doc/user_doc/7_template_engine.md` - Update if template usage changes
- `/doc/user_doc/8_error_pages.md` - Update if error handling changes
- `/doc/user_doc/10_building_a_jar.md` - Update if build process changes
- `/doc/user_doc/11_custom_response_handlers.md` - Update for new response system

### Sample Documentation
- `/src/main/kotlin/io/schinzel/sample/README.md` - Update all route examples

### Code Standards
- `/doc/code_standards/code_standards_ais.md` - Update with annotation examples

## Implementation Requirements

### 1. Update Getting Started Guide

```markdown
# The first page
## 1 - Create app
Create a package for where your pages, endpoints and so on will reside.
In this package, create a class that extends AbstractWebApp.
This sets the root of the routing.

For example:
```kotlin
class MyWebApp() : AbstractWebApp() {
    override val port: Int = 5555
}
```

Start the project by invoking `start` of your newly created app.

```kotlin
MyWebApp()
    .start()
```

Your project structure should look like:
```
com.mycompany/
└── MyWebApp.kt
```

## 2 - Create your first page
In your new package, create the package `pages`.
All pages will reside in this package. 

Create a package for your first page, `simple_page`.
In this package create a class that uses the `@WebBlockPage` annotation and implements `WebBlockRoute`.

Your project structure should now look like:
```
com.mycompany/
├── MyWebApp.kt
└── pages/
    └── simple_page/
        └── ThePage.kt
```

For example:
```kotlin
import io.schinzel.web_blocks.web.routes.WebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html

@WebBlockPage
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

## 3 - Start the project and request the page
Invoke `MyWebApp().start()`

You should see something along the lines
```
RouteMapping(type='WebBlockPage', path='simple-page', parameters=[])
******************************
Project started on port 5555
******************************
```

In a browser request `http://127.0.0.1:5555/simple-page` which should return "Hello world"

**Note**: The URL path `/simple-page` is automatically generated from your package name `simple_page` (snake_case is converted to kebab-case).

## Annotation-Based Routing System

WebBlocks uses annotations to mark different types of routes:

### @WebBlockPage
Used for HTML pages that users visit in their browser.
- **Location**: Classes in `/pages` directory
- **Returns**: HTML content using `html()` function
- **Content-Type**: `text/html`
- **Path**: Based on directory structure

### @WebBlockApi  
Used for standalone API endpoints that return JSON.
- **Location**: Classes in `/api` directory  
- **Returns**: JSON content using `json()` function
- **Content-Type**: `application/json`
- **Path**: Based on directory structure + class name

### @WebBlockPageApi
Used for page-specific API endpoints (like form submissions).
- **Location**: Classes in `/pages` directory
- **Returns**: JSON content using `json()` function  
- **Content-Type**: `application/json`
- **Path**: Based on directory structure + class name with `/page-api` prefix

## Why WebBlock Prefix?

The WebBlock prefix in annotation names (`@WebBlockPage` vs `@Page`) prevents naming conflicts with other libraries. This ensures your code works reliably even when using multiple frameworks together.
```

### 2. Update Routes Documentation

```markdown
# Routes

There are 3 types of routes in WebBlocks:
- Page route - Returns HTML for web pages
- API route - Returns JSON for standalone API endpoints  
- Page API route - Returns JSON for page-specific operations

All routes use the unified `WebBlockRoute` interface with annotations to specify the route type.

## Route Types

### Page Routes

| Attribute  | Description                           |
|------------|---------------------------------------|
| Annotation | `@WebBlockPage`                       |
| Interface  | `WebBlockRoute`                       |
| Returns    | `WebBlockResponse` (typically `HtmlResponse`) |
| Location   | Located in the `pages` directory      |

| Property     | Description                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Path         | The path is decided by the directory structure                                    |
| Case         | Directory names are converted from snake_case to kebab-case                       |
| Special case | A page that resides in the directory `landing` will be served as the root page |
| Example      | `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page`        |

```kotlin
@WebBlockPage
class ThePage : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
}
```

### API Routes

| Attribute  | Description                           |
|------------|---------------------------------------|
| Annotation | `@WebBlockApi`                        |
| Interface  | `WebBlockRoute`                       |
| Returns    | `WebBlockResponse` (typically `JsonResponse`) |
| Location   | Located in the `api` directory        |

| Property         | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name           |
| Case             | Directory names are converted from snake_case to kebab-case                 |
| Case             | Class names are converted from PascalCase to kebab-case                     |
| Suffixes removed | Suffixes `Route` are removed                                                |
| Prefix           | Api paths are prefixed with `api`                                           |
| Example          | `/api/my_dir/MyPersonRoute.kt` will receive the path `api/my-dir/my-person` |

```kotlin
@WebBlockApi
class UserPets : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
}
```

### Page API Routes

| Attribute  | Description                           |
|------------|---------------------------------------|
| Annotation | `@WebBlockPageApi`                    |
| Interface  | `WebBlockRoute`                       |
| Returns    | `WebBlockResponse` (typically `JsonResponse`) |
| Location   | Located in the `pages` directory      |

| Property         | Description                                                                                                               |
|------------------|---------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                         |
| Case             | Directory names are converted from snake_case to kebab-case                                                               |
| Case             | Class names are converted from PascalCase to kebab-case                                                                   |
| Suffixes removed | Suffixes `Route` are removed                                                                                               |
| Prefix           | Prefixed with `page-api`                                                                                                  |
| Example          | `/pages/user_pages/settings/SavePersonNameRoute.kt` will receive the path `page-api/user-pages/settings/save-person-name` |

```kotlin
@WebBlockPageApi
class SavePersonNameRoute : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(mapOf("success" to true))
}
```


## Parameters
Arguments to routes can be passed as:
- Query parameters
- Request body
- These are stated as constructor arguments that are declared as `val` and non-private (public)
- Parameters are converted from camelCase to kebab-case

## Status Code Control
The framework and implementing classes share responsibility for HTTP status codes. When a route successfully executes and returns a WebBlockResponse, the implementing class has final authority over the status code.

| Scenario | Who Controls | Status Code | Example |
|----------|-------------|-------------|---------|
| Route not found | Framework | 404 | `GET /does-not-exist` |
| Uncaught exception in route | Framework | 500 | Route throws `RuntimeException` |
| Parameter parsing fails | Framework | 400 | Invalid query parameter format |
| Request body invalid | Framework | 400 | Malformed JSON in POST body |
| Route returns response | **Implementing Class** | **Any** | `JsonResponse(data, status = 201)` |
```


### 4. Update Sample Documentation

```markdown
# Sample

Below directories are relative to this README file.
Start the main file [MyWebApp.kt](MyWebApp.kt).

## Page Routes

A simple HTML page using the new annotation-based system.
- [pages/simple_page/ThePage.kt](pages/simple_page/ThePage.kt)
- http://127.0.0.1:5555/simple-page
- Uses `@WebBlockPage` annotation

Landing page. Returns the page in the directory `landing`.
- [pages/landing/LandingPage.kt](pages/landing/LandingPage.kt)
- http://127.0.0.1:5555/
- Uses `@WebBlockPage` annotation

A page with one simple block.
- [pages/page_with_block/ThePage.kt](pages/page_with_block/ThePage.kt)
- http://127.0.0.1:5555/page-with-block?user-id=123222
- Uses `@WebBlockPage` annotation

A page with three blocks and a page API route. 
Two of the blocks observe one block. 
This means if the observed block changes, the observing blocks update themselves.
- [pages/page_with_blocks_and_page_api_route/WelcomePage.kt](pages/page_with_blocks_and_page_api_route/WelcomePage.kt)
- http://127.0.0.1:5555/page-with-blocks-and-page-api-route?user-id=123222
- Uses `@WebBlockPage` annotation

A page with custom status code (201) using HtmlResponse.
- [pages/page_with_custom_status/ThePageWithStatus.kt](pages/page_with_custom_status/ThePageWithStatus.kt)
- http://127.0.0.1:5555/page-with-custom-status
- Uses `@WebBlockPage` annotation

A page with custom headers using HtmlResponse.
- [pages/page_with_headers/ThePageWithHeaders.kt](pages/page_with_headers/ThePageWithHeaders.kt)
- http://127.0.0.1:5555/page-with-headers
- Uses `@WebBlockPage` annotation

A page demonstrating Java-friendly builder usage.
- [pages/java_style_page/JavaStylePage.kt](pages/java_style_page/JavaStylePage.kt)
- http://127.0.0.1:5555/java-style-page
- Uses `@WebBlockPage` annotation

## API Routes

A simple API route using the new annotation-based system.
- [api/UserPets.kt](api/UserPets.kt)
- http://127.0.0.1:5555/api/user-pets
- Uses `@WebBlockApi` annotation

An API route with one parameter.
- [api/UserInformationEndpoint.kt](api/UserInformationEndpoint.kt)
- http://127.0.0.1:5555/api/user-information-endpoint?user-id=123
- Uses `@WebBlockApi` annotation

An API route that throws an error.
- [api/ApiRouteThatThrowsError.kt](api/ApiRouteThatThrowsError.kt)
- http://127.0.0.1:5555/api/api-route-that-throws-error
- Uses `@WebBlockApi` annotation

An API route with custom headers using JsonResponse.
- [api/UserApiWithHeaders.kt](api/UserApiWithHeaders.kt)
- http://127.0.0.1:5555/api/user-api-with-headers?user-id=123
- Uses `@WebBlockApi` annotation

## Page API Routes

A page API route for updating user names.
- [pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt](pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt)
- http://127.0.0.1:5555/page-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name
- Uses `@WebBlockPageApi` annotation

## Annotation-Based Routing System

All sample routes now use the unified `WebBlockRoute` interface with annotations to specify route types:

- **@WebBlockPage**: For HTML pages that users visit
- **@WebBlockApi**: For standalone JSON API endpoints
- **@WebBlockPageApi**: For page-specific JSON API endpoints

This provides a consistent interface while maintaining clear type identification through annotations.
```

### 5. Update Code Standards for AIs

```markdown
# Code Standards for AIs

## Annotation-Based Routing Examples

When creating routes in WebBlocks, use the new annotation-based system:

### Page Routes
```kotlin
@WebBlockPage
class UserProfile : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = html("""
        <h1>User Profile</h1>
        <p>Welcome to your profile page.</p>
    """)
}
```

### API Routes
```kotlin
@WebBlockApi
class UserApi : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(mapOf(
        "id" to 123,
        "name" to "John Doe",
        "email" to "john@example.com"
    ))
}
```

### Page API Routes
```kotlin
@WebBlockPageApi
class SaveUserDataRoute : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(mapOf(
        "success" to true,
        "message" to "Data saved successfully"
    ))
}
```

## Key Points for AIs

1. **Always use annotations**: Every route must have exactly one WebBlock annotation
2. **Single interface**: All routes implement `WebBlockRoute`
3. **Response type matching**: 
   - `@WebBlockPage` → use `html()` or `HtmlResponse`
   - `@WebBlockApi` → use `json()` or `JsonResponse`
   - `@WebBlockPageApi` → use `json()` or `JsonResponse`
4. **Path generation**: Paths are generated from file structure, not annotations
5. **WebBlock prefix**: Always use the WebBlock prefix to avoid naming conflicts

## Key Points for AIs

Always:
1. Add appropriate annotation
2. Use `WebBlockRoute` interface
3. Update imports
4. Verify response type matches annotation
```

## Documentation File Structure
```
doc/
├── user_doc/
│   ├── 1_getting_started.md (updated)
│   ├── 2_routes.md (updated)
│   ├── 4_page_builder.md (updated if needed)
│   ├── 7_template_engine.md (updated if needed)
│   ├── 8_error_pages.md (updated if needed)
│   ├── 10_building_a_jar.md (updated if needed)
│   └── 11_custom_response_handlers.md (updated)
├── code_standards/
│   └── code_standards_ais.md (updated)
└── tasks/
    └── annotation-implementation-overview.md (updated)

src/main/kotlin/io/schinzel/sample/
└── README.md (updated)
```

## Acceptance Criteria
- [ ] All user documentation updated to show annotation-based examples
- [ ] Sample documentation updated with annotation examples
- [ ] Code standards updated for AIs
- [ ] All documentation follows WebBlocks documentation standards
- [ ] Examples are accurate and tested
- [ ] Documentation explains WebBlock prefix reasoning
- [ ] All links and references updated
- [ ] Documentation is clear and comprehensive

## Documentation Standards
- Use US English throughout
- Include code examples for all concepts
- Clear section headers and organization
- Consistent formatting and styling
- Include troubleshooting information
- Link between related documentation sections

## Testing Documentation
- [ ] All code examples in documentation compile correctly
- [ ] All URLs and paths in examples are accurate
- [ ] Sample routes match documentation descriptions
- [ ] All imports in examples are correct

## Documentation Review Process
1. **Technical Review**: Verify all code examples work
2. **Content Review**: Check clarity and completeness
3. **Final Review**: Ensure consistency across all docs

## Integration with Other Documentation
- [ ] Main README.md links to new annotation documentation
- [ ] Getting started guide flows naturally to route creation
- [ ] Code standards reference annotation examples
- [ ] Sample documentation aligns with user documentation

## Future Documentation Considerations
- Plan for future annotation parameters (e.g., `@WebBlockPage(cacheable = true)`)
- Plan for additional language examples (Java, Scala) if needed
- Consider creating video tutorials or interactive guides

## Dependencies
- **Phase 1-6**: All implementation phases must be complete
- **Phase 5**: Sample routes must be migrated before documenting
- **Testing**: All functionality must be tested before documenting
- **Code Review**: All code must be reviewed before documenting

## Notes
- Documentation should be updated incrementally with implementation
- Ensure documentation is accessible to developers of all skill levels
- Plan for community feedback and documentation improvements