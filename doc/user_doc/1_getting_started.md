# The first  page
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

Create a package for you first page, `simple_page`.
In this package create a class that extends `IPageResponseHandler`.
The class can have any name.  

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
class ThePage : IPageResponseHandler {
    override fun getResponse(): String {
        return """
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
        """.trimMargin()
    }
}
```

## 3 - Start the project and request the page
Invoke `MyWebApp().start()`

You should see something along the lines
```
RouteMapping(type='WebPage', path='simple-page', parameters=[])
******************************
Project started on port 5555
******************************
```

In a browser request `http://127.0.0.1:5555/simple-page` which should return "Hello world"

**Note**: The URL path `/simple-page` is automatically generated from your package name `simple_page` (snake_case is converted to kebab-case).
