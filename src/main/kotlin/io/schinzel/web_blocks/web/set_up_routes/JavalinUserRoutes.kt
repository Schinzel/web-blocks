package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.request_handler.RequestHandler
import io.schinzel.web_blocks.web.route_mapping.RouteMapping

/**
 * The purpose of this class is to provide user routes extension for Javalin setup.
 * Registers user-defined routes discovered from the application package.
 *
 * Written by Claude Sonnet 4
 */
fun Javalin.setupUserRoutes(
    webAppConfig: WebAppConfig,
    routeMappings: List<RouteMapping>,
): Javalin {
    // Register user routes
    routeMappings.forEach { routeMapping: RouteMapping ->
        if (webAppConfig.printStartupMessages) {
            // Print the route
            routeMapping.println()
        }
        // Create request handler
        val requestHandler =
            RequestHandler(routeMapping, webAppConfig)
                .getHandler()
        // Register both GET and POST handlers for the same path
        this.getAndPost(routeMapping.routePath, requestHandler)
    }
    return this
}

private fun Javalin.getAndPost(
    path: String,
    handler: (ctx: io.javalin.http.Context) -> Unit,
) {
    this.get(path, handler)
    this.post(path, handler)
}
