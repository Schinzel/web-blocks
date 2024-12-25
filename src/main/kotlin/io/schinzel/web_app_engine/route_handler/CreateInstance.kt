package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_registry.processors.IRequestProcessor
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_mapping.RouteMapping

fun createInstance(
    routeMapping: RouteMapping,
    ctx: Context,
    log: Log
): IRequestProcessor {
    // Get arguments from from the request
    val arguments: Map<String, String> = getArguments(routeMapping.parameters, ctx)
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