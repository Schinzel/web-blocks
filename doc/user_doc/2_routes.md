# Routes

There are 3 types of routes:
- API route - a standalone route that returns JSON
- Page route - an HTML-page which returns HTML
- Page API route - an route that belongs to an HTML-page and returns JSON

## API route

| Attribute  | Description                                   |
|------------|-----------------------------------------------|
| Annotation | `@Api`                                        |
| Interface  | `WebBlockRoute`                               |
| Returns    | `WebBlockResponse` (typically `JsonResponse`) |
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
@WebBlockApi
class UserPets : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
}
```

## Page route

| Attribute  | Description                                   |
|------------|-----------------------------------------------|
| Annotation | `@Page`                                       |
| Interface  | `WebBlockRoute`                               |
| Returns    | `WebBlockResponse` (typically `HtmlResponse`) |
| Location   | Located in the `pages` directory              |

| Property     | Description                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Path         | The path is decided by the directory structure                                    |
| Case         | Directory names are converted from snake_case to kebab-case                       |
| Special case | A that page resides in the directory `landing` it will be served as the root page |
| Example      | `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page`        |

```kotlin
@WebBlockPage
class ThePage : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = html("<h1>Hello</h1>")
}
```

## Page API route

These are assume to be tied to pages as opposed to being standalone endpoints like the API routes.
Used by pages to for example save data or update an element on a the page.

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
| Suffixes removed | Suffixes `Route`are removed                                                                                               |
| Prefix           | Prefixed with `page-api`                                                                                                  |
| Example          | `/pages/user_pages/settings/SavePersonNameRoute.kt` will receive the path `page-api/user-pages/settings/save-person-name` |

```kotlin
@WebBlockPageApi
class SavePersonNameRoute : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(mapOf("success" to true))
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
