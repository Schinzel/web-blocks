package io.schinzel.web

import io.schinzel.web.request_handler.log.ConsoleLogger
import io.schinzel.web.request_handler.log.ILogger

/**
 * The purpose of this class is to hold configuration settings for the web app.
 */
data class WebAppConfig(
    val endpointPackage: String,
    val port: Int = 5555,
    val logger: ILogger = ConsoleLogger(true),
    val localTimezone: String = "Europe/Stockholm",
    val prettyFormatHtml: Boolean = true,
)