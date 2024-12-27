package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_registry.response_handlers.IResponseHandler
import io.schinzel.web_app_engine.route_handler.log.ErrorLog
import io.schinzel.web_app_engine.route_handler.log.ILogger
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_mapping.ResponseHandlerMapping
import kotlin.reflect.full.createInstance

class RequestHandler(
    private val responseHandlerMapping: ResponseHandlerMapping,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
) {

    fun handle(): (Context) -> Unit {
        return { ctx: Context ->
            // Create log
            val log = Log(
                localTimeZone = localTimezone,
                routeType = responseHandlerMapping.type,
                httpMethod = ctx.method().toString()
            )
            // Set the start time
            val startTime = System.currentTimeMillis()
            try {
                // Log the request path
                log.requestLog.path = responseHandlerMapping.path
                // Check if route has arguments
                val hasNoArguments = responseHandlerMapping.parameters.isEmpty()
                // Create instance of route class
                val responseHandler: IResponseHandler = when {
                    hasNoArguments -> responseHandlerMapping.responseHandlerClass.createInstance()
                    else -> createResponseHandler(responseHandlerMapping, ctx, log)
                }
                // Send response
                sendResponse(ctx, responseHandler, log)
                // Log the response status code
                log.responseLog.statusCode = ctx.statusCode()
            } catch (e: Exception) {
                log.errorLog = ErrorLog(e)
            } finally {
                // Log the execution time
                log.executionTimeInMs = System.currentTimeMillis() - startTime
                // Write log
                logger.log(log)
            }
        }
    }
}