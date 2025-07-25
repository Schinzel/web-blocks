package io.schinzel.web_blocks.web.request_handler

import io.javalin.http.Context
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry
import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.HtmlErrorResponse
import io.schinzel.web_blocks.web.response.HtmlRedirectResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.JsonErrorResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
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
    prettyFormatHtml: Boolean,
) {
    // Cast route to IWebBlockRoute to get response
    val webBlockRoute =
        route as? IWebBlockRoute<*>
            ?: throw IllegalArgumentException("Route must implement IWebBlockRoute")

    // Get the WebBlockResponse from the route
    val response: IWebBlockResponse = webBlockRoute.getResponse()

    // Set custom headers if provided
    response.headers.forEach { (key, value) ->
        ctx.header(key, value)
    }

    // Set status code
    ctx.status(response.status)

    // Send response based on type
    when (response) {
        is HtmlContentResponse -> {
            val formattedHtml =
                if (prettyFormatHtml) {
                    prettyFormatHtml(response.content)
                } else {
                    response.content
                }
            ctx.html(formattedHtml)
        }

        is HtmlRedirectResponse -> {
            // Set status first, then redirect
            ctx.status(response.status)
            ctx.header("Location", response.location)
        }

        is HtmlErrorResponse -> {
            val formattedHtml =
                if (prettyFormatHtml) {
                    prettyFormatHtml(response.content)
                } else {
                    response.content
                }
            ctx.html(formattedHtml)
        }

        is JsonSuccessResponse -> {
            val responseObject = ApiResponse.Success(message = response.data)
            ctx.json(responseObject)
            logEntry.responseLog.response = responseObject
        }

        is JsonErrorResponse -> {
            val responseObject =
                ApiResponse.Error(
                    message = response.error.message,
                    errorId = response.error.code,
                )
            ctx.json(responseObject)
            logEntry.responseLog.response = responseObject
        }
    }
}

private fun prettyFormatHtml(htmlString: String): String {
    val document = Jsoup.parse(htmlString)
    document
        .outputSettings()
        .prettyPrint(true)
        .indentAmount(2)
    return document.outerHtml()
}
