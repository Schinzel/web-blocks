package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_registry.response_handlers.IResponseHandler
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_mapping.RouteMapping

fun createResponseHandler(
    routeMapping: RouteMapping,
    ctx: Context,
    log: Log
): IResponseHandler {
    // Get arguments from from the request
    val arguments: Map<String, Any?> = getArguments(routeMapping.parameters, ctx)
    // Log the arguments
    log.requestLog.arguments = arguments
    val constructor = routeMapping.getPrimaryConstructor()
    // Create instance of route class with arguments
    return routeMapping.getPrimaryConstructor().callBy(
        constructor.parameters.associateWith { param ->
            arguments[param.name]
        }
    )
}