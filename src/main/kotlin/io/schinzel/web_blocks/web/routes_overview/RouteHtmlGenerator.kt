package io.schinzel.web_blocks.web.routes_overview

import io.schinzel.web_blocks.web.route_mapping.RouteMapping

/**
 * Generates HTML content for individual routes and route sections.
 * Handles the creation of expandable route HTML with tables and parameters.
 *
 * Written by Claude Sonnet 4
 */
class RouteHtmlGenerator(
    private val routeProcessor: RouteProcessor = RouteProcessor(),
) {
    /**
     * Generates the Pages & Components section
     */
    fun generatePagesSection(
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
            val associatedBlocks = routeProcessor.findAssociatedBlocks(page, pageBlockRoutes)
            html.append(generateExpandablePageWithBlocks(page, associatedBlocks, pageBlockApiRoutes))
        }

        return html.toString()
    }

    /**
     * Generates the API Routes section
     */
    fun generateApiSection(apiRoutes: List<RouteMapping>): String {
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
    fun generateExpandablePageWithBlocks(
        page: RouteMapping,
        blocks: List<RouteMapping>,
        pageBlockApiRoutes: List<RouteMapping>,
    ): String {
        val html = StringBuilder()

        html.append("<div class=\"route-container\">\n")
        html.append("  <div class=\"route-header\" onclick=\"toggleExpand(this)\">\n")
        html.append("    <span class=\"route-path\">${HtmlEscapeUtil.escapeHtml(page.routePath)}</span>\n")
        html.append("    <span class=\"expand-icon\">&gt;</span>\n")
        html.append("  </div>\n")
        html.append("  <div class=\"route-details\">\n")
        html.append("    <div class=\"details-content\">\n")

        // Class and File table
        html.append(generateInfoTable(page))

        // Parameters table if they exist
        if (page.parameters.isNotEmpty()) {
            html.append(generateParametersTable(page))
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
    fun generateExpandableBlockWithApis(
        block: RouteMapping,
        pageBlockApiRoutes: List<RouteMapping>,
        additionalStyle: String = "",
    ): String {
        val styleAttr = if (additionalStyle.isNotEmpty()) " style=\"$additionalStyle\"" else ""
        val html = StringBuilder()

        html.append("<div class=\"block-container\"$styleAttr>\n")
        html.append("  <div class=\"block-header\" onclick=\"toggleExpand(this)\">\n")
        html.append("    <span class=\"route-path\">${HtmlEscapeUtil.escapeHtml(block.routePath)}</span>\n")
        html.append("    <span class=\"expand-icon\">&gt;</span>\n")
        html.append("  </div>\n")
        html.append("  <div class=\"route-details\">\n")
        html.append("    <div class=\"details-content\">\n")

        // Class and File table
        html.append(generateInfoTable(block))

        // Parameters table if they exist
        if (block.parameters.isNotEmpty()) {
            html.append(generateParametersTable(block))
        }

        // Find associated APIs for this block
        val associatedApis = routeProcessor.findAssociatedBlockApis(block, pageBlockApiRoutes)
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
    fun generateExpandableRouteHtml(
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
        ).replace("{", "").replace("}", "")}-${HtmlEscapeUtil.escapeHtml(route.routeClass.simpleName)}"

        html.append("<div class=\"$containerType-container\"$styleAttr>\n")
        html.append("  <div class=\"$containerType-header\" onclick=\"toggleExpand(this)\">\n")
        html.append("    <span class=\"route-path\">${HtmlEscapeUtil.escapeHtml(route.routePath)}</span>\n")
        html.append("    <span class=\"expand-icon\">&gt;</span>\n")
        html.append("  </div>\n")
        html.append("  <div class=\"route-details\">\n")
        html.append("    <div class=\"details-content\">\n")

        // Class and File table
        html.append(generateInfoTable(route))

        // Parameters table if they exist
        if (route.parameters.isNotEmpty()) {
            html.append(generateParametersTable(route))
        }

        html.append("    </div>\n") // Close details-content
        html.append("  </div>\n")
        html.append("</div>\n")

        return html.toString()
    }

    /**
     * Generates info table with class and file information
     */
    private fun generateInfoTable(route: RouteMapping): String {
        val filePath = HtmlEscapeUtil.getRelativeFilePath(route.routeClass)
        return """
            <table class="info-table">
              <thead>
                <tr>
                  <th>Property</th>
                  <th>Value</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>Class</td>
                  <td>${HtmlEscapeUtil.escapeHtml(route.routeClass.simpleName)}</td>
                </tr>
                <tr>
                  <td>File</td>
                  <td>${HtmlEscapeUtil.escapeHtml(filePath)}</td>
                </tr>
              </tbody>
            </table>
            """.trimIndent()
    }

    /**
     * Generates parameters table
     */
    private fun generateParametersTable(route: RouteMapping): String {
        val html = StringBuilder()
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
            html.append(
                "            <td><span class=\"param-name\">${HtmlEscapeUtil.escapeHtml(param.name)}</span></td>\n",
            )
            html.append(
                "            <td><span class=\"param-type\">${HtmlEscapeUtil.escapeHtml(typeName)}</span></td>\n",
            )
            html.append("          </tr>\n")
        }
        html.append("        </tbody>\n")
        html.append("      </table>\n")
        return html.toString()
    }
}
