package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry

/**
 * The purpose of this class is to provide the before handler extension for Javalin setup.
 * Creates LogEntry for ALL requests and stores in context for use by RequestHandler and after handler.
 *
 * Written by Claude Sonnet 4
 */

fun Javalin.setupBeforeHandler(webAppConfig: WebAppConfig): Javalin {
    this.before { ctx ->
        val logEntry =
            LogEntry(
                localTimeZone = webAppConfig.localTimezone,
                routeType = "Unknown", // Will be updated by RequestHandler if route exists
                httpMethod = ctx.method().toString(),
            )
        logEntry.requestLog.path = ctx.path()
        logEntry.httpMethod = ctx.method().toString()

        // Set log entry and start time as javalin attributes so they can be used
        // in the RequestHandler and in the after handler
        ctx.attribute("logEntry", logEntry)
        ctx.attribute("startTime", System.currentTimeMillis())
    }
    return this
}
