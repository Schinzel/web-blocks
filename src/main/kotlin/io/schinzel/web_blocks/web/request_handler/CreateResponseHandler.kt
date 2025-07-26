package io.schinzel.web_blocks.web.request_handler

import io.javalin.http.Context
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry
import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import io.schinzel.web_blocks.web.routes.IRoute

fun createRoute(
    routeMapping: RouteMapping,
    ctx: Context,
    logEntry: LogEntry,
    requestBody: String,
    isMultipart: Boolean,
): IRoute {
    // Get arguments from the request
    val arguments: Map<String, Any?> = getArguments(routeMapping.parameters, ctx, requestBody, isMultipart)
    // Log the arguments
    logEntry.requestLog.arguments = arguments
    val constructor = routeMapping.getPrimaryConstructor()
    // Create instance of route class with arguments
    return constructor.callBy(
        constructor.parameters.associateWith { param ->
            arguments[param.name]
        },
    )
}
