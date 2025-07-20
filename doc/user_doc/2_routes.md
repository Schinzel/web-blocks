# Routes

There are 5 types of routes:
- API route - a standalone route that returns JSON
- Page route - an HTML-page which returns HTML
- Page API route - an route that belongs to an HTML-page and returns JSON
- WebBlock route - a standalone HTML component that can be embedded in pages
- WebBlock API route - an API route that serves WebBlock components and returns JSON

## API route

| Attribute  | Description                                   |
|------------|-----------------------------------------------|
| Annotation | `@Api`                                        |
| Interface  | `IApiRoute`                                   |
| Returns    | `IJsonResponse`                               |
| Location   | Located in the `api` directory                |

| Property         | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name           |
| Case             | Directory names are converted from snake_case to kebab-case                 |
| Case             | Class names are converted from PascalCase to kebab-case                     |
| Suffixes removed | Suffixes `Route` are removed                                                |
| Prefix           | Api paths are prefixed with `api`                                           |
| Example          | `/api/my_dir/MyPersonRoute.kt` will receive the path `api/my-dir/my-person` |

```kotlin
@Api
class UserPets : IApiRoute {
    override suspend fun getResponse(): IJsonResponse = json(listOf("cat", "dog"))
}
```

## Page route

| Attribute  | Description                                   |
|------------|-----------------------------------------------|
| Annotation | `@Page`                                       |
| Interface  | `IHtmlRoute`                                  |
| Returns    | `IHtmlResponse`                               |
| Location   | Located in the `pages` directory              |

| Property     | Description                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Path         | The path is decided by the directory structure                                    |
| Case         | Directory names are converted from snake_case to kebab-case                       |
| Special case | A that page resides in the directory `landing` it will be served as the root page |
| Example      | `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page`        |

```kotlin
@Page
class ThePage : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse = html("<h1>Hello</h1>")
}
```


## WebBlock route

Standalone HTML components that can be embedded in pages. These are reusable UI components 
that return HTML and can update themselves independently.

| Attribute  | Description                           |
|------------|---------------------------------------|
| Annotation | `@WebBlock`                           |
| Interface  | `IHtmlRoute`                          |
| Returns    | `IHtmlResponse`                       |
| Location   | Located in the `pages` directory      |

| Property         | Description                                                                                                               |
|------------------|---------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                         |
| Case             | Directory names are converted from snake_case to kebab-case                                                               |
| Case             | Class names are converted from PascalCase to kebab-case                                                                   |
| Suffixes removed | No suffixes are removed (full class name is used)                                                                         |
| Prefix           | Prefixed with `web-block`                                                                                                 |
| Example          | `/pages/user_profile/blocks/avatar_block/AvatarBlock.kt` will receive the path `web-block/user-profile/blocks/avatar-block/avatar-block` |

```kotlin
@WebBlock
class AvatarBlock(val userId: Int) : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse = html("<div>Avatar for user $userId</div>")
}
```

## WebBlock API route

API routes that serve WebBlock components. These handle CRUD operations, form submissions, 
and AJAX requests for WebBlock components.

| Attribute  | Description                           |
|------------|---------------------------------------|
| Annotation | `@WebBlockApi`                        |
| Interface  | `IApiRoute`                           |
| Returns    | `IJsonResponse`                       |
| Location   | Located in the `pages` directory      |

| Property         | Description                                                                                                               |
|------------------|---------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                         |
| Case             | Directory names are converted from snake_case to kebab-case                                                               |
| Case             | Class names are converted from PascalCase to kebab-case                                                                   |
| Suffixes removed | Suffixes `Route` are removed                                                                                              |
| Prefix           | Prefixed with `web-block-api`                                                                                             |
| Example          | `/pages/user_profile/blocks/avatar_block/UpdateAvatarRoute.kt` will receive the path `web-block-api/user-profile/blocks/avatar-block/update-avatar` |

```kotlin
@WebBlockApi
class UpdateAvatarRoute(val userId: Int, val avatarUrl: String) : IApiRoute {
    override suspend fun getResponse(): IJsonResponse = json(mapOf("success" to true))
}
```


## Parameters
Arguments to pages, api endpoints and page endpoints can be passed as:
- Query parameters
- Request body
- These are stated a constructor arguments that are declared as `val` and non-private (public)
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
