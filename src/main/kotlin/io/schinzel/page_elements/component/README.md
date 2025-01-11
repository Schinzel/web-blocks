# Component Framework

## Features
- A grid based system based on Bootstrap
- A page consists of a set of modular standalone page-elements.
- Each page-element has it's own endpoint
- The page-elements can update themselves independently of the page
- A publish-subscribe system where a page-elements can notify is subscribers that they should update themselves.
- Instant Updates. The HTML and JS files are read from the source folder. This means that changes to these files will
  be reflected instantly in the application without the need for a hot reload.
- Template engine

## Template Engine
- Variables can be passed to the template engine. In the file variables to be replaced with values have the syntax `{{variableName}}`
- Template files can include other template files. The syntax for this is `{{include:fileName.html}}`.
    - Include files can contain include files. There is a maximum depth of 10 levels of include files, as to avoid infinite loops.

### Template Engine File Reader
There exists two file readers: 
  - The source file reader. Read files from the source folder. This means that changes to these files will be 
    reflected instantly in the application without the need for a hot reload.
  - JAR file reader. Read files from the JAR file. This file reader caches read files.
The file readers take two arguments
  - The file name and path
  - The caller class. The files are read relative to the caller class. 

The code automatically selects file reader based on if the application is run as a JAR file or not.


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
[Sample](../samples/component/README.md)




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
