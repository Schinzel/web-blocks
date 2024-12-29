# Page Elements - A Modern Web Framework for Kotlin
The project consists of two parts:
- A web framework
- A component framework

# Problem to solve


# Web Framework
## Features
- File-based routing system.
- Implement the interfaces 
  - IPageResponseHandler - For handling page requests
  - IApiEndpointResponseHandler - For handling API requests


## Benefits
The framework uses a file-system based routing approach where the
application's URL structure directly mirrors its folder structure. 
This intuitive system:
- Eliminates the need for manual route configuration
- Makes the application structure immediately clear to new developers
- Simplifies navigation and routing management
- Provides a predictable pattern for adding new pages

## Problem to solve


# Component Framework

## Features
- A grid based system based on Bootstrap
- A page consists of a set of modular stand alone page elements. 
- Each page element has its own endpoint
- The page elements can update themselves independently of the page
- A publish-subscribe system where a page elements can notify is subscribers that they should update themselves.
- Instant Updates. The HTML and JS files are read from the source folder. This means that changes to these files will be reflected in the
application without the need for a hot reload.
- Each endpoint supports both GET and POST requests


## Problem to solve



Page Elements is a lightweight web framework for Kotlin that combines the simplicity of a file-system based routing 
with the power of modular, feature-based architecture. 
It enables developers to quickly build maintainable web applications by embracing 
convention over configuration and component-based development.

## Key Features


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
- http://127.0.0.1:5555/page-api/user-account/name-pe/name-update-dao/123/Henrik/Svensson

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

# Routes
- Pages reside in the directory "pages"
    - Pages have no route prefix
    - Routes are defined by directories that lead to the page
    - Routing paths mirror the directory structure, where each folder represents a path segment
- Endpoints reside in the directory "api"
    - Endpoints have the route prefix "api"
    - Routing paths mirror the directory structure, where each folder represents a path segment, plus the name of the class in kebab-case
- Page endpoints are found in the "pages" directory
    - Typically, these endpoints are used to save data from a page
    - Page endpoints have the route prefix "page-api"
    - Routing paths mirror the directory structure, where each folder represents a path segment, plus the name of the class in kebab-case


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
