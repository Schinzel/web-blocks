package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.IRequestProcessor
import io.schinzel.web_app_engine.route_handler.log.ErrorLog
import io.schinzel.web_app_engine.route_handler.log.ILogger
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_mapping.RouteMapping
import io.schinzel.web_app_engine.route_mapping.RouteMapping2
import kotlin.reflect.full.createInstance

class RequestHandler(
    private val routeMapping: RouteMapping,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
) {
    fun handle(): (Context) -> Unit {
        return { ctx: Context ->
            // Create log
            val log = Log(
                localTimeZone = localTimezone,
                routeType = routeMapping.getType(),
                httpMethod = ctx.method().toString()
            )
            // Set the start time
            val startTime = System.currentTimeMillis()
            try {
                // Log the request path
                log.requestLog.path = routeMapping.getRoutePath()
                // Check if route has arguments
                val hasNoArguments = routeMapping.parameters.isEmpty()
                // Create instance of route class
                val routeClassInstance: IRequestProcessor = when {
                    hasNoArguments -> routeMapping.clazz.createInstance()
                    else -> createInstance(routeMapping, ctx, log)
                }
                // Send response
                sendResponse(ctx, routeClassInstance, log)
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

class RequestHandler2(
    private val routeMapping: RouteMapping2,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
) {

    fun handle(): (Context) -> Unit {
        return { ctx: Context ->
            // Create log
            val log = Log(
                localTimeZone = localTimezone,
                routeType = routeMapping.type,
                httpMethod = ctx.method().toString()
            )
            // Set the start time
            val startTime = System.currentTimeMillis()
            try {
                // Log the request path
                log.requestLog.path = routeMapping.path
                // Check if route has arguments
                val hasNoArguments = routeMapping.parameters.isEmpty()
                // Create instance of route class
                val routeClassInstance: IRequestProcessor = when {
                    hasNoArguments -> routeMapping.clazz.createInstance()
                    else -> createInstance2(routeMapping, ctx, log)
                }
                // Send response
                sendResponse(ctx, routeClassInstance, log)
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

    companion object {

        fun createInstance2(
            routeMapping: RouteMapping2,
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
    }
}