# Page Elements - A Modern Web Framework for Kotlin
The project consists of two parts:
- A web framework
- A component framework


# Web Framework
## Features
- File-based routing system.
- Each endpoint supports both GET and POST requests and the arguments can be passed as:
  - Query parameters
  - Request body

- Implement the interfaces 
  - IPageResponseHandler.getResponse():String - For handling page requests
  - IApiEndpointResponseHandler.getResponse():Any - For handling API requests


## Benefits
The framework uses a file-system based routing approach where the
application's URL structure directly mirrors its folder structure. 
This intuitive system:
- Eliminates the need for manual route configuration
- Makes the application structure immediately clear to new developers
- Simplifies navigation and routing management
- Provides a predictable pattern for adding new pages


# Component Framework

## Features
- A grid based system based on Bootstrap
- A page consists of a set of modular stand alone page elements. 
- Each page element has its own endpoint
- The page elements can update themselves independently of the page
- A publish-subscribe system where a page elements can notify is subscribers that they should update themselves.
- Instant Updates. The HTML and JS files are read from the source folder. This means that changes to these files will 
be reflected instantly in the application without the need for a hot reload.


### Benefits
- **Maintainability**: Small, focused components are easier to understand and modify
- **Scalability**: Feature-based organization supports growing codebases
- **Developer Experience**: Intuitive routing and clear structure reduces learning curve
- **Testing**:  Can be developed, tested, and maintained independently
- **Code Quality**: Encourages clean, modular code with clear responsibilities
- 


### Feature-Based Architecture
The project structure encourages feature-based design principles, where related code is 
grouped by feature rather than technical function. This approach:
- Makes code more discoverable and easier to understand
- Reduces cognitive load when working on specific features
- Facilitates better code organization and maintenance
- Enables easier scaling of the codebase



# Sample
Start the class io.schinzel.sample.main

Sample urls:
- Call landing page: http://127.0.0.1:5555/?userId=ABC
- Invoke an API endpoint: http://127.0.0.1:5555/api/v1/user-information?userId=123
- Call a page a in a subdirectory = http://127.0.0.1:5555/sub-dir/sub-dir-2
- Call a page element in a page http://127.0.0.1:5555/page-api/user-account/update-name-pe/update-first-name/?userId=123&firstName=Jack
- Call a page with interconnecting page elements http://127.0.0.1:5555/user-account?userId=123

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
