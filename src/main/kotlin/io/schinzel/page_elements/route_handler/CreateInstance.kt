package io.schinzel.page_elements.route_handler

import io.javalin.http.Context
import io.schinzel.page_elements.route_handler.log.Log
import io.schinzel.page_elements.route_mapping.RouteMapping
import io.schinzel.page_elements.web_response.IRequestProcessor
import kotlin.reflect.full.primaryConstructor


/**
 * Creates an instance of the route class with constructor arguments applied.
 */
fun createInstance(routeMapping: RouteMapping, ctx: Context, log: Log): IRequestProcessor {
    // Get arguments from from the request
    val arguments: Map<String, String> = getArguments(routeMapping.parameters, ctx)
    // Log the arguments
    log.requestLog.arguments = arguments
    // Get the primary constructor of the route class
    val constructor = routeMapping.clazz.primaryConstructor
        ?: throw IllegalStateException("No primary constructor found for ${routeMapping.clazz.simpleName}")
    // Create instance of route class with arguments
    return constructor.callBy(
        constructor.parameters.associateWith { param ->
            arguments[param.name]
        }
    )
}