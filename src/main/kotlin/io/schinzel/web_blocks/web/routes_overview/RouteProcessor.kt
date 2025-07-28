package io.schinzel.web_blocks.web.routes_overview

import io.schinzel.web_blocks.web.route_mapping.RouteMapping

/**
 * Processes and groups route mappings for the routes overview page.
 * Handles route classification, association, and hierarchical relationships.
 *
 * Written by Claude Sonnet 4
 */
class RouteProcessor {
    /**
     * Groups route mappings by type
     */
    fun groupRoutesByType(routeMappings: List<RouteMapping>): GroupedRoutes =
        GroupedRoutes(
            pageRoutes = routeMappings.filter { it.type == "PageRoute" },
            apiRoutes = routeMappings.filter { it.type == "ApiRoute" },
            pageBlockRoutes = routeMappings.filter { it.type == "PageBlockRoute" },
            pageBlockApiRoutes = routeMappings.filter { it.type == "PageBlockApiRoute" },
        )

    /**
     * Finds page blocks associated with a given page
     * Matches based on route path hierarchy
     */
    fun findAssociatedBlocks(
        page: RouteMapping,
        pageBlockRoutes: List<RouteMapping>,
    ): List<RouteMapping> {
        // Page blocks follow pattern: /page-block{pagePath}/...
        // For page "/page-with-block" -> blocks should match "/page-block/page-with-block/*"
        // For root page "/" -> should not match anything (need exact match logic)

        return if (page.routePath == "/") {
            // Root page should not have any blocks
            emptyList()
        } else {
            val pagePathForBlocks = "/page-block${page.routePath}"
            pageBlockRoutes
                .filter { block ->
                    block.routePath.startsWith(pagePathForBlocks + "/")
                }.sortedBy { it.routePath }
        }
    }

    /**
     * Finds page block APIs associated with a given block
     * Matches based on directory structure - both block and API should be in the same directory
     */
    fun findAssociatedBlockApis(
        block: RouteMapping,
        pageBlockApiRoutes: List<RouteMapping>,
    ): List<RouteMapping> {
        // Extract directory path from block route
        // /page-block/page-with-blocks-and-page-api-route/blocks/update-name-block/update-name
        // Should match with APIs in the same directory:
        // /page-block-api/page-with-blocks-and-page-api-route/blocks/update-name-block/...

        val blockPath = block.routePath.removePrefix("/page-block/")
        val blockDirectory = blockPath.substringBeforeLast("/")
        val expectedApiPrefix = "/page-block-api/$blockDirectory/"

        return pageBlockApiRoutes
            .filter { api ->
                api.routePath.startsWith(expectedApiPrefix)
            }.sortedBy { it.routePath }
    }

    /**
     * Extracts base path for hierarchical matching
     * Removes parameter placeholders to find the base structure
     */
    fun extractBasePath(routePath: String): String {
        // Remove parameter placeholders like {id} from path
        return routePath
            .replace(Regex("\\{[^}]+\\}"), "")
            .replace("//", "/")
            .removeSuffix("/")
    }
}

/**
 * Data class to hold grouped routes
 */
data class GroupedRoutes(
    val pageRoutes: List<RouteMapping>,
    val apiRoutes: List<RouteMapping>,
    val pageBlockRoutes: List<RouteMapping>,
    val pageBlockApiRoutes: List<RouteMapping>,
)
