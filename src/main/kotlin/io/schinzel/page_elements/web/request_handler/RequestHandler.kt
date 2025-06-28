package io.schinzel.page_elements.web.request_handler

import io.javalin.http.Context
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.request_handler.log.LogEntry
import io.schinzel.page_elements.web.route_mapping.RouteMapping
import io.schinzel.page_elements.web.routes.IRoute
import kotlin.reflect.full.createInstance

/**
 * The purpose of this class is to handle a request.
 */
class RequestHandler(
    private val routeMapping: RouteMapping,
    private val webAppConfig: WebAppConfig,
) {

    fun getHandler(): (Context) -> Unit {
        return { ctx: Context ->
            val returnType = routeMapping.returnType
            // Create log
            val logEntry = LogEntry(
                localTimeZone = webAppConfig.localTimezone,
                routeType = routeMapping.type,
                httpMethod = ctx.method().toString()
            )
            // Set the start time
            val startTime = System.currentTimeMillis()
            // Log the request path
            logEntry.requestLog.path = routeMapping.path
            logEntry.requestLog.requestBody = ctx.body()
            // Check if route has arguments
            val hasNoArguments = routeMapping.parameters.isEmpty()
            // Create instance of route class
            val route: IRoute = when {
                hasNoArguments -> routeMapping.routeClass.createInstance()
                else -> createRoute(routeMapping, ctx, logEntry)
            }
            // Send response
            sendResponse(ctx, route, logEntry, returnType, webAppConfig.prettyFormatHtml)

            // Log the response status code
            logEntry.responseLog.statusCode = ctx.statusCode()
            // Log the execution time
            logEntry.executionTimeInMs = System.currentTimeMillis() - startTime
            // Log the request and response
            webAppConfig.logger.log(logEntry)

        }
    }
}
