package io.schinzel.page_elements.route_handler

import io.javalin.http.Context
import io.schinzel.page_elements.route_mapping.RouteMapping
import io.schinzel.page_elements.route_handler.log.ErrorLog
import io.schinzel.page_elements.route_handler.log.ILogger
import io.schinzel.page_elements.route_handler.log.Log
import io.schinzel.page_elements.route_handler.log.PrettyConsoleLogger
import io.schinzel.page_elements.web_response.IRequestProcessor
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class RequestHandler(
    private val routeMapping: RouteMapping,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
) {
    fun handle(): (Context) -> Unit {
        return { ctx: Context ->
            val log = Log(
                localTimeZone = localTimezone,
                routeType = routeMapping.getType(),
                httpMethod = ctx.method().toString()
            )
            val startTime = System.currentTimeMillis()
            try {
                log.requestLog.path = routeMapping.getRoute()
                val hasNoArguments = routeMapping.parameters.isEmpty()
                // Create instance of route class
                val routeClassInstance: IRequestProcessor = when {
                    hasNoArguments -> routeMapping.clazz.createInstance()
                    else -> createInstanceWithArguments(ctx, log)
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

    private fun createInstanceWithArguments(ctx: Context, log: Log): IRequestProcessor {
        val arguments: Map<String, String> = getArguments(routeMapping.parameters, ctx)
        log.requestLog.arguments = arguments
        val constructor = routeMapping.clazz.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${routeMapping.clazz.simpleName}")
        return constructor.callBy(
            constructor.parameters.associateWith { param ->
                arguments[param.name]
            }
        )
    }
}

