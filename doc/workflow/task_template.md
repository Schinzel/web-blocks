# [Task Name]

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

## Problem to solve
[Specify the problem that is to be solved with this task]

## How to solve the problem
[Specify how to solve the problems]

## AI Instructions for writing code
Follow the [code standards](../code_standards/_index.md)
- Especially follow to use defensive programming principles
- Make sure to write elegant code
- Follow the naming standards for tests:
  - unitOfWork_StateUnderTest_ExpectedBehavior
  - `constructor _ valid asset class _ creates object with correct id`

## Acceptance Criteria
[Add specific criteria for this task]

### Standard Acceptance Criteria
- Go through [code standards](../code_standards/_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- mvn ktlint:check
- mvn compile -DskipTests
- mvn test
