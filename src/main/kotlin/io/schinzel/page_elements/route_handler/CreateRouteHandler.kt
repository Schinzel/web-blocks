package io.schinzel.page_elements.route_handler

import io.javalin.http.Context
import io.schinzel.page_elements.route.Route
import io.schinzel.page_elements.route.log.ErrorLog
import io.schinzel.page_elements.route.log.ILogger
import io.schinzel.page_elements.route.log.Log
import io.schinzel.page_elements.route.log.PrettyConsoleLogger
import io.schinzel.page_elements.web_response.IWebResponse
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

fun createRouteHandler(
    route: Route,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
): (Context) -> Unit {
    return { ctx: Context ->
        val log =
            Log(localTimeZone = localTimezone, routeType = route.getType(), httpMethod = ctx.method().toString())
        val startTime = System.currentTimeMillis()
        try {
            log.requestLog.path = route.getPath()
            val hasNoArguments = route.parameters.isEmpty()
            // Create instance of route class
            val routeClassInstance: IWebResponse = when {
                // If no arguments, use no-argument constructor
                hasNoArguments -> route.clazz.createInstance()
                else -> {
                    // If arguments, use constructor with arguments
                    val arguments: Map<String, String> = getArguments(route.parameters, ctx)
                    log.requestLog.arguments = arguments
                    // Get constructor
                    val constructor = route.clazz.primaryConstructor
                        ?: throw IllegalStateException("No primary constructor found for ${route.clazz.simpleName}")
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