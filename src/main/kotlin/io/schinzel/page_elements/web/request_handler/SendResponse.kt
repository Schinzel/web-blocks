package io.schinzel.page_elements.web.request_handler

import io.javalin.http.Context
import io.schinzel.page_elements.web.request_handler.log.LogEntry
import io.schinzel.page_elements.web.response_handlers.IResponseHandler
import io.schinzel.page_elements.web.response_handlers.ReturnTypeEnum
import org.jsoup.Jsoup

/**
 * The purpose of this class is to send a response to a client
 */
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

private fun prettyFormatHtml(htmlString: String): String {
    val document = Jsoup.parse(htmlString)
    document.outputSettings()
        .prettyPrint(true)
        .indentAmount(2)
    return document.outerHtml()
}
