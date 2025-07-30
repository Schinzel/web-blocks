package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.request_handler.getErrorMessageForStatus
import io.schinzel.web_blocks.web.request_handler.handleExceptionResponse
import io.schinzel.web_blocks.web.request_handler.log_entry.ErrorLog
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry

/**
 * The purpose of this class is to provide the after handler extension for Javalin setup.
 * Completes logging for ALL requests and handles unlogged errors (404s, etc.).
 *
 * Written by Claude Sonnet 4
 */
fun Javalin.setupAfterHandler(webAppConfig: WebAppConfig): Javalin {
    this.after { ctx ->
        // Get the log entry that was set in javalin.before and modified in the RequestHandler
        val logEntry =
            ctx.attribute<LogEntry>("logEntry")
                ?: throw IllegalStateException("LogEntry not found in Javalin context")
        val startTime =
            ctx.attribute<Long>("startTime")
                ?: throw IllegalStateException("startTime not found in Javalin context")

        // If no errorLog was set but status indicates error (404, 405, etc.)
        if (logEntry.errorLog == null && ctx.statusCode() >= 400) {
            val statusCode = ctx.statusCode()
            val errorMessage = getErrorMessageForStatus(statusCode, ctx.path())
            logEntry.errorLog = ErrorLog(RuntimeException(errorMessage))

            // Send appropriate error response for unhandled errors
            handleExceptionResponse(
                RuntimeException(errorMessage),
                ctx,
                webAppConfig,
                logEntry,
                statusCode,
            )
        }

        // Always complete logging
        logEntry.responseLog.statusCode = ctx.statusCode()
        logEntry.executionTimeInMs = System.currentTimeMillis() - startTime
        webAppConfig.logger.log(logEntry)
    }
    return this
}
