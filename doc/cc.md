See the sample:
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/README.md

With adding annotations, two end points seems to be broken:
http://127.0.0.1:5555/page-with-block?user-id=123222
http://127.0.0.1:5555/page-with-blocks-and-page-api-route?user-id=123222

Have you tested the sample endpoints
/Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/sample/README.md

under user_doc add a stack.md document (nej, det är för användare)
project_description

- stack


Should they return WebBlockResponse or HtmlResponse JsonResponse???


IPageApiRoute remove?

PageApi change to return raw html. Good idea. No success
or future error message to display

Is observable block a good name?

modules/logic/db/src/test/kotlin/
├── unit/                    # Mocked repository tests
│   └── WorkflowServiceTest.kt
├── integration/             # Real database tests
│   ├── WorkflowDaoTest.kt
│   └── DatabaseTestBase.kt  # Shared setup
└── fixtures/                # Test data builders
└── WorkflowFixtures.kt

Mermaid diagram, from request to response.

We are currently adding annotations to Web Blocks
See: /Users/schinzel/code/web-blocks/doc/tasks/annotation-implementation-overview.md
Get started on the next phase please

