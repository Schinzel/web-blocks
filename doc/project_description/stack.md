# Technology Stack

## Overview

This document describes the technology stack used in the WebBlocks framework and sample application. It serves as a
reference for both human developers and AI assistants working on the codebase.

## Backend

### Programming Language

- **Kotlin** - Primary programming language
  - Version: 2.1.20 (latest stable release)
  - **K2 Compiler Benefits**: Up to 94% faster compilation speeds, improved IDE performance, and enhanced language features like smart casts
  - **Ecosystem Alignment**: Uses current industry standard version to ensure compatibility with modern frameworks and tooling
- **Java** - Runtime environment
  - Version: 17 (target platform - industry standard LTS baseline for modern frameworks)

### Web Framework

- **Javalin** - Lightweight web framework for JVM

### Logging

- **SLF4J Simple** - Logging framework

## Frontend
- **JavaScript ES6+**

### UI Libraries
- **Bootstrap 5.1 ** - CSS framework (bundled with framework)
- **jQuery 3.5** - JavaScript library (minified version included)
- **Custom ServerCaller.js** - AJAX utility library with Promise support

## Build Tools

- **Apache Maven** - Primary build tool
  - Automated build messages and validation
  - Fat JAR packaging for deployment
- **Maven Plugins**:
  - **Kotlin Maven Plugin** - Compiles Kotlin source code and handles mixed Java/Kotlin projects
  - **Maven Shade Plugin** - Creates uber/fat JARs with all dependencies bundled into a single executable JAR
  - **Maven Compiler Plugin** - Required for IntelliJ IDE to work properly with Kotlin code
  - **Exec Maven Plugin** - Executes system and Java programs during the build lifecycle
  - **ktlint Maven Plugin** - Enforces Kotlin code style and formatting rules

## Deployment & Infrastructure

### Deployment Configuration

- **Fat JAR Deployment** - Single executable JAR (default name: myJar.jar)
- **Default Port**: 5555 (configurable)

### Deployment Command

```bash
web: java $JAVA_OPTS -jar myJar.jar
```

## Version Control

- **Git**

## Testing

- **JUnit Jupiter** - Unit testing framework
- **AssertJ** - Fluent assertions library
- **JSoup** - HTML parsing and HTTP requests for testing


