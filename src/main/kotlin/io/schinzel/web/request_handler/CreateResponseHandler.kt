package io.schinzel.web.request_handler

import io.javalin.http.Context
import io.schinzel.web.response_handlers.response_handlers.IResponseHandler
import io.schinzel.web.request_handler.log.LogEntry
import io.schinzel.web.response_handler_mapping.ResponseHandlerMapping

fun createResponseHandler(
    responseHandlerMapping: ResponseHandlerMapping,
    ctx: Context,
    logEntry: LogEntry
): IResponseHandler {
    // Get arguments from from the request
    val arguments: Map<String, Any?> = getArguments(responseHandlerMapping.parameters, ctx)
    // Log the arguments
    logEntry.requestLog.arguments = arguments
    val constructor = responseHandlerMapping.getPrimaryConstructor()
    // Create instance of route class with arguments
    return constructor.callBy(
        constructor.parameters.associateWith { param ->
            arguments[param.name]
        }
    )
}