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
Launch 4 subagents in parallel by making multiple Task tool calls in a single message:

<invoke name="Task">
<parameter name="description">Code standards compliance review</parameter>
<parameter name="prompt">Review TemplateProcessor implementation against /doc/code_standards/code_standards_all.md and /doc/code_standards/code_standards_ais.md for compliance with class size limits, naming conventions, documentation requirements, immutability principles, defensive programming, interface usage, and JVM language compatibility</parameter>
<parameter name="subagent_type">general-purpose</parameter>
</invoke>

<invoke name="Task">
<parameter name="description">Testing standards adherence review</parameter>
<parameter name="prompt">Analyze the SimpleTemplateProcessorTest test suite against /doc/code_standards/testing_standards.md for adherence to testing principles, naming conventions, AssertJ usage, and test organization</parameter>
<parameter name="subagent_type">general-purpose</parameter>
</invoke>

<invoke name="Task">
<parameter name="description">In-code documentation review</parameter>
<parameter name="prompt">Evaluate in-code documentation quality in TemplateProcessor against /doc/code_standards/code_standards_all.md focusing on class/function documentation, comment density, and WHY vs WHAT explanations</parameter>
<parameter name="subagent_type">general-purpose</parameter>
</invoke>

<invoke name="Task">
<parameter name="description">Code elegance and DRY review</parameter>
<parameter name="prompt">Assess TemplateProcessor code for elegance, DRY principles, clean code practices, avoiding cleverness, and maintaining readability while ensuring enterprise production readiness</parameter>
<parameter name="subagent_type">general-purpose</parameter>
</invoke>

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test


