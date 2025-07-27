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
                .route-container { margin: 5px 0; border: 1px solid #dee2e6; border-radius: 4px; }
                .route-header { 
                    padding: 10px; 
                    background: #f8f9fa; 
                    cursor: pointer; 
                    border-left: 3px solid #007bff;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                .route-header:hover { background: #e9ecef; }
                .route-path { font-weight: bold; color: #007bff; }
                .route-details { 
                    padding: 10px; 
                    background: #ffffff; 
                    border-top: 1px solid #dee2e6;
                    display: none;
                }
                .route-details.expanded { display: block; }
                .route-class { color: #6c757d; margin: 5px 0; }
                .route-file { color: #495057; margin: 5px 0; font-size: 0.9em; }
                .route-params { color: #28a745; margin: 5px 0; }
                .expand-icon { 
                    font-family: monospace; 
                    font-weight: bold; 
                    transition: transform 0.2s;
                }
                .expand-icon.expanded { transform: rotate(90deg); }
                .nested { margin-left: 20px; }
                .block-container { 
                    margin: 5px 0; 
                    border: 1px solid #ffc107; 
                    border-radius: 4px; 
                }
                .block-header { 
                    padding: 8px; 
                    background: #fff3cd; 
                    cursor: pointer;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                .block-header:hover { background: #ffeaa7; }
                .api-container { 
                    margin: 5px 0; 
                    border: 1px solid #dc3545; 
                    border-radius: 4px; 
                }
                .api-header { 
                    padding: 8px; 
                    background: #f8d7da; 
                    cursor: pointer;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                .api-header:hover { background: #f1b0b7; }
                .blocks-label, .apis-label { 
                    margin: 10px 0 5px 20px; 
                    font-weight: bold; 
                    color: #495057; 
                }
            </style>
            <script>
                function toggleExpand(element) {
                    const details = element.nextElementSibling;
                    const icon = element.querySelector('.expand-icon');
                    
                    if (details.classList.contains('expanded')) {
                        details.classList.remove('expanded');
                        icon.classList.remove('expanded');
                    } else {
                        details.classList.add('expanded');
                        icon.classList.add('expanded');
                    }
                }
            </script>
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
            // Find associated blocks for this page
            val associatedBlocks = findAssociatedBlocks(page, pageBlockRoutes)

            html.append(generateExpandablePageWithBlocks(page, associatedBlocks, pageBlockApiRoutes))
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
            html.append(generateExpandableRouteHtml(api, "route"))
        }

        return html.toString()
    }

    /**
     * Generates HTML for a page with its nested blocks
     */
    private fun generateExpandablePageWithBlocks(
        page: RouteMapping,
        blocks: List<RouteMapping>,
        pageBlockApiRoutes: List<RouteMapping>,
    ): String {
        val html = StringBuilder()

        html.append("<div class=\"route-container\">\n")
        html.append("  <div class=\"route-header\" onclick=\"toggleExpand(this)\">\n")
        html.append("    <span class=\"route-path\">${page.routePath}</span>\n")
        html.append("    <span class=\"expand-icon\">&gt;</span>\n")
        html.append("  </div>\n")
        html.append("  <div class=\"route-details\">\n")

        val filePath = getRelativeFilePath(page.routeClass)
        html.append("    <div class=\"route-class\">Class: ${page.routeClass.simpleName}</div>\n")
        html.append("    <div class=\"route-file\">File: $filePath</div>\n")

        // Add parameters if they exist
        if (page.parameters.isNotEmpty()) {
            val paramsList =
                page.parameters.joinToString(", ") { param ->
                    val typeName =
                        param.type
                            .toString()
                            .substringAfterLast('.')
                            .substringBefore('?')
                    "${param.name} ($typeName)"
                }
            html.append("    <div class=\"route-params\">Parameters: $paramsList</div>\n")
        }

        // Add blocks if they exist
        if (blocks.isNotEmpty()) {
            html.append("    <div class=\"blocks-label\">Blocks:</div>\n")
            for (block in blocks) {
                html.append("    ")
                html.append(generateExpandableBlockWithApis(block, pageBlockApiRoutes, "margin-left: 20px;"))
            }
        }

        html.append("  </div>\n")
        html.append("</div>\n")

        return html.toString()
    }

    /**
     * Generates HTML for a block with its nested APIs
     */
    private fun generateExpandableBlockWithApis(
        block: RouteMapping,
        pageBlockApiRoutes: List<RouteMapping>,
        additionalStyle: String = "",
    ): String {
        val styleAttr = if (additionalStyle.isNotEmpty()) " style=\"$additionalStyle\"" else ""
        val html = StringBuilder()

        html.append("<div class=\"block-container\"$styleAttr>\n")
        html.append("  <div class=\"block-header\" onclick=\"toggleExpand(this)\">\n")
        html.append("    <span class=\"route-path\">${block.routePath}</span>\n")
        html.append("    <span class=\"expand-icon\">&gt;</span>\n")
        html.append("  </div>\n")
        html.append("  <div class=\"route-details\">\n")

        val filePath = getRelativeFilePath(block.routeClass)
        html.append("    <div class=\"route-class\">Class: ${block.routeClass.simpleName}</div>\n")
        html.append("    <div class=\"route-file\">File: $filePath</div>\n")

        // Add parameters if they exist
        if (block.parameters.isNotEmpty()) {
            val paramsList =
                block.parameters.joinToString(", ") { param ->
                    val typeName =
                        param.type
                            .toString()
                            .substringAfterLast('.')
                            .substringBefore('?')
                    "${param.name} ($typeName)"
                }
            html.append("    <div class=\"route-params\">Parameters: $paramsList</div>\n")
        }

        // Find associated APIs for this block
        val associatedApis = findAssociatedBlockApis(block, pageBlockApiRoutes)
        if (associatedApis.isNotEmpty()) {
            html.append("    <div class=\"apis-label\">Block APIs:</div>\n")
            for (api in associatedApis) {
                html.append("    ")
                html.append(generateExpandableRouteHtml(api, "api", "api", "margin-left: 20px;"))
            }
        }

        html.append("  </div>\n")
        html.append("</div>\n")

        return html.toString()
    }

    /**
     * Generates HTML for a single expandable route
     */
    private fun generateExpandableRouteHtml(
        route: RouteMapping,
        routeType: String,
        containerType: String = "route",
        additionalStyle: String = "",
    ): String {
        val styleAttr = if (additionalStyle.isNotEmpty()) " style=\"$additionalStyle\"" else ""
        val html = StringBuilder()
        val containerId = "route-${route.routePath.replace(
            "/",
            "-",
        ).replace("{", "").replace("}", "")}-${route.routeClass.simpleName}"

        html.append("<div class=\"$containerType-container\"$styleAttr>\n")
        html.append("  <div class=\"$containerType-header\" onclick=\"toggleExpand(this)\">\n")
        html.append("    <span class=\"route-path\">${route.routePath}</span>\n")
        html.append("    <span class=\"expand-icon\">&gt;</span>\n")
        html.append("  </div>\n")
        html.append("  <div class=\"route-details\">\n")

        val filePath = getRelativeFilePath(route.routeClass)
        html.append("    <div class=\"route-class\">Class: ${route.routeClass.simpleName}</div>\n")
        html.append("    <div class=\"route-file\">File: $filePath</div>\n")

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
            html.append("    <div class=\"route-params\">Parameters: $paramsList</div>\n")
        }

        html.append("  </div>\n")
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
