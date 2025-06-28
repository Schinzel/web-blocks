# Routes

There are 3 types of routes:
- API route - a standalone endpoint that returns JSON
- Page route - an HTML-page which returns HTML
- Page API route - an endpoint that belongs to an HTML-page and returns JSON

## API route

| Attribute  | Description                    |
|------------|--------------------------------|
| Implements | `IApiRoute.getResponse()`      |
| Returns    | JSON                           |
| Location   | Located in the `api` directory |



| Property         | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name           |
| Case             | Directory names are converted from snake_case to kebab-case                 |
| Case             | Class names are converted from PascalCase to kebab-case                     |
| Suffixes removed | Suffixes `Route` are removed                                                |
| Prefix           | Api paths are prefixed with `api`                                           |
| Example          | `/api/my_dir/MyPersonRoute.kt` will receive the path `api/my-dir/my-person` |


## Page route

| Attribute  | Description                      |
|------------|----------------------------------|
| Implements | `IPageRoute.getResponse()`       |
| Returns    | HTML                             |
| Location   | Located in the `pages` directory |


| Property     | Description                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Path         | The path is decided by the directory structure                                    |
| Case         | Directory names are converted from snake_case to kebab-case                       |
| Special case | A that page resides in the directory `landing` it will be served as the root page |
| Example      | `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page`        |


## Page API route

These are assume to be tied to pages as opposed to being standalone endpoints like the API routes.
Used by pages to for example save data or update an element on a the page.

| Attribute  | Description                      |
|------------|----------------------------------|
| Implements | `IPageApiRoute.getResponse()`    |
| Returns    | JSON                             |
| Location   | Located in the `pages` directory |


| Property         | Description                                                                                                               |
|------------------|---------------------------------------------------------------------------------------------------------------------------|
| Path             | The path is decided by the directory structure and the Class name                                                         |
| Case             | Directory names are converted from snake_case to kebab-case                                                               |
| Case             | Class names are converted from PascalCase to kebab-case                                                                   |
| Suffixes removed | Suffixes `Route`are removed                                                                                               |
| Prefix           | Prefixed with `page-api`                                                                                                  |
| Example          | `/pages/user_pages/settings/SavePersonNameRoute.kt` will receive the path `page-api/user-pages/settings/save-person-name` |


## Parameters
Arguments to pages, api endpoints and page endpoints can be passed as:
- Query parameters
- Request body
- These are stated a constructor arguments that are declared as `val` and non-private (public)
- Parameters are converted from camelCase to kebab-case
