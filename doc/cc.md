


modules/logic/db/src/test/kotlin/
├── unit/                    # Mocked repository tests
│   └── WorkflowServiceTest.kt
├── integration/             # Real database tests
│   ├── WorkflowDaoTest.kt
│   └── DatabaseTestBase.kt  # Shared setup


Mermaid diagram, from request to response.




RouteDescriptorRegistry är object. Alltid pain i rumpan med test.
Kan man sicka in istället?
