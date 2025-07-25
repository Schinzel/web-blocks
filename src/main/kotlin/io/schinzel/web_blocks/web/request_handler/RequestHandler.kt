package io.schinzel.web_blocks.web.request_handler

import io.javalin.http.Context
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry
import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import io.schinzel.web_blocks.web.routes.IRoute
import kotlinx.coroutines.runBlocking
import kotlin.reflect.full.createInstance

/**
 * The purpose of this class is to handle a request.
 */
class RequestHandler(
    private val routeMapping: RouteMapping,
    private val webAppConfig: WebAppConfig,
) {
    fun getHandler(): (Context) -> Unit =
        { ctx: Context ->
            // Use runBlocking to handle suspend functions at the top level
            runBlocking {
                val returnType = routeMapping.returnType
                val logEntry =
                    LogEntry(
                        localTimeZone = webAppConfig.localTimezone,
                        routeType = routeMapping.type,
                        httpMethod = ctx.method().toString(),
                    )

                val startTime = System.currentTimeMillis()
                logEntry.requestLog.path = routeMapping.routePath

                // Only read body for non-multipart requests to avoid "body already consumed" errors
                val contentType = ctx.contentType() ?: ""
                val isMultipart = contentType.contains("multipart/form-data")
                val requestBody = if (isMultipart) "" else ctx.body()
                logEntry.requestLog.requestBody = requestBody

                val hasNoArguments = routeMapping.parameters.isEmpty()
                val route: IRoute =
                    when {
                        hasNoArguments -> routeMapping.routeClass.createInstance()
                        else -> createRoute(routeMapping, ctx, logEntry, requestBody, isMultipart)
                    }

                // Now this is a suspend call within the coroutine
                sendResponse(ctx, route, logEntry, returnType, webAppConfig.prettyFormatHtml)

                logEntry.responseLog.statusCode = ctx.statusCode()
                logEntry.executionTimeInMs = System.currentTimeMillis() - startTime
                webAppConfig.logger.log(logEntry)
            }
        }
}
