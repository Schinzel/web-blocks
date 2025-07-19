


Read /Users/schinzel/code/web-blocks/doc/user_doc/0_index.md
Focus on: /Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md

So currently there are 3 types of routes:
- @WebBlockApi
- @WebBlockPage
- @WebBlockPageApi

Rename from:
- @WebBlockApi to @Api - Works as before in all other aspects
- @WebBlockPage → @Page - Works as before in all other aspects

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

- Rename @WebBlockApi to @Api & @WebBlockPage → @Page
- Split @WebBlockPageApi into to two types of routes
- Change sample
- Change doc
  - /Users/schinzel/code/web-blocks/doc/user_doc/2_routes.md





Om tog bort getPath från IRoute. Kan vi då göra auto doc?


Should they return WebBlockResponse or HtmlResponse JsonResponse???



PageApi change to return raw html. Good idea? No success
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



Workflow management in a separate project or ok for now in here??
