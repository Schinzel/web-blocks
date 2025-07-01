package io.schinzel.web_blocks.web.request_handler

import io.javalin.http.Context
import io.schinzel.web_blocks.web.request_handler.log.LogEntry
import io.schinzel.web_blocks.web.response.HtmlResponse
import io.schinzel.web_blocks.web.response.JsonResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import org.jsoup.Jsoup

/**
 * The purpose of this class is to send a response to a client
 */
suspend fun sendResponse(
    ctx: Context,
    route: IRoute,
    logEntry: LogEntry,
    returnType: ReturnTypeEnum,
    prettyFormatHtml: Boolean
) {
    // Get the WebBlockResponse from the route
    val response: WebBlockResponse = route.getResponse()
    
    // Set custom headers if provided
    response.headers.forEach { (key, value) ->
        ctx.header(key, value)
    }
    
    // Set status code
    ctx.status(response.status)
    
    // Send response based on type
    when (response) {
        is HtmlResponse -> {
            val formattedHtml = if (prettyFormatHtml) {
                prettyFormatHtml(response.content)
            } else {
                response.content
            }
            ctx.html(formattedHtml)
        }

        is JsonResponse -> {
            val responseObject = ApiResponse.Success(message = response.data)
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
