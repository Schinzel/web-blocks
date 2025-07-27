# Routes JSON API

## Objective
Create a JSON API endpoint at `/web-blocks/routes-json` that provides machine-readable route information for AI-assisted development and tooling.

## Background
The existing HTML routes overview page at `/web-blocks/routes` is designed for human readability. AI tools and automated systems need a structured JSON format to efficiently query and parse route information.

## Relevant classes
- /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/web_blocks/web/set_up_routes/SetUpRoutes.kt
- /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/web_blocks/web/routes_overview/RoutesOverviewPageGenerator.kt

## Requirements

### Endpoint Specification
| Property | Value |
|----------|-------|
| Path | `/web-blocks/routes-json` |
| Type | API endpoint (returns JSON) |
| HTTP Method | GET |

### JSON Structure
```json
{
  "pages": [
    {
      "path": "/page-path",
      "className": "full.package.ClassName",
      "simpleClassName": "ClassName",
      "parameters": [
        {"name": "param-name", "type": "kotlin.String"}
      ],
      "blocks": [
        {
          "name": "block-name",
          "path": "/page-block/...",
          "className": "full.package.BlockClass",
          "simpleClassName": "BlockClass",
          "parameters": []
        }
      ],
      "blockApis": [
        {
          "name": "api-name",
          "path": "/page-block-api/...",
          "className": "full.package.ApiClass",
          "simpleClassName": "ApiClass",
          "parameters": [
            {"name": "param1", "type": "kotlin.Int"},
            {"name": "param2", "type": "kotlin.String"}
          ]
        }
      ]
    }
  ],
  "apis": [
    {
      "path": "/api/endpoint",
      "className": "full.package.ApiClass",
      "simpleClassName": "ApiClass",
      "parameters": []
    }
  ]
}
```

### Field Definitions
| Field | Description |
|-------|-------------|
| path | Route path as accessed by HTTP |
| className | Fully qualified class name |
| simpleClassName | Class name without package |
| parameters | Array of parameter objects with name and type |
| blocks | Array of page blocks (for pages only) |
| blockApis | Array of block APIs (for pages only) |

## Implementation

### Location
Create file: `/src/main/kotlin/io/schinzel/web_blocks/web/routes_overview/RoutesJsonGenerator.kt`

### Modify setUpRoutes
In `/src/main/kotlin/io/schinzel/web_blocks/web/set_up_routes/setUpRoutes.kt`:

Add after the HTML routes endpoint:
```kotlin
// JSON version for AI/tooling
javalin.get("/web-blocks/routes-json") { ctx ->
    val json = RoutesJsonGenerator(routeMappings).generateJson()
    ctx.json(json)
}
```

### Implementation Details

| Step | Action |
|------|--------|
| 1 | Group routes by type using RouteMapping.type |
| 2 | Extract className from routeClass.qualifiedName |
| 3 | Extract simpleClassName from routeClass.simpleName |
| 4 | Parse block relationships from path patterns |
| 5 | Build hierarchical JSON structure |

## Testing

### Unit Tests
Create: `/src/test/kotlin/io/schinzel/web_blocks/web/routes_overview/RoutesJsonGeneratorTest.kt`

| Test Case | Description |
|-----------|-------------|
| `generateJson_emptyRoutes_returnsEmptyStructure` | Empty routes list returns valid JSON with empty arrays |
| `generateJson_singleApiRoute_includesAllFields` | API route includes path, className, parameters |
| `generateJson_pageWithBlocks_createsHierarchy` | Page includes nested blocks and blockApis |
| `generateJson_parametersWithTypes_formatsCorrectly` | Parameters include name and type fields |
| `generateJson_multipleRouteTypes_groupsCorrectly` | Pages and APIs in separate arrays |

### Integration Test
Create: `/src/test/kotlin/io/schinzel/web_blocks/web/routes_overview/RoutesJsonIntegrationTest.kt`

| Test Case | Description |
|-----------|-------------|
| `routesJsonEndpoint_returnsValidJson` | GET /web-blocks/routes-json returns 200 |
| `routesJsonEndpoint_containsExpectedStructure` | Response has pages and apis arrays |
| `routesJsonEndpoint_parsesAsValidJson` | Response parses without errors |

## Standard Acceptance Criteria
* Go through code standards and verify that the code and the tests added follow the standards
* mvn ktlint:format
* mvn ktlint:check
* mvn compile -DskipTests
* mvn test

## Success Criteria
* `/web-blocks/routes-json` returns valid JSON
* All routes included with full class information
* Hierarchical structure preserved for pages/blocks
* Parameters include types
* Response is parseable by standard JSON parsers
* AI tools can query routes by path or class name
