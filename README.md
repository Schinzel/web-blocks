# Page Elements - A Modern Web Framework for Kotlin

Page Elements is a lightweight web framework for Kotlin that combines the simplicity of Next.js routing 
with the power of modular, feature-based architecture. 
It enables developers to quickly build maintainable web applications by embracing 
convention over configuration and component-based development.

## Key Features

### File-System Based Routing
Taking inspiration from Next.js, the framework uses a file-system based routing approach where the 
application's URL structure directly mirrors its folder structure. This intuitive system:
- Eliminates the need for manual route configuration
- Makes the application structure immediately clear to new developers
- Simplifies navigation and routing management
- Provides a predictable pattern for adding new pages

### Modular Page Elements
The framework encourages building pages from small, independent Page Elements. Each Page Element:
- Contains all its necessary components, logic, and resources
- Functions as a self-contained feature module
- Can be developed, tested, and maintained independently

### Feature-Based Architecture
The project structure encourages feature-based design principles, where related code is 
grouped by feature rather than technical function. This approach:
- Makes code more discoverable and easier to understand
- Reduces cognitive load when working on specific features
- Facilitates better code organization and maintenance
- Enables easier scaling of the codebase

### Simple API Integration
The framework provides a straightforward way to create both page renders and API endpoints:
- Unified handling of GET and POST requests
- Automatic parameter parsing
- Clean separation between page rendering and API logic
- Type-safe request handling

### Benefits
- **Rapid Development**: Convention over configuration reduces boilerplate
- **Maintainability**: Small, focused components are easier to understand and modify
- **Scalability**: Feature-based organization supports growing codebases
- **Developer Experience**: Intuitive routing and clear structure reduces learning curve
- **Code Quality**: Encourages clean, modular code with clear responsibilities

## Sample
Start the class io.schinzel.sample.main

Sample urls:
- http://127.0.0.1:5555/?userId=ABC
- http://127.0.0.1:5555/api/v1/user-information?userId=123
- http://127.0.0.1:5555/api/v1/user-information/123
- http://127.0.0.1:5555/sub-dir/sub-dir-2

## Page path
The path to the directory in which a page is located. 
If the page is located in the directory "landing" then the path to the page is "/"

## Routes
Each route is set up with both get and post requests.
Variables can be passed as:
- Query parameters
- Path parameters
- Request body

## Api path
Prefix: /api. 
Name of the class in kebab-case

## HTML and JS files
The HTML and JS files are read from the source folder. This means that changes to these files will be reflected in the 
application without the need for a hot reload.

## Building a JAR
When building a JAR the html and js files need to be included in the JAR.
This is done by adding the following to the pom.xml file.
```xml

<build>
  <resources>
    <resource>
      <directory>src/main/kotlin</directory>
      <includes>
        <!-- Include html and js files in the jar -->
        <include>**/*.html</include>
        <include>**/*.js</include>
      </includes>
    </resource>
    <!-- Keep the default resources directory if you have one -->
    <resource>
      <directory>src/main/resources</directory>
    </resource>
  </resources>
  [...]
</build>
```
