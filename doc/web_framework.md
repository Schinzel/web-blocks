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

## Instructions
### Response Handlers
To create a html page or an API endpoint, implement a response handler.
There are three types of response handlers:
- Page response handler
    - Used to serve HTML Pages
    - Returns an HTML page as a string
    - Located in the `pages` directory
    - Implement `IPageResponseHandler`
    - See for details: `PageEndpointResponseHandlerDescriptor`
- API response handler
    - Used to serve JSON responses
    - Located in the `api` directory
    - Implement `IApiEndpointResponseHandler`
    - See for details:`IApiEndpointResponseHandler`
- Page endpoint response handler - Returns a JSON object
    - Used by pages to for example save data or update an element on a the page.
    - These are tied to pages as opposed to being standalone endpoints like the API endpoints
    - Located in the `pages` directory
    - Implement `IPageEndpointResponseHandler`
    - See for details: `IPageEndpointResponseHandler`

### Custom Response Handler
To create your own response handler, implement one of the interfaces above, you need to:
- Implement `IResponseHandler`
- Implement `IResponseHandlerDescriptor`
- Register the descriptor with the `ResponseHandlerDescriptorRegistry`
