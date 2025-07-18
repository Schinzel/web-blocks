


Should they return WebBlockResponse or HtmlResponse JsonResponse???

Change kotlin version to Version: 2.1.x? Or better to have it lower
for backward compatability


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

Get the diagrams in doc/work_dir to mermaid


Workflow management in a separate project or ok for now in here??
