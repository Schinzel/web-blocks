

We have IApiRoute & IHtmlRoute
Is this consistent naming?
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/web_blocks/web/routes/IWebBlockRoute.kt
Html is what is being returned.
Api is what it is.

See examples:
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/pages/landing/LandingPage.kt
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/api/UserPets.kt



modules/logic/db/src/test/kotlin/
├── unit/                    # Mocked repository tests
│   └── WorkflowServiceTest.kt
├── integration/             # Real database tests
│   ├── WorkflowDaoTest.kt
│   └── DatabaseTestBase.kt  # Shared setup


Mermaid diagram, from request to response.




