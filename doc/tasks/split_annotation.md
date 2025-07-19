# Split WebBlockPageApi Annotation

## Current state
- Current state: [To do / Doing / Done]
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

## Dependencies
- This task depends on the completion of task: rename_annotations.md
- Assumes @WebBlockPageApi has been renamed to @WebBlockApi in that task

## Background
- Read /Users/schinzel/code/web-blocks/doc/user_doc/0_index.md
- Focus on: /Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md
- Look through the sample: /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample

## Problem to solve
Split @WebBlockPageApi into to two types of routes:
- @WebBlock
  - HTML components. Returns HTML.
  - Change the start of the route path from "page-api" -> "web-block"
- @WebBlockApi
  - JSON APIs for WebBlocks
  - Typically does CRUD operations Returns JSON.
  - Change the start of the route path from "page-api" -> "web-block-api"

This would be a @WebBlock:
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateNameBlock.kt

This would be a @WebBlockApi:
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/pages/page_with_blocks_and_page_api_route/blocks/update_name_block/UpdateFirstNameRoute.kt

- Update documentation: /Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md
- Make sure all links in the sample works:
  - /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample
  - /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/README.md

## AI Instructions for writing code
Follow the [code standards](../code_standards/_index.md)
- Especially follow to use defensive programming principles
- Make sure to write elegant code
- Follow the naming standards for tests:
  - unitOfWork_StateUnderTest_ExpectedBehavior
  - `constructor _ valid asset class _ creates object with correct id`

## Acceptance Criteria
- The annotation has been split into two
- UpdateNameBlock should be accessible at: /web-block/page-with-blocks-and-page-api-route/blocks/update-name-block/update-name-block
- UpdateFirstNameRoute should be accessible at: /web-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/update-first-name
- All links in the sample works
- Documentation has been updated

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test
