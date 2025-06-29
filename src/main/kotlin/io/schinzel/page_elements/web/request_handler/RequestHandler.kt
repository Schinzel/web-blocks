package io.schinzel.page_elements.web.request_handler

import io.javalin.http.Context
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.request_handler.log.LogEntry
import io.schinzel.page_elements.web.route_mapping.RouteMapping
import io.schinzel.page_elements.web.routes.IRoute
import kotlinx.coroutines.*
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
            // Use runBlocking to handle suspend functions at the top level
            runBlocking {
                val returnType = routeMapping.returnType
                val logEntry = LogEntry(
                    localTimeZone = webAppConfig.localTimezone,
                    routeType = routeMapping.type,
                    httpMethod = ctx.method().toString()
                )
                
                val startTime = System.currentTimeMillis()
                logEntry.requestLog.path = routeMapping.path
                logEntry.requestLog.requestBody = ctx.body()
                
                val hasNoArguments = routeMapping.parameters.isEmpty()
                val route: IRoute = when {
                    hasNoArguments -> routeMapping.routeClass.createInstance()
                    else -> createRoute(routeMapping, ctx, logEntry)
                }
                
                // Now this is a suspend call within the coroutine
                sendResponse(ctx, route, logEntry, returnType, webAppConfig.prettyFormatHtml)
                
                logEntry.responseLog.statusCode = ctx.statusCode()
                logEntry.executionTimeInMs = System.currentTimeMillis() - startTime
                webAppConfig.logger.log(logEntry)
            }
        }
    }
}
