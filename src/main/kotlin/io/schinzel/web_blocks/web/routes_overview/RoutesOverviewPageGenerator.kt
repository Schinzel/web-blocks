package io.schinzel.web_blocks.web.routes_overview

import io.schinzel.web_blocks.web.route_mapping.RouteMapping

/**
 * The purpose of this class is to generate an HTML page displaying all registered
 * routes in the WebBlocks application with their class names, parameters, and
 * hierarchical relationships.
 *
 * Written by Claude Sonnet 4
 */
class RoutesOverviewPageGenerator {
    /**
     * Generates HTML for the routes overview page
     * @param routeMappings List of all route mappings in the application
     * @return Complete HTML page as string
     */
    fun generateHtml(routeMappings: List<RouteMapping>): String {
        // Group routes by type
        val pageRoutes = routeMappings.filter { it.type == "PageRoute" }
        val apiRoutes = routeMappings.filter { it.type == "ApiRoute" }
        val pageBlockRoutes = routeMappings.filter { it.type == "PageBlockRoute" }
        val pageBlockApiRoutes = routeMappings.filter { it.type == "PageBlockApiRoute" }

        // Build HTML
        val html = StringBuilder()
        html.append(generateHtmlHeader())
        html.append(generatePagesSection(pageRoutes, pageBlockRoutes, pageBlockApiRoutes))
        html.append(generateApiSection(apiRoutes))
        html.append(generateHtmlFooter())

        return html.toString()
    }

    /**
     * Generates HTML header with title and styles
     */
    private fun generateHtmlHeader(): String =
        """
        <!DOCTYPE html>
        <html>
        <head>
            <title>WebBlocks Routes Overview</title>
            <style>
                body { font-family: monospace; margin: 20px; }
                h1, h2 { color: #333; }
                .route { margin: 10px 0; padding: 10px; border-left: 3px solid #007bff; background: #f8f9fa; }
                .route-path { font-weight: bold; color: #007bff; }
                .route-class { color: #6c757d; margin-left: 20px; }
                .route-file { color: #495057; margin-left: 20px; font-size: 0.9em; }
                .route-params { color: #28a745; margin-left: 20px; }
                .nested { margin-left: 40px; }
                .block { margin-left: 20px; border-left: 2px solid #ffc107; background: #fff3cd; padding: 5px; }
                .api { margin-left: 20px; border-left: 2px solid #dc3545; background: #f8d7da; padding: 5px; }
            </style>
        </head>
        <body>
            <h1>WebBlocks Routes Overview</h1>
        """.trimIndent()

    /**
     * Generates HTML footer
     */
    private fun generateHtmlFooter(): String =
        """
        </body>
        </html>
        """.trimIndent()

    /**
     * Generates the Pages & Components section
     */
    private fun generatePagesSection(
        pageRoutes: List<RouteMapping>,
        pageBlockRoutes: List<RouteMapping>,
        pageBlockApiRoutes: List<RouteMapping>,
    ): String {
        val html = StringBuilder()
        html.append("<h2>Pages &amp; Components</h2>\n")

        // Sort pages alphabetically
        val sortedPages = pageRoutes.sortedBy { it.routePath }

        for (page in sortedPages) {
            html.append(generateRouteHtml(page, "route"))

            // Find associated blocks for this page
            val associatedBlocks = findAssociatedBlocks(page, pageBlockRoutes)
            if (associatedBlocks.isNotEmpty()) {
                html.append("<div class=\"nested\">Blocks:</div>\n")
                for (block in associatedBlocks) {
                    html.append(generateRouteHtml(block, "block nested"))

                    // Find associated APIs for this block
                    val associatedApis = findAssociatedBlockApis(block, pageBlockApiRoutes)
                    if (associatedApis.isNotEmpty()) {
                        html.append("<div class=\"nested\" style=\"margin-left: 60px;\">Block APIs:</div>\n")
                        for (api in associatedApis) {
                            html.append(generateRouteHtml(api, "api nested", "margin-left: 80px;"))
                        }
                    }
                }
            }
        }

        return html.toString()
    }

    /**
     * Generates the API Routes section
     */
    private fun generateApiSection(apiRoutes: List<RouteMapping>): String {
        val html = StringBuilder()
        html.append("<h2>API Routes</h2>\n")

        // Sort APIs alphabetically
        val sortedApis = apiRoutes.sortedBy { it.routePath }

        for (api in sortedApis) {
            html.append(generateRouteHtml(api, "route"))
        }

        return html.toString()
    }

    /**
     * Generates HTML for a single route
     */
    private fun generateRouteHtml(
        route: RouteMapping,
        cssClass: String,
        additionalStyle: String = "",
    ): String {
        val styleAttr = if (additionalStyle.isNotEmpty()) " style=\"$additionalStyle\"" else ""
        val html = StringBuilder()

        html.append("<div class=\"$cssClass\"$styleAttr>\n")
        html.append("  <div class=\"route-path\">${route.routePath}</div>\n")
        val filePath = getRelativeFilePath(route.routeClass)
        html.append("  <div class=\"route-class\">Class: ${route.routeClass.simpleName}</div>\n")
        html.append("  <div class=\"route-file\">File: $filePath</div>\n")

        // Add parameters if they exist
        if (route.parameters.isNotEmpty()) {
            val paramsList =
                route.parameters.joinToString(", ") { param ->
                    val typeName =
                        param.type
                            .toString()
                            .substringAfterLast('.')
                            .substringBefore('?')
                    "${param.name} ($typeName)"
                }
            html.append("  <div class=\"route-params\">Parameters: $paramsList</div>\n")
        }

        html.append("</div>\n")

        return html.toString()
    }

    /**
     * Finds page blocks associated with a given page
     * Matches based on route path hierarchy
     */
    private fun findAssociatedBlocks(
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
    private fun findAssociatedBlockApis(
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
    private fun extractBasePath(routePath: String): String {
        // Remove parameter placeholders like {id} from path
        return routePath
            .replace(Regex("\\{[^}]+\\}"), "")
            .replace("//", "/")
            .removeSuffix("/")
    }

    /**
     * Converts a KClass to its relative file path in the project
     * Example: io.schinzel.sample.api.UserPets -> src/main/kotlin/io/schinzel/sample/api/UserPets.kt
     */
    private fun getRelativeFilePath(kClass: kotlin.reflect.KClass<*>): String {
        val packagePath = kClass.java.packageName.replace('.', '/')
        val className = kClass.simpleName ?: "Unknown"
        return "src/main/kotlin/$packagePath/$className.kt"
    }
}
