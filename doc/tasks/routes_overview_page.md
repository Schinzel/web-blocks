# Routes Overview Page

## Objective
Create a routes overview page at `/web-blocks/routes` that displays all registered routes in the WebBlocks application with their class names, parameters, and hierarchical relationships.

## Background
When WebBlocks starts, it prints all routes to console. We need a web-accessible page that shows this information in a structured format, making it easier for developers to understand the application's API surface.

## Requirements

### URL Structure
| Property | Value |
|----------|-------|
| Path | `/web-blocks/routes` |
| Reserved prefix | `/web-blocks/*` for all framework pages |

### Display Requirements

#### Page Routes Section
| Element | Description |
|---------|-------------|
| Path | Full route path |
| Class | Implementing class name |
| Parameters | Parameter names with types |
| Blocks | Nested list of associated page blocks |
| Block APIs | Nested list of associated page block APIs |

#### API Routes Section
| Element | Description |
|---------|-------------|
| Path | Full route path |
| Class | Implementing class name |
| Parameters | Parameter names with types |

### Technical Requirements

| Requirement | Description |
|-------------|-------------|
| Route Registration | Register before user routes to ensure precedence |
| No Singletons | Use closure to capture routeMappings |
| Dynamic Generation | Generate HTML fresh on each request |

## Implementation

### Location
Create file: `/src/main/kotlin/io/schinzel/web_blocks/web/routes_overview/RoutesOverviewPageGenerator.kt`

### Modify setUpRoutes
In `/src/main/kotlin/io/schinzel/web_blocks/web/set_up_routes/setUpRoutes.kt`:

1. Capture routeMappings from findRoutes()
2. Register `/web-blocks/routes` endpoint before user routes
3. Use closure to pass routeMappings to generator

### HTML Structure
```
WebBlocks Routes Overview
========================

Pages & Components
------------------
[Page Path]
├─ Class: [ClassName]
├─ Parameters: [param-name] ([Type])
├─ Blocks:
│   └─ [block-name]
│       ├─ Class: [BlockClassName]
│       └─ [block-path]
└─ Block APIs:
    └─ [block-name]
        ├─ Class: [ApiClassName]
        └─ [api-path]
            └─ Parameters: [param] ([Type])

API Routes
----------
[API Path]
├─ Class: [ClassName]
└─ Parameters: [param-name] ([Type])
```

### Parsing Logic

| Step | Action |
|------|--------|
| 1 | Group routes by type (Page, Api, PageBlock, PageBlockApi) |
| 2 | For pages, extract associated blocks from PageBlock routes |
| 3 | For blocks, extract associated APIs from PageBlockApi routes |
| 4 | Sort alphabetically within each group |

## Testing

### Unit Tests
Create: `/src/test/kotlin/io/schinzel/web_blocks/web/routes_overview/RoutesOverviewPageGeneratorTest.kt`

| Test Case | Description |
|-----------|-------------|
| `generateHtml_noRoutes_returnsEmptyPageStructure` | Empty routes list generates valid HTML |
| `generateHtml_singleApiRoute_displaysCorrectly` | Single API route with parameters |
| `generateHtml_pageWithBlocks_showsHierarchy` | Page with blocks and block APIs nested correctly |
| `generateHtml_multipleRouteTypes_groupsCorrectly` | Mixed route types grouped in correct sections |

### Integration Test
Verify `/web-blocks/routes` endpoint returns 200 and contains expected structure.

## Standard Acceptance Criteria
* Go through code standards and verify that the code and the tests added follow the standards
* mvn ktlint:format
* mvn ktlint:check
* mvn compile -DskipTests
* mvn test

## Success Criteria
* `/web-blocks/routes` displays all routes with class names
* Routes grouped by type (Pages vs APIs)
* Page blocks and APIs nested under their parent pages
* Parameters shown with types
* Clean, readable HTML output