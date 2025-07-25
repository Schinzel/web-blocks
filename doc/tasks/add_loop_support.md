# Add Loop Support to Template Engine

## Goal
Add loop functionality to the template engine to iterate over lists and collections.

## Syntax
```html
{{for item in items}}
    <!-- content -->
{{/for}}
```

## Requirements

### 1. Simple Lists
Support iteration over simple lists (strings, numbers):
```kotlin
.addData("colors", listOf("red", "green", "blue"))
```
```html
{{for color in colors}}
    <span>{{color}}</span>
{{/for}}
```

### 2. Object Lists
Support iteration over DTOs/objects with property access:
```kotlin
data class User(val name: String, val email: String)
.addData("users", listOf(User("Anna", "anna@example.com"), ...))
```
```html
{{for user in users}}
    <li>{{user.name}} - {{user.email}}</li>
{{/for}}
```

### 3. Nested Loops
Support nested loops with proper scoping:
```html
{{for order in orders}}
    <h3>Order #{{order.id}}</h3>
    {{for product in order.products}}
        <p>{{product.name}}</p>
    {{/for}}
{{/for}}
```

## Implementation Notes
- Add new `addData(key: String, value: List<Any>)` method
- Use Kotlin reflection for object property access
- No max depth limit for loops (limited by data structure)
- Maintain backward compatibility with existing template syntax

## Files to Modify
- `/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/web_blocks/component/template_engine/TemplateProcessor.kt`
Break into multiple classes if required.

## Documentation
- Updated: `/Users/schinzel/code/web-blocks/doc/user_doc/3_2_template_engine.md`

## Testing
- Test simple lists
- Test object lists with property access
- Test nested loops
- Test edge cases (empty lists, null values)
- Test mixed templates (loops + variables + includes)
  - Includes with included that have nested loops

### AI Review Delegation
- Launch one sub agent to analyze compliance against the code standards section of the markdown
  - [code standards add](../code_standards/code_standards_all.md)
  - [code standards AIs](../code_standards/code_standards_ais.md)
- Launch another sub agent to analyze adherence to the testing standards section.
  - [testing standards](../code_standards/testing_standards.md)
- Launch another sub agent to analyze adherence to in code documentation
  - [code standards add](../code_standards/code_standards_all.md)
- Launch another sub agent to analyze adherence elegant code, DRY principles and so on
  - [code standards add](../code_standards/code_standards_all.md)

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test


