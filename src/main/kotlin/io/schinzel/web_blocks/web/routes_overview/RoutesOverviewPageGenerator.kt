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
                body { 
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                    background-color: #f5f7fa;
                    margin: 0;
                    padding: 30px;
                    line-height: 1.6;
                    color: #2d3748;
                }
                
                h1 { 
                    color: #2d3748; 
                    font-size: 2.5rem;
                    font-weight: 600;
                    margin-bottom: 10px;
                }
                
                h2 { 
                    color: #4a5568; 
                    font-size: 1.5rem;
                    font-weight: 600;
                    margin: 40px 0 20px 0;
                    border-bottom: 2px solid #e2e8f0;
                    padding-bottom: 10px;
                }
                
                .route-container, .block-container, .api-container { 
                    background: white;
                    border-radius: 8px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    margin-bottom: 16px;
                    border: 1px solid #e2e8f0;
                    overflow: hidden;
                }
                
                .route-header, .block-header, .api-header { 
                    padding: 20px; 
                    cursor: pointer;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    transition: background-color 0.2s ease;
                    border-bottom: 1px solid #e2e8f0;
                }
                
                .route-header { 
                    background: #f8fafc;
                    border-left: 4px solid #3182ce;
                }
                .route-header:hover { background: #edf2f7; }
                
                .block-header { 
                    background: #fffbf0;
                    border-left: 4px solid #ed8936;
                }
                .block-header:hover { background: #fef5e7; }
                
                .api-header { 
                    background: #f0fff4;
                    border-left: 4px solid #38a169;
                }
                .api-header:hover { background: #e6fffa; }
                
                .route-path { 
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    font-weight: 600;
                    font-size: 1.1rem;
                    color: #2d3748;
                }
                
                .expand-icon { 
                    font-size: 1.2rem;
                    transition: transform 0.2s ease;
                    color: #718096;
                }
                .expand-icon.expanded { transform: rotate(90deg); }
                
                .route-details { 
                    padding: 0;
                    background: #ffffff;
                    display: none;
                }
                .route-details.expanded { display: block; }
                
                .details-content {
                    padding: 20px;
                }
                
                .info-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 16px 0;
                    background: white;
                    border-radius: 6px;
                    overflow: hidden;
                    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                }
                
                .info-table th {
                    background: #f7fafc;
                    padding: 12px 16px;
                    text-align: left;
                    font-weight: 600;
                    color: #4a5568;
                    border-bottom: 1px solid #e2e8f0;
                    font-size: 0.875rem;
                }
                
                .info-table td {
                    padding: 12px 16px;
                    border-bottom: 1px solid #f1f5f9;
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    font-size: 0.875rem;
                    color: #2d3748;
                }
                
                .params-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 16px 0;
                    background: white;
                    border-radius: 6px;
                    overflow: hidden;
                    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                }
                
                .params-table th {
                    background: #f7fafc;
                    padding: 12px 16px;
                    text-align: left;
                    font-weight: 600;
                    color: #4a5568;
                    border-bottom: 1px solid #e2e8f0;
                    font-size: 0.875rem;
                }
                
                .params-table td {
                    padding: 12px 16px;
                    border-bottom: 1px solid #f1f5f9;
                    font-size: 0.875rem;
                }
                
                .param-name {
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    font-weight: 600;
                    color: #2d3748;
                }
                
                .param-type {
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    color: #805ad5;
                    background: #faf5ff;
                    padding: 2px 6px;
                    border-radius: 4px;
                    font-size: 0.75rem;
                }
                
                .blocks-label, .apis-label { 
                    margin: 20px 0 12px 0;
                    font-weight: 600; 
                    color: #4a5568;
                    font-size: 1.1rem;
                }
                
                .nested-blocks, .nested-apis {
                    margin-left: 0;
                }
                
                .page-bottom {
                    margin-bottom: 40px;
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
        <div class="page-bottom"></div>
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
        html.append("    <div class=\"details-content\">\n")

        // Class and File table
        val filePath = getRelativeFilePath(page.routeClass)
        html.append("      <table class=\"info-table\">\n")
        html.append("        <thead>\n")
        html.append("          <tr>\n")
        html.append("            <th>Property</th>\n")
        html.append("            <th>Value</th>\n")
        html.append("          </tr>\n")
        html.append("        </thead>\n")
        html.append("        <tbody>\n")
        html.append("          <tr>\n")
        html.append("            <td>Class</td>\n")
        html.append("            <td>${page.routeClass.simpleName}</td>\n")
        html.append("          </tr>\n")
        html.append("          <tr>\n")
        html.append("            <td>File</td>\n")
        html.append("            <td>$filePath</td>\n")
        html.append("          </tr>\n")
        html.append("        </tbody>\n")
        html.append("      </table>\n")

        // Parameters table if they exist
        if (page.parameters.isNotEmpty()) {
            html.append("      <table class=\"params-table\">\n")
            html.append("        <thead>\n")
            html.append("          <tr>\n")
            html.append("            <th>Parameter</th>\n")
            html.append("            <th>Type</th>\n")
            html.append("          </tr>\n")
            html.append("        </thead>\n")
            html.append("        <tbody>\n")
            for (param in page.parameters) {
                val typeName =
                    param.type
                        .toString()
                        .substringAfterLast('.')
                        .substringBefore('?')
                html.append("          <tr>\n")
                html.append("            <td><span class=\"param-name\">${param.name}</span></td>\n")
                html.append("            <td><span class=\"param-type\">$typeName</span></td>\n")
                html.append("          </tr>\n")
            }
            html.append("        </tbody>\n")
            html.append("      </table>\n")
        }

        // Add blocks if they exist
        if (blocks.isNotEmpty()) {
            html.append("      <div class=\"blocks-label\">Blocks:</div>\n")
            html.append("      <div class=\"nested-blocks\">\n")
            for (block in blocks) {
                html.append("        ")
                html.append(generateExpandableBlockWithApis(block, pageBlockApiRoutes))
            }
            html.append("      </div>\n")
        }

        html.append("    </div>\n") // Close details-content
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
        html.append("    <div class=\"details-content\">\n")

        // Class and File table
        val filePath = getRelativeFilePath(block.routeClass)
        html.append("      <table class=\"info-table\">\n")
        html.append("        <thead>\n")
        html.append("          <tr>\n")
        html.append("            <th>Property</th>\n")
        html.append("            <th>Value</th>\n")
        html.append("          </tr>\n")
        html.append("        </thead>\n")
        html.append("        <tbody>\n")
        html.append("          <tr>\n")
        html.append("            <td>Class</td>\n")
        html.append("            <td>${block.routeClass.simpleName}</td>\n")
        html.append("          </tr>\n")
        html.append("          <tr>\n")
        html.append("            <td>File</td>\n")
        html.append("            <td>$filePath</td>\n")
        html.append("          </tr>\n")
        html.append("        </tbody>\n")
        html.append("      </table>\n")

        // Parameters table if they exist
        if (block.parameters.isNotEmpty()) {
            html.append("      <table class=\"params-table\">\n")
            html.append("        <thead>\n")
            html.append("          <tr>\n")
            html.append("            <th>Parameter</th>\n")
            html.append("            <th>Type</th>\n")
            html.append("          </tr>\n")
            html.append("        </thead>\n")
            html.append("        <tbody>\n")
            for (param in block.parameters) {
                val typeName =
                    param.type
                        .toString()
                        .substringAfterLast('.')
                        .substringBefore('?')
                html.append("          <tr>\n")
                html.append("            <td><span class=\"param-name\">${param.name}</span></td>\n")
                html.append("            <td><span class=\"param-type\">$typeName</span></td>\n")
                html.append("          </tr>\n")
            }
            html.append("        </tbody>\n")
            html.append("      </table>\n")
        }

        // Find associated APIs for this block
        val associatedApis = findAssociatedBlockApis(block, pageBlockApiRoutes)
        if (associatedApis.isNotEmpty()) {
            html.append("      <div class=\"apis-label\">Block APIs:</div>\n")
            html.append("      <div class=\"nested-apis\">\n")
            for (api in associatedApis) {
                html.append("        ")
                html.append(generateExpandableRouteHtml(api, "api", "api"))
            }
            html.append("      </div>\n")
        }

        html.append("    </div>\n") // Close details-content

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
        html.append("    <div class=\"details-content\">\n")

        // Class and File table
        val filePath = getRelativeFilePath(route.routeClass)
        html.append("      <table class=\"info-table\">\n")
        html.append("        <thead>\n")
        html.append("          <tr>\n")
        html.append("            <th>Property</th>\n")
        html.append("            <th>Value</th>\n")
        html.append("          </tr>\n")
        html.append("        </thead>\n")
        html.append("        <tbody>\n")
        html.append("          <tr>\n")
        html.append("            <td>Class</td>\n")
        html.append("            <td>${route.routeClass.simpleName}</td>\n")
        html.append("          </tr>\n")
        html.append("          <tr>\n")
        html.append("            <td>File</td>\n")
        html.append("            <td>$filePath</td>\n")
        html.append("          </tr>\n")
        html.append("        </tbody>\n")
        html.append("      </table>\n")

        // Parameters table if they exist
        if (route.parameters.isNotEmpty()) {
            html.append("      <table class=\"params-table\">\n")
            html.append("        <thead>\n")
            html.append("          <tr>\n")
            html.append("            <th>Parameter</th>\n")
            html.append("            <th>Type</th>\n")
            html.append("          </tr>\n")
            html.append("        </thead>\n")
            html.append("        <tbody>\n")
            for (param in route.parameters) {
                val typeName =
                    param.type
                        .toString()
                        .substringAfterLast('.')
                        .substringBefore('?')
                html.append("          <tr>\n")
                html.append("            <td><span class=\"param-name\">${param.name}</span></td>\n")
                html.append("            <td><span class=\"param-type\">$typeName</span></td>\n")
                html.append("          </tr>\n")
            }
            html.append("        </tbody>\n")
            html.append("      </table>\n")
        }

        html.append("    </div>\n") // Close details-content
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
