# Data Saver Registry

## Current state
- Current state: To do
- Date started: [YYYY-MM-DD HH:MM Stockholm time zone - Command: TZ='Europe/Stockholm' date]
- Date finished: [YYYY-MM-DD HH:MM Stockholm time zone]

## Task summary
[Fill out after task completion - be concise and to the point]
- Include major changes made and files affected
- Document key decisions and their rationale
- Highlight any items that might affect future tasks

## Instructions on how to use and fill out this document
- When a task is started or completed, fill out section Current state
- When a task is completed fill out section Task summary

## Problem to solve
Writing code to save individual form fields (first name, last name, etc.) requires repetitive boilerplate on both client and server side. For each field, developers must:
- Write JavaScript to capture and send data
- Create API endpoints
- Implement validation logic
- Handle the save operation

This leads to duplicated code and makes adding new fields time-consuming.

## How to solve the problem
Create a reusable auto-save system with these components:

### Client-side
1. **Auto-save JavaScript**
   - Monitor all input elements with `data-saver-name` attribute
   - On blur event, send data to server
   - Data payload: `{name: "data-saver-name-value", value: "input-value"}`
   - Single endpoint: `/web-blocks/data-saver`

2. **Save notification**
   - Default notification: appears for 3 seconds then fades out
   - Non-blocking (no click required)
   - Customizable/overridable per field

### Server-side
1. **IDataSaver interface (mandatory)**
   ```kotlin
   interface IDataSaver {
       fun save(value: String): SaveResult
   }
   ```

2. **IValidation interface (optional)**
   ```kotlin
   interface IValidation {
       fun validate(value: String): ValidationResult
   }
   ```

3. **DataSaverRegistry**
   - Singleton registry to register data savers
   - Maps `data-saver-name` to specific saver
   - Route requests to appropriate saver
   - Check if saver implements IValidation before validation

4. **API endpoint**
   - Single endpoint `/web-blocks/data-saver`
   - Receives `{name: String, value: String}`
   - Uses registry to find saver
   - If saver implements IValidation, calls validate first
   - Calls save
   - Returns success/error response

### Example usage
```kotlin
// Simple saver without validation
class FirstNameSaver : IDataSaver {
    override fun save(value: String): SaveResult {
        // Save logic
    }
}

// Saver with validation
class EmailSaver : IDataSaver, IValidation {
    override fun validate(value: String): ValidationResult {
        // Email validation logic
    }
    
    override fun save(value: String): SaveResult {
        // Save logic
    }
}

// Register once in application startup
DataSaverRegistry.register("first-name", FirstNameSaver())
DataSaverRegistry.register("email", EmailSaver())
```

```html
<!-- Just add data-saver-name to any input -->
<input type="text" data-saver-name="first-name" placeholder="First Name">
<input type="text" data-saver-name="last-name" placeholder="Last Name">
```

## Implementation details

### JavaScript file location
- Create `data_saver.js` in appropriate location following WebBlocks patterns
- Must be included in pages that use data-saver functionality

### Notification system
- Use CSS transitions for fade in/out
- Position: top-right corner by default
- Success: green background
- Error: red background
- Override mechanism: allow custom notification function via data attribute

### Error handling
- Network errors: show error notification
- Validation errors: show error with validation message
- Server errors: show generic error message

### Scope limitations
- This task covers only `<input>` elements
- Future tasks may extend to textarea, select, etc.

## AI Instructions for writing code
Follow the [code standards](../code_standards/_index.md)
- Especially follow to use defensive programming principles
- Make sure to write elegant code
- Follow the naming standards for tests:
  - unitOfWork_StateUnderTest_ExpectedBehavior
  - `constructor _ valid asset class _ creates object with correct id`

## Acceptance Criteria
- [ ] JavaScript monitors all inputs with `data-saver-name`
- [ ] Data sends to server on blur
- [ ] Single `/web-blocks/data-saver` endpoint works
- [ ] DataSaverRegistry routes to correct saver
- [ ] Validation runs only if saver implements IValidation
- [ ] Default notification appears and auto-dismisses
- [ ] Notification can be overridden
- [ ] Validation errors display properly
- [ ] Example savers (FirstNameSaver, EmailSaver) demonstrate usage
- [ ] Comprehensive tests for registry and savers

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test