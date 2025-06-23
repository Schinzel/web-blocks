# Component Framework

## Features
- A grid based system based on Bootstrap
- A page consists of a set of modular standalone page-elements
- Each page-element has its own endpoint
- The page-elements can update themselves independently of the page
- A observer-pattern system where a page-elements can notify is observing page-elements that they should update themselves
- Instant Updates. The HTML and JS files are read from the source folder. This means that changes to these files will
  be reflected instantly in the application without the need for a hot reload.
- A template engine

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
When building a JAR the HTML and JS files need to be included in the JAR.
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
