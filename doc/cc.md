






Should they return WebBlockResponse or HtmlResponse JsonResponse
ApiResponse and PageResponse/HtmlResponse


Om tog bort getPath från IRoute. Kan vi då göra auto doc?


modules/logic/db/src/test/kotlin/
├── unit/                    # Mocked repository tests
│   └── WorkflowServiceTest.kt
├── integration/             # Real database tests
│   ├── WorkflowDaoTest.kt
│   └── DatabaseTestBase.kt  # Shared setup
└── fixtures/                # Test data builders
└── WorkflowFixtures.kt

Mermaid diagram, from request to response.

