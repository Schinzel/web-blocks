# Template Engine
- Variables can be passed to the template engine.
  In the file variables to be replaced with values have the syntax `{{variableName}}`.
- Template files can include other template files. The syntax for this is `{{include:fileName.html}}`.
    - Include files can contain include files. There is a maximum depth of 10 levels of include files, as to avoid infinite loops.
- Lists can be iterated over using loops. The syntax is `{{for item in items}}...{{/for}}`.
    - Loops can be nested. The depth is limited by the data structure.

## Example

```kotlin
data class User(val name: String, val email: String)

TemplateProcessor(this)
    // Set that variable firstName is Pelle
    .withData("firstName", "Pelle")
    // Add simple list
    .withData("colors", listOf("red", "green", "blue"))
    // Add list of users
    .withData("users", listOf(
        User("Anna", "anna@example.com"),
        User("Bob", "bob@example.com")
    ))
    // Read the file template file and return HTML
    .processTemplate("GreetingPe.html")
```

Below is `GreetingPe.html`
```HTML
<html lang="">
<head>
    <title>Basic Page</title>
    <link rel="stylesheet" type="text/css" href="/style.css">
</head>
<body>
    <h1>Hi {{firstName}}!</h1>
    <h2>Users:</h2>
    <ul>
    {{for user in users}}
        <li>{{user.name}} - {{user.email}}</li>
    {{/for}}
    </ul>
</body>
</html>
```

## Syntax
Add data
```html
<h1>Hi {{firstName}}!</h1>
```

Include files
```html
<body>
    {{include: my-include-file.html}}
</body>
```

Simple lists
```html
{{for color in colors}}
    <span class="{{color}}">{{color}}</span>
{{/for}}
```

Loops
```html
{{for item in items}}
    <p>{{item.name}}</p>
{{/for}}
```

Nested loops
```html
{{for order in orders}}
    <h3>Order #{{order.id}}</h3>
    {{for product in order.products}}
        <p>{{product.name}} - {{product.price}}</p>
    {{/for}}
{{/for}}
```

## Template Engine File Reader
There exists two file readers:
- The source file reader. Read files from the source folder. This means that changes to these files will be
  reflected instantly in the application without the need for a hot reload.
- JAR file reader. Read files from the JAR file. This file reader caches read files.

The file readers take two arguments:
- The file name and path
- The caller class. The files are read relative to the caller class.

The code automatically selects file reader based on if the application is run as a JAR file or not.
