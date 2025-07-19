# Rename annotations

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

## Background
Read /Users/schinzel/code/web-blocks/doc/user_doc/0_index.md
Focus on: /Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md
Look through the sample: /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample

## Problem to solve
Rename from:
- @WebBlockApi to @Api - Works as before in all other aspects
- @WebBlockPage → @Page - Works as before in all other aspects
- Update documentation /Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md
- Update the sample that uses renamed annotations
  - /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/README.md

## AI Instructions for writing code
Follow the [code standards](../code_standards/_index.md)
- Especially follow to use defensive programming principles
- Make sure to write elegant code
- Follow the naming standards for tests:
  - unitOfWork_StateUnderTest_ExpectedBehavior
  - `constructor _ valid asset class _ creates object with correct id`

## Acceptance Criteria
- The annotations have been renamed
- All links in the sample works
- Documentation has been updated

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test
