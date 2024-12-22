package io.schinzel.page_elements.route_handler

import io.javalin.http.Context
import io.schinzel.page_elements.endpoint.Endpoint
import io.schinzel.page_elements.route_handler.log.ErrorLog
import io.schinzel.page_elements.route_handler.log.ILogger
import io.schinzel.page_elements.route_handler.log.Log
import io.schinzel.page_elements.route_handler.log.PrettyConsoleLogger
import io.schinzel.page_elements.web_response.IWebResponse
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

fun createRouteHandler(
    endpoint: Endpoint,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
): (Context) -> Unit {
    return { ctx: Context ->
        val log =
            Log(localTimeZone = localTimezone, routeType = endpoint.getType(), httpMethod = ctx.method().toString())
        val startTime = System.currentTimeMillis()
        try {
            log.requestLog.path = endpoint.getPath()
            val hasNoArguments = endpoint.parameters.isEmpty()
            // Create instance of route class
            val routeClassInstance: IWebResponse = when {
                // If no arguments, use no-argument constructor
                hasNoArguments -> endpoint.clazz.createInstance()
                else -> {
                    // If arguments, use constructor with arguments
                    val arguments: Map<String, String> = getArguments(endpoint.parameters, ctx)
                    log.requestLog.arguments = arguments
                    // Get constructor
                    val constructor = endpoint.clazz.primaryConstructor
                        ?: throw IllegalStateException("No primary constructor found for ${endpoint.clazz.simpleName}")
                    // Create instance with parameters
                    constructor.callBy(
                        constructor.parameters.associateWith { param ->
                            arguments[param.name]
                        }
                    )
                }
            }
            sendResponse(ctx, routeClassInstance, log)
            log.responseLog.statusCode = ctx.statusCode()
            log.executionTimeInMs = System.currentTimeMillis() - startTime
        } catch (e: Exception) {
            log.executionTimeInMs = System.currentTimeMillis() - startTime
            log.errorLog = ErrorLog(e)
        }
        logger.log(log)
    }
}

