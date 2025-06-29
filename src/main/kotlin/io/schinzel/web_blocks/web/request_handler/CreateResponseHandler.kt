package io.schinzel.web_blocks.web.request_handler

import io.javalin.http.Context
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.request_handler.log.LogEntry
import io.schinzel.web_blocks.web.route_mapping.RouteMapping

fun createRoute(
    routeMapping: RouteMapping,
    ctx: Context,
    logEntry: LogEntry
): IRoute {
    // Get arguments from from the request
    val arguments: Map<String, Any?> = getArguments(routeMapping.parameters, ctx)
    // Log the arguments
    logEntry.requestLog.arguments = arguments
    val constructor = routeMapping.getPrimaryConstructor()
    // Create instance of route class with arguments
    return constructor.callBy(
        constructor.parameters.associateWith { param ->
            arguments[param.name]
        }
    )
}