package io.schinzel.web_blocks.web.routes_overview

import io.schinzel.web_blocks.web.route_mapping.RouteMapping

/**
 * The purpose of this class is to generate an HTML page displaying all registered
 * routes in the WebBlocks application with their class names, parameters, and
 * hierarchical relationships.
 *
 * This class serves as the main coordinator, delegating specific concerns to:
 * - HtmlTemplateGenerator: HTML structure and CSS
 * - RouteProcessor: Route grouping and association logic
 * - RouteHtmlGenerator: HTML content generation for routes
 * - HtmlEscapeUtil: Security and escaping utilities
 *
 * Written by Claude Sonnet 4
 */
class RoutesOverviewPageGenerator(
    private val templateGenerator: HtmlTemplateGenerator = HtmlTemplateGenerator(),
    private val routeProcessor: RouteProcessor = RouteProcessor(),
    private val htmlGenerator: RouteHtmlGenerator = RouteHtmlGenerator(routeProcessor),
) {
    /**
     * Generates HTML for the routes overview page
     * @param routeMappings List of all route mappings in the application
     * @return Complete HTML page as string
     */
    fun generateHtml(routeMappings: List<RouteMapping>): String {
        // Group routes by type
        val groupedRoutes = routeProcessor.groupRoutesByType(routeMappings)

        // Build HTML
        val html = StringBuilder()
        html.append(templateGenerator.generateHeader())
        html.append(
            htmlGenerator.generatePagesSection(
                groupedRoutes.pageRoutes,
                groupedRoutes.pageBlockRoutes,
                groupedRoutes.pageBlockApiRoutes,
            ),
        )
        html.append(htmlGenerator.generateApiSection(groupedRoutes.apiRoutes))
        html.append(templateGenerator.generateFooter())

        return html.toString()
    }
}
