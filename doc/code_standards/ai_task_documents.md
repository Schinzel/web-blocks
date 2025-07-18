# AI Task Documents


# Creating Task Documents
Simpler tasks just require one document.

Each task documents have at least the below headings:
- Current state
- Problem to solve
- How to solve the problem
- Acceptance Criteria

### Current state
- Current state [To do / Doing / Done]
- Date started [YYYY-MM-DD HH:MM Stockholm time zone Command to get time TZ='Europe/Stockholm' date]
- Date finished [same as date started]


### Acceptance Criteria
Has at least the below criteria
- Go through [code standards](_index.md) and verify that the code and the tests added follow the standards
- mvn ktlint:format
- ktlint:check
- mvn compile -DskipTests
- mvn test






## Multiple Task Documents
More complex task can require be broken down in several documents.
Several less complicated tasks or task documents are to be preferred or a single more complex and/or large document.


