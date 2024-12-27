package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_registry.response_handlers.IResponseHandler
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_mapping.ResponseHandlerMapping

fun createResponseHandler(
    responseHandlerMapping: ResponseHandlerMapping,
    ctx: Context,
    log: Log
): IResponseHandler {
    // Get arguments from from the request
    val arguments: Map<String, Any?> = getArguments(responseHandlerMapping.parameters, ctx)
    // Log the arguments
    log.requestLog.arguments = arguments
    val constructor = responseHandlerMapping.getPrimaryConstructor()
    // Create instance of route class with arguments
    return constructor.callBy(
        constructor.parameters.associateWith { param ->
            arguments[param.name]
        }
    )
}