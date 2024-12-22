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

class RequestHandler(
    private val endpoint: Endpoint,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
) {
    fun handle(): (Context) -> Unit {
        return { ctx: Context ->
            val log = Log(
                localTimeZone = localTimezone,
                routeType = endpoint.getType(),
                httpMethod = ctx.method().toString()
            )
            val startTime = System.currentTimeMillis()
            try {
                log.requestLog.path = endpoint.getPath()
                val hasNoArguments = endpoint.parameters.isEmpty()
                // Create instance of route class
                val routeClassInstance: IWebResponse = when {
                    hasNoArguments -> endpoint.clazz.createInstance()
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

    private fun createInstanceWithArguments(ctx: Context, log: Log): IWebResponse {
        val arguments: Map<String, String> = getArguments(endpoint.parameters, ctx)
        log.requestLog.arguments = arguments
        val constructor = endpoint.clazz.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${endpoint.clazz.simpleName}")
        return constructor.callBy(
            constructor.parameters.associateWith { param ->
                arguments[param.name]
            }
        )
    }
}

