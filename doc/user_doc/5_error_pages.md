_# Error Pages

The framework provides automatic error handling with customizable error pages.

## How Error Pages Work

When an error occurs:
- The framework generates a unique error ID for tracking
- For API endpoints (`/api` or `/page-api`), a JSON error response is returned
- For web pages, an HTML error page is displayed

## Creating Custom Error Pages

Error pages are placed in the `errors` directory and can be customized per environment.

### File Structure
```
errors/
├── 404.html                  # Development 404 page
├── 500.html                  # Development 500 page
├── default.html              # Development default error page
├── production/
│   ├── 404.html             # Production 404 page
│   ├── 500.html             # Production 500 page
│   └── default.html         # Production default error page
└── staging/
    ├── 404.html             # Staging 404 page
    └── default.html         # Staging default error page
```

### Search Priority

The framework searches for error pages in this order:
1. `errors/{environment}/{errorCode}.html` - Environment-specific error code page
2. `errors/{environment}/default.html` - Environment-specific default page
3. `errors/{errorCode}.html` - General error code page
4. `errors/default.html` - General default page
5. Built-in default error page - If no custom pages are found

## Template Variables

Error pages support the following template variables:

| Variable         | Description                                      |
|------------------|--------------------------------------------------|
| `{{errorCode}}`  | The HTTP error code (e.g., 404, 500)           |
| `{{errorMessage}}` | The error message                              |
| `{{errorId}}`    | Unique error identifier for tracking            |
| `{{environment}}` | Current environment (development, staging, production) |

## Example Error Page

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>{{errorCode}} - Page Not Found</title>
</head>
<body>
    <h1>{{errorCode}}</h1>
    <p>{{errorMessage}}</p>
    <small>Error ID: {{errorId}}</small>
    <small>Environment: {{environment}}</small>
</body>
</html>
```

## Environments

The framework supports three built-in environments:
- `DEVELOPMENT` - Default for local development
- `STAGING` - For staging servers
- `PRODUCTION` - For production servers

Custom environments can be added by extending the `Environment` sealed class.

## Built-in Error Handling

The framework automatically handles two types of errors:

### 404 Not Found
- Triggered when a page or endpoint doesn't exist
- Message includes the requested path

### Exceptions (500 Internal Server Error)
- Triggered by any unhandled exception
- Error page displays with error code 500
- Message includes the exception message

## Supporting Other Error Codes

While the framework only handles 404 and exceptions automatically, you can create error pages for any HTTP error code:
- Name the file with the error code (e.g., `403.html`, `401.html`)
- These pages can be displayed by your application code when needed
- If no specific error code file exists, `default.html` is used
