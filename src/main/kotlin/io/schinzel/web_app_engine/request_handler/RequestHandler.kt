package io.schinzel.web_app_engine.request_handler

import io.javalin.http.Context
import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.web_app_engine.request_handler.log.ErrorLog
import io.schinzel.web_app_engine.request_handler.log.ILogger
import io.schinzel.web_app_engine.request_handler.log.Log
import io.schinzel.web_app_engine.request_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.response_handler_mapping.ResponseHandlerMapping
import io.schinzel.web_app_engine.response_handlers.response_handlers.IResponseHandler
import io.schinzel.web_app_engine.response_handlers.response_handlers.ReturnTypeEnum
import org.jsoup.Jsoup
import kotlin.reflect.full.createInstance

class RequestHandler(
    private val responseHandlerMapping: ResponseHandlerMapping,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
    private val prettyFormatHtml: Boolean = true,
) {

    fun getHandler(): (Context) -> Unit {
        return { ctx: Context ->
            ctx.body().printlnWithPrefix("Request body")

            val returnType = responseHandlerMapping.returnType
            // Create log
            val log = Log(
                localTimeZone = localTimezone,
                routeType = responseHandlerMapping.type,
                httpMethod = ctx.method().toString()
            )
            // Set the start time
            val startTime = System.currentTimeMillis()
            try {
                // Log the request path
                log.requestLog.path = responseHandlerMapping.path
                log.requestLog.requestBody = ctx.body()
                // Check if route has arguments
                val hasNoArguments = responseHandlerMapping.parameters.isEmpty()
                // Create instance of route class
                val responseHandler: IResponseHandler = when {
                    hasNoArguments -> responseHandlerMapping.responseHandlerClass.createInstance()
                    else -> createResponseHandler(responseHandlerMapping, ctx, log)
                }
                // Send response
                sendResponse(ctx, responseHandler, log, returnType, prettyFormatHtml)
            } catch (e: Exception) {
                ctx.status(500)
                val errorLog = ErrorLog(e)
                log.errorLog = errorLog
                ApiError(e.message ?: "An error occurred", errorLog.errorId)
            } finally {
                // Log the response status code
                log.responseLog.statusCode = ctx.statusCode()
                // Log the execution time
                log.executionTimeInMs = System.currentTimeMillis() - startTime
                // Write log
                logger.log(log)
            }
        }
    }
}

fun sendResponse(
    ctx: Context,
    responseHandler: IResponseHandler,
    log: Log,
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
            val responseObject = ApiResponse.Success(message = response as String)
            ctx.json(responseObject)
            log.responseLog.response = responseObject
        }
    }
}

sealed class ApiResponse {
    data class Success(
        val success: Boolean = true,
        val message: String? = null
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