# Fix Template URLs After Annotation Split

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
After splitting @WebBlockPageApi into @WebBlock and @WebBlockApi annotations, the HTML template files still contain hardcoded URLs with the old "page-api" prefix. These need to be updated to use the new "web-block-api" prefix for API calls.

Specifically, the file `/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/pages/page_with_blocks_and_page_api_route/blocks/update_name_block/template.html` contains:
```javascript
url: "page-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name"
```

This should be:
```javascript
url: "web-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name"
```

## How to solve the problem
1. Search for all HTML and JavaScript files in the project that contain the string "page-api"
2. For each occurrence, determine if it's an API call URL that should be updated to "web-block-api"
3. Update the URLs to use the new prefix
4. Verify that the sample application still works correctly after the changes

## AI Instructions for writing code
Follow the [code standards](../code_standards/_index.md)
- Be careful to only change URLs that are API calls, not any documentation or comments
- Preserve all other aspects of the code exactly as they are
- Make sure to update ALL occurrences, not just the first one found

## Acceptance Criteria
- All hardcoded "page-api" URLs in template files are updated to "web-block-api"
- The sample application works correctly:
  - Start the sample: `mvn compile && mvn exec:exec@run-sample`
  - Navigate to: http://127.0.0.1:5555/page-with-blocks-and-page-api-route?user-id=123222
  - Test that changing the name in the form updates all blocks correctly
- No other code is modified

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test
