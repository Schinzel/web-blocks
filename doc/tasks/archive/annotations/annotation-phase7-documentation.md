# Phase 7: Update Documentation and Create Migration Guides

**Status**: ✅ Complete
**Priority**: Low
**Dependencies**: Phase 1-6 (All previous phases)
**Overview**: [annotation-implementation-overview.md](annotation-implementation-overview.md)
**Completed**: 2025-07-18


## Current Documentation to Update

### User Documentation
The only document is the Route document:
- `/doc/user_doc/2_routes.md` - Update route type descriptions

## Routes

API Routes

### API Routes


| Attribute  | Description                                   |
| ---------- | --------------------------------------------- |
| Annotation | `@WebBlockApi`                                |
| Interface  | `WebBlockRoute`                               |
| Returns    | `WebBlockResponse` (typically `JsonResponse`) |
| Location   | Located in the`api` directory                 |


| Property         | Description                                                                 |
| ---------------- | --------------------------------------------------------------------------- |
| Path             | The path is decided by the directory structure and the Class name           |
| Case             | Directory names are converted from snake_case to kebab-case                 |
| Case             | Class names are converted from PascalCase to kebab-case                     |
| Suffixes removed | Suffixes`Route` are removed                                                 |
| Prefix           | Api paths are prefixed with`api`                                            |
| Example          | `/api/my_dir/MyPersonRoute.kt` will receive the path `api/my-dir/my-person` |

```kotlin
@WebBlockApi
class UserPets : WebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = json(listOf("cat", "dog"))
}
```

### Page API Routes


| Attribute  | Description                                   |
| ---------- | --------------------------------------------- |
| Annotation | `@WebBlockPageApi`                            |
| Interface  | `WebBlockRoute`                               |
| Returns    | `WebBlockResponse` (typically `JsonResponse`) |
| Location   | Located in the`pages` directory               |


| Property         | Description                                                                                                               |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Path             | The path is decided by the directory structure and the Class name                                                         |
| Case             | Directory names are converted from snake_case to kebab-case                                                               |
| Case             | Class names are converted from PascalCase to kebab-case                                                                   |
| Suffixes removed | Suffixes`Route` are removed                                                                                               |
| Prefix           | Prefixed with`page-api`                                                                                                   |
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

The framework and implementing classes share responsibility for HTTP status codes.
When a route successfully executes and returns a WebBlockResponse, the implementing class has final authority over the status code.


| Scenario                    | Who Controls           | Status Code | Example                            |
| --------------------------- | ---------------------- | ----------- | ---------------------------------- |
| Route not found             | Framework              | 404         | `GET /does-not-exist`              |
| Uncaught exception in route | Framework              | 500         | Route throws`RuntimeException`     |
| Parameter parsing fails     | Framework              | 400         | Invalid query parameter format     |
| Request body invalid        | Framework              | 400         | Malformed JSON in POST body        |
| Route returns response      | **Implementing Class** | **Any**     | `JsonResponse(data, status = 201)` |



