# Web Framework
## Features
- File-based routing system.
- Each endpoint supports both GET and POST requests and the arguments can be passed as:
    - Query parameters
    - Request body

## Benefits
The framework uses a file-system based routing approach where the
application's URL structure directly mirrors its folder structure.
This intuitive system:
- Eliminates the need for manual route configuration
- Makes the application structure immediately clear to new developers
- Simplifies navigation and routing management
- Provides a predictable pattern for adding new pages

## Routes
Each route is set up with both get and post requests.

### API response handler

| Attribute  | Description                                 |
|------------|---------------------------------------------|
| Returns    | JSON                                        |
| Implements | `IApiEndpointResponseHandler.getResponse()` |
| Location   | Located in the `api` directory              |



| Property         | Description                                                                    |
|------------------|--------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name              |
| Case             | Directory names are converted from snake_case to kebab-case                    |
| Case             | Class names are converted from PascalCase to kebab-case                        |
| Suffixes removed | Suffixes `Api` or `Endpoint` are removed                                       |
| Prefix           | Api paths are prefixed with `api`                                              |
| Example          | `/api/my_dir/MyPersonEndpoint.kt` will receive the path `api/my-dir/my-person` |


### Page response handler

| Attribute  | Description                           |
|------------|---------------------------------------|
| Returns    | HTML                                  |
| Implements | `IIPageResponseHandler.getResponse()` |
| Location   | Located in the `pages` directory      |


| Property     | Description                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Path         | The path is decided by the directory structure                                    |
| Case         | Directory names are converted from snake_case to kebab-case                       |
| Special case | A that page resides in the directory `landing` it will be served as the root page |
| Example      | `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page`        |

  
### Page endpoint response handler 

These are assume to be tied to pages as opposed to being standalone endpoints like the API endpoints.
Used by pages to for example save data or update an element on a the page.

| Attribute  | Description                                  |
|------------|----------------------------------------------|
| Returns    | JSON                                         |
| Implements | `IPageEndpointResponseHandler.getResponse()` |
| Location   | Located in the `pages` directory             |


| Property         | Description                                                                                                                  |
|------------------|------------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                            |
| Case             | Directory names are converted from snake_case to kebab-case                                                                  |
| Case             | Class names are converted from PascalCase to kebab-case                                                                      |
| Suffixes removed | Suffixes `PageEndpoint` or `Endpoint` are removed                                                                            |
| Prefix           | Prefixed with `page-api`                                                                                                     |
| Example          | `/pages/user_pages/settings/SavePersonNameEndpoint.kt` will receive the path `page-api/user-pages/settings/save-person-name` |


## Parameters
Arguments to pages, api endpoints and page endpoints can be passed as:
- Query parameters
- Request body
- These are stated a constructor arguments that are declared as `val` and non-private (public)
- Parameters are converted from camelCase to kebab-case
  
## Sample
Start the main file in `io.schinzel.samples.web.main`

URLs:
- http://127.0.0.1:5555/my-page
- http://127.0.0.1:5555/my-dir/my-page
- http://127.0.0.1:5555/api/my-dir/my-person
- http://127.0.0.1:5555/page-api/my-page/save-person-name?userId=123&firstName=John

## Custom Response Handler
To create your own response handler you need to:
- Implement `IResponseHandler`
- Implement `IResponseHandlerDescriptor`
- Register the descriptor with the `ResponseHandlerDescriptorRegistry`
