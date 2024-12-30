package io.schinzel.web_app_engine.request_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.WebAppConfig
import io.schinzel.web_app_engine.request_handler.log.ErrorLog
import io.schinzel.web_app_engine.request_handler.log.LogEntry
import io.schinzel.web_app_engine.response_handler_mapping.ResponseHandlerMapping
import io.schinzel.web_app_engine.response_handlers.response_handlers.IResponseHandler
import io.schinzel.web_app_engine.response_handlers.response_handlers.ReturnTypeEnum
import org.jsoup.Jsoup
import kotlin.reflect.full.createInstance

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
                ApiError(e.message ?: "An error occurred", errorLog.errorId)
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

fun sendResponse(
    ctx: Context,
    responseHandler: IResponseHandler,
    logEntry: LogEntry,
    returnType: ReturnTypeEnum,
    prettyFormatHtml: Boolean
) {
    // Get the response
    val response: Any = responseHandler.getResponse()
    // Send response
    when (returnType) {
        ReturnTypeEnum.HTML -> {
            val formattedHtml = if (prettyFormatHtml) {
                prettyFormatHtml(response as String)
            } else {
                response as String
            }
            ctx.html(formattedHtml)
        }

        ReturnTypeEnum.JSON -> {
            val responseObject = ApiResponse.Success(message = response)
            ctx.json(responseObject)
            logEntry.responseLog.response = responseObject
        }
    }
}

sealed class ApiResponse {
    data class Success(
        val success: Boolean = true,
        val message: Any? = null
    ) : ApiResponse()

    data class Error(
        val success: Boolean = false,
        val message: String,
        val code: String? = null
    ) : ApiResponse()
}

fun prettyFormatHtml(htmlString: String): String {
    val document = Jsoup.parse(htmlString)
    document.outputSettings()
        .prettyPrint(true)
        .indentAmount(2)
    return document.outerHtml()
}

data class ApiError(
    val message: String,
    val errorId: String
)