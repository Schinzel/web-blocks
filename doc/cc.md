

Is ObservableBlock a good name?
It feels it might be confusing to users.
Maybe ObservableBlock should be WebPageBlock or Block or something else.

I like that the implementation is separate from IBlock, that observer pattern implementation is separate.
This I want to keep.



Om tog bort getPath från IRoute. Kan vi då göra auto doc?


Should they return WebBlockResponse or HtmlResponse JsonResponse???



PageApi change to return raw html. Good idea. No success
or future error message to display


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


Are there four or three routes?
- API
- Page
- PageApi
- PageBlock

Workflow management in a separate project or ok for now in here??
