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

## Response Handlers
To create a html page or an API endpoint, implement a response handler.
There are three types of response handlers:

### Page response handler
- Used to serve HTML Pages
- Implements `IPageResponseHandler.getResponse()`
- Returns an HTML page as a string
- The path is decided by the directory structure.
  - Pages are located in the `pages` directory
  - Directory name in snake case are converted to kebab case
  - For example the page `/pages/my_dir/my_page/ThePage.kt` will receive the path `/my-dir/my-page` 
  - A special case is if a page resides in the directory `landing` it will be served as the root page

### API response handler
- Used to serve JSON responses
- Implements `IApiEndpointResponseHandler.getResponse()`
- Returns an object which will be converted to JSON
- The path is decided by the directory structure and the file name
  - API endpoints are located in the `api` directory
  - Directory names are converted from snake case to kebab case
  - Class names are converted from pascal case to kebab case
  - A possible suffix `Endpoint` is removed
  - Api paths are prefixed with `api`
  - For example the endpoint `/api/my_dir/MyPersonEndpoint.kt` will receive the path `api/my-dir/my-person`
  
  
### Page endpoint response handler 
- Returns a JSON object
- Used by pages to for example save data or update an element on a the page. 
These are tied to pages as opposed to being standalone endpoints like the API endpoints.
- Implements `IPageEndpointResponseHandler.getResponse()`
- Returns an object which will be converted to JSON
- The path is decided by the directory structure and the file name
    - Located in the `pages` directory
    - All page endpoints are prefixed with `page-api`
    - Class names are converted from pascal case to kebab case
    - A possible suffix `Endpoint` or `PageEndpoint` is removed
    - Page endpoints paths are prefixed with `page-api`
    - For example the page endpoint `/pages/my_dir/MyPageSavePersonNameEndpoint.kt` 
      will receive the path `page-api/my-dir/my-page/save-person-name`


## Parameters


## Sample URLs
- http://127.0.0.1:5555/my-page
- http://127.0.0.1:5555/my-dir/my-page
- http://127.0.0.1:5555/api/my-dir/my-person
- http://127.0.0.1:5555/page-api/my-page/save-person-name?userId=123&firstName=John

### Custom Response Handler
To create your own response handler, implement one of the interfaces above, you need to:
- Implement `IResponseHandler`
- Implement `IResponseHandlerDescriptor`
- Register the descriptor with the `ResponseHandlerDescriptorRegistry`
