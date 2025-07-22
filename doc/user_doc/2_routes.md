# Routes

There are 3 types of routes:

- API route - a standalone route that returns JSON.
- Page route - an HTML-page which returns HTML.
- Page Block route - a block (i.e. component) that is a part of a page that returns HTML.
- Page Block Api route - a route that typically is used for CRUD operation by a Page block. Returns JSON.

## API route

| Attribute  | Description                    |
|------------|--------------------------------|
| Annotation | `@Api`                         |
| Interface  | `IJsonRoute`                   |
| Returns    | `IJsonResponse`                |
| Location   | Located in the `api` directory |

| Property         | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name           |
| Case             | Directory names are converted from snake_case to kebab-case                 |
| Case             | Class names are converted from PascalCase to kebab-case                     |
| Suffixes removed | Suffixes `JsonRoute`, `Route`, `API` and `Api` are removed                  |
| Prefix           | Api paths are prefixed with `api`                                           |
| Example          | `/api/my_dir/MyPersonRoute.kt` will receive the path `api/my-dir/my-person` |

```kotlin
@WebBlockApi
class UserPets : IJsonRoute {
  override suspend fun getResponse(): IJsonResponse = json(listOf("cat", "dog"))
}
```

## Page route

| Attribute  | Description                      |
|------------|----------------------------------|
| Annotation | `@Page`                          |
| Interface  | `IHtmlRoute`                     |
| Returns    | `IHtmlResponse`                  |
| Location   | Located in the `pages` directory |

| Property     | Description                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Path         | The path is decided by the directory structure                                    |
| Case         | Directory names are converted from snake_case to kebab-case                       |
| Special case | A that page resides in the directory `landing` it will be served as the root page |
| Example      | `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page`        |

```kotlin
@WebBlockPage
class ThePage : IHtmlRoute {
  override suspend fun getResponse(): IHtmlResponse = html("<h1>Hello</h1>")
}
```

## Page Block route

| Attribute  | Description                      |
|------------|----------------------------------|
| Annotation | `@PageBlock`                     |
| Interface  | `IHtmlRoute`                     |
| Returns    | `IHtmlResponse`                  |
| Location   | Located in the `pages` directory |

| Property         | Description                                                                                                                 |
|------------------|-----------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                           |
| Case             | Directory names are converted from snake_case to kebab-case                                                                 |
| Case             | Class names are converted from PascalCase to kebab-case                                                                     |
| Suffixes removed | Suffixes `PageBlock`, `Pb`, `PB` and `Block` are removed                                                                    |
| Prefix           | Prefixed with `page-block`                                                                                                  |
| Example          | `/pages/user_pages/settings/SavePersonNameRoute.kt` will receive the path `page-block/user-pages/settings/save-person-name` |

```kotlin
@PageBlock
class ThePageBlock : IHtmlRoute {
  override suspend fun getResponse(): IHtmlResponse = html("<h1>Hello</h1>")
}
```

## Page Block API route

These are assume to be tied to pages as opposed to being standalone endpoints like the API routes.
Used by blocks typically for CRUD operations.

| Attribute  | Description                      |
|------------|----------------------------------|
| Annotation | `@PageBlockApi`                  |
| Interface  | `IJsonRoute`                     |
| Returns    | `IJsonResponse`                  |
| Location   | Located in the `pages` directory |

| Property         | Description                                                                                                                     |
|------------------|---------------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                               |
| Case             | Directory names are converted from snake_case to kebab-case                                                                     |
| Case             | Class names are converted from PascalCase to kebab-case                                                                         |
| Suffixes removed | Suffixes `PageBlockApi`and `Api` are removed                                                                                    |
| Prefix           | Prefixed with `page-block-api`                                                                                                  |
| Example          | `/pages/user_pages/settings/SavePersonNameRoute.kt` will receive the path `page-block-api/user-pages/settings/save-person-name` |

```kotlin
@PageBlockApi
class ThePageBlockApi : IJsonRoute {
  override suspend fun getResponse(): IJsonResponse = json(listOf("cat", "dog"))
}
```

## Parameters

Arguments to pages, api endpoints and page endpoints can be passed as:

- Query parameters
- Request body
- These are stated a constructor arguments that are declared as `val` and non-private (public)
- Parameters are converted from camelCase to kebab-case

## Example
Below is an example of a page with two page blocks.
```
com.mycompany/
├── MyWebApp.kt
└── pages/
    └── simple_page/
        ├── ThePage.kt
        └── blocks/
            │
            ├── my_first_block/
            │   ├── MyFirstBlock.kt
            │   ├── TheTemplate.html
            │   ├── TheDto.kt
            │   └── TheDao.kt
            │
            └── my_second_block/
                ├── MySecondBlock.kt
                ├── MyTemplate.html
                ├── MyDto.kt
                ├── MyDao.kt
                └── SomeOtherCode.kt
```

## Status Code Control

The framework and implementing classes share responsibility for HTTP status codes. When a route successfully executes
and returns a response, the implementing class has final authority over the status code.

| Scenario                    | Who Controls           | Status Code | Example                            |
|-----------------------------|------------------------|-------------|------------------------------------|
| Route not found             | Framework              | 404         | `GET /does-not-exist`              |
| Uncaught exception in route | Framework              | 500         | Route throws `RuntimeException`    |
| Parameter parsing fails     | Framework              | 400         | Invalid query parameter format     |
| Request body invalid        | Framework              | 400         | Malformed JSON in POST body        |
| Route returns response      | **Implementing Class** | **Any**     | `JsonResponse(data, status = 201)` |
