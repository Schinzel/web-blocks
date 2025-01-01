package io.schinzel.web.request_handler

import io.javalin.http.Context
import io.schinzel.web.request_handler.log.LogEntry
import io.schinzel.web.response_handlers.response_handlers.IResponseHandler
import io.schinzel.web.response_handlers.response_handlers.ReturnTypeEnum
import org.jsoup.Jsoup


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

private sealed class ApiResponse {
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

private fun prettyFormatHtml(htmlString: String): String {
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