package io.schinzel.web_app_engine.request_handler

import io.javalin.http.Context
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
                // Check if route has arguments
                val hasNoArguments = responseHandlerMapping.parameters.isEmpty()
                // Create instance of route class
                val responseHandler: IResponseHandler = when {
                    hasNoArguments -> responseHandlerMapping.responseHandlerClass.createInstance()
                    else -> createResponseHandler(responseHandlerMapping, ctx, log)
                }

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

                    ReturnTypeEnum.JSON -> ctx.json(response)
                }

                // Log the response if it is JSON
                if (returnType == ReturnTypeEnum.JSON) {
                    log.responseLog.response = response
                }

                // Log the response status code
                log.responseLog.statusCode = ctx.statusCode()
            } catch (e: Exception) {
                ctx.status(500)
                log.errorLog = ErrorLog(e)
                ApiError(e.message ?: "An error occurred", log.errorId)
            } finally {
                // Log the execution time
                log.executionTimeInMs = System.currentTimeMillis() - startTime
                // Write log
                logger.log(log)
            }
        }
    }
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