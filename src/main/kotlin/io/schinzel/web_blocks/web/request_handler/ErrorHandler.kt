package io.schinzel.web_blocks.web.request_handler

import io.javalin.http.Context
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.errors.ErrorPage
import io.schinzel.web_blocks.web.request_handler.log_entry.ErrorLog
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum

/**
 * The purpose of this class is to provide pure error response functions without logging.
 * Logging is handled by RequestHandler to avoid code duplication.
 *
 * Written by Claude Sonnet 4
 */
fun handleExceptionResponse(
    e: Exception,
    ctx: Context,
    webAppConfig: WebAppConfig,
    logEntry: LogEntry,
    statusCode: Int = 500,
) {
    ctx.status(statusCode)

    // Ensure errorLog exists and get consistent errorId
    if (logEntry.errorLog == null) {
        logEntry.errorLog = ErrorLog(e)
    }
    val errorId = logEntry.errorLog!!.errorId

    when (getReturnType(ctx.path())) {
        ReturnTypeEnum.JSON -> {
            val response =
                ApiResponse.Error(
                    message = e.message ?: getDefaultErrorMessage(statusCode),
                    errorId = errorId,
                )
            ctx.json(response)
        }

        ReturnTypeEnum.HTML -> {
            val errorPageHtml =
                ErrorPage(webAppConfig.webRootClass, webAppConfig.environment)
                    .addData("errorMessage", e.message ?: getDefaultErrorMessage(statusCode))
                    .addData("errorId", errorId)
                    .getErrorPage(statusCode)
            ctx.html(errorPageHtml)
        }
    }
}

private fun getDefaultErrorMessage(statusCode: Int): String =
    when (statusCode) {
        404 -> "Page not found"
        500 -> "Internal server error"
        else -> "Error occurred"
    }

/**
 * Generate error message for HTTP status codes
 */
fun getErrorMessageForStatus(
    statusCode: Int,
    path: String,
): String =
    when (statusCode) {
        400 -> "Bad Request: $path"
        401 -> "Unauthorized: $path"
        403 -> "Forbidden: $path"
        404 -> "Not Found: $path"
        405 -> "Method Not Allowed: $path"
        413 -> "Payload Too Large: $path"
        415 -> "Unsupported Media Type: $path"
        429 -> "Too Many Requests: $path"
        500 -> "Internal Server Error: $path"
        502 -> "Bad Gateway: $path"
        503 -> "Service Unavailable: $path"
        else -> "HTTP $statusCode Error: $path"
    }

private fun getReturnType(path: String): ReturnTypeEnum {
    // If the path starts with /api or /page-block-api, return JSON
    if (path.startsWith("/api") || path.startsWith("/page-block-api")) {
        return ReturnTypeEnum.JSON
    }
    return ReturnTypeEnum.HTML
}
