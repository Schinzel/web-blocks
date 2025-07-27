package io.schinzel.web_blocks.web.routes_overview

import io.schinzel.web_blocks.web.route_mapping.RouteMapping

/**
 * The purpose of this class is to generate JSON representation of all registered
 * routes in the WebBlocks application for AI-assisted development and tooling.
 *
 * Written by Claude Sonnet 4
 */
class RoutesJsonGenerator(
    private val routeMappings: List<RouteMapping>,
) {
    /**
     * Generates JSON structure for all routes
     * @return Map representing the complete route structure
     */
    fun generateJson(): Map<String, Any> {
        // Group routes by type
        val pageRoutes = routeMappings.filter { it.type == "PageRoute" }
        val apiRoutes = routeMappings.filter { it.type == "ApiRoute" }
        val pageBlockRoutes = routeMappings.filter { it.type == "PageBlockRoute" }
        val pageBlockApiRoutes = routeMappings.filter { it.type == "PageBlockApiRoute" }

        return mapOf(
            "pages" to generatePagesJson(pageRoutes, pageBlockRoutes, pageBlockApiRoutes),
            "apis" to generateApisJson(apiRoutes),
        )
    }

    /**
     * Generates JSON for pages with their associated blocks and block APIs
     */
    private fun generatePagesJson(
        pageRoutes: List<RouteMapping>,
        pageBlockRoutes: List<RouteMapping>,
        pageBlockApiRoutes: List<RouteMapping>,
    ): List<Map<String, Any>> =
        pageRoutes
            .sortedBy { it.routePath }
            .map { page ->
                val associatedBlocks = findAssociatedBlocks(page, pageBlockRoutes)
                val associatedBlockApis = findAssociatedBlockApis(page, pageBlockApiRoutes)

                mapOf(
                    "path" to page.routePath,
                    "className" to (page.routeClass.qualifiedName ?: ""),
                    "simpleClassName" to (page.routeClass.simpleName ?: ""),
                    "parameters" to
                        page.parameters.map { param ->
                            mapOf(
                                "name" to param.name,
                                "type" to param.type.toString(),
                            )
                        },
                    "blocks" to
                        associatedBlocks.map { block ->
                            mapOf(
                                "name" to extractBlockName(block),
                                "path" to block.routePath,
                                "className" to (block.routeClass.qualifiedName ?: ""),
                                "simpleClassName" to (block.routeClass.simpleName ?: ""),
                                "parameters" to
                                    block.parameters.map { param ->
                                        mapOf(
                                            "name" to param.name,
                                            "type" to param.type.toString(),
                                        )
                                    },
                            )
                        },
                    "blockApis" to
                        associatedBlockApis.map { api ->
                            mapOf(
                                "name" to extractApiName(api),
                                "path" to api.routePath,
                                "className" to (api.routeClass.qualifiedName ?: ""),
                                "simpleClassName" to (api.routeClass.simpleName ?: ""),
                                "parameters" to
                                    api.parameters.map { param ->
                                        mapOf(
                                            "name" to param.name,
                                            "type" to param.type.toString(),
                                        )
                                    },
                            )
                        },
                )
            }

    /**
     * Generates JSON for standalone API routes
     */
    private fun generateApisJson(apiRoutes: List<RouteMapping>): List<Map<String, Any>> =
        apiRoutes
            .sortedBy { it.routePath }
            .map { api ->
                mapOf(
                    "path" to api.routePath,
                    "className" to (api.routeClass.qualifiedName ?: ""),
                    "simpleClassName" to (api.routeClass.simpleName ?: ""),
                    "parameters" to
                        api.parameters.map { param ->
                            mapOf(
                                "name" to param.name,
                                "type" to param.type.toString(),
                            )
                        },
                )
            }

    /**
     * Finds page blocks associated with a given page
     * Matches based on route path hierarchy
     */
    private fun findAssociatedBlocks(
        page: RouteMapping,
        pageBlockRoutes: List<RouteMapping>,
    ): List<RouteMapping> =
        if (page.routePath == "/") {
            // Root page should not have any blocks
            emptyList()
        } else {
            val pagePathForBlocks = "/page-block${page.routePath}"
            pageBlockRoutes
                .filter { block ->
                    block.routePath.startsWith(pagePathForBlocks + "/")
                }.sortedBy { it.routePath }
        }

    /**
     * Finds page block APIs associated with a given page
     * Matches all block APIs that belong to the same page structure
     */
    private fun findAssociatedBlockApis(
        page: RouteMapping,
        pageBlockApiRoutes: List<RouteMapping>,
    ): List<RouteMapping> =
        if (page.routePath == "/") {
            // Root page should not have any block APIs
            emptyList()
        } else {
            val pagePathForApis = "/page-block-api${page.routePath}"
            pageBlockApiRoutes
                .filter { api ->
                    api.routePath.startsWith(pagePathForApis + "/")
                }.sortedBy { it.routePath }
        }

    /**
     * Extracts meaningful block name from route path
     * Example: /page-block/simple-page/blocks/my-block -> my-block
     */
    private fun extractBlockName(block: RouteMapping): String {
        val path = block.routePath.removePrefix("/page-block/")
        return path.substringAfterLast("/")
    }

    /**
     * Extracts meaningful API name from route path
     * Example: /page-block-api/simple-page/blocks/save-data -> save-data
     */
    private fun extractApiName(api: RouteMapping): String {
        val path = api.routePath.removePrefix("/page-block-api/")
        return path.substringAfterLast("/")
    }
}
