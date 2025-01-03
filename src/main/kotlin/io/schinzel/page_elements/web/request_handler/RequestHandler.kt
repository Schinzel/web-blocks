package io.schinzel.page_elements.web.request_handler

import io.javalin.http.Context
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.request_handler.log.ErrorLog
import io.schinzel.page_elements.web.request_handler.log.LogEntry
import io.schinzel.page_elements.web.response_handler_mapping.ResponseHandlerMapping
import io.schinzel.page_elements.web.response_handlers.IResponseHandler
import kotlin.reflect.full.createInstance

/**
 * The purpose of this class is to handle a request.
 */
class RequestHandler(
    private val responseHandlerMapping: ResponseHandlerMapping,
    private val webAppConfig: WebAppConfig,
) {

    fun getHandler(): (Context) -> Unit {
        return { ctx: Context ->
            val returnType = responseHandlerMapping.returnType
            // Create log
            val logEntry = LogEntry(
                localTimeZone = webAppConfig.localTimezone,
                routeType = responseHandlerMapping.type,
                httpMethod = ctx.method().toString()
            )
            // Set the start time
            val startTime = System.currentTimeMillis()
            try {
                // Log the request path
                logEntry.requestLog.path = responseHandlerMapping.path
                logEntry.requestLog.requestBody = ctx.body()
                // Check if route has arguments
                val hasNoArguments = responseHandlerMapping.parameters.isEmpty()
                // Create instance of route class
                val responseHandler: IResponseHandler = when {
                    hasNoArguments -> responseHandlerMapping.responseHandlerClass.createInstance()
                    else -> createResponseHandler(responseHandlerMapping, ctx, logEntry)
                }
                // Send response
                sendResponse(ctx, responseHandler, logEntry, returnType, webAppConfig.prettyFormatHtml)
            } catch (e: Exception) {
                ctx.status(500)
                val errorLog = ErrorLog(e)
                logEntry.errorLog = errorLog
                ApiResponse.Error(
                    message = e.message ?: "An error occurred",
                    errorId = errorLog.errorId
                )
            } finally {
                // Log the response status code
                logEntry.responseLog.statusCode = ctx.statusCode()
                // Log the execution time
                logEntry.executionTimeInMs = System.currentTimeMillis() - startTime
                // Log the request and response
                webAppConfig.logger.log(logEntry)
            }
        }
    }
}
