package io.schinzel.web

import io.schinzel.web.request_handler.log.ConsoleLogger
import io.schinzel.web.request_handler.log.ILogger

/**
 * The purpose of this class is to hold configuration settings for the web app.
 * @param endpointPackage The package where the endpoints are located.
 * @param port The port the web app will listen to. Default value is 5555.
 * @param logger The logger to use. Default value is a console logger.
 * @param localTimezone The timezone to use. Default value is "Europe/Stockholm".
 * @param prettyFormatHtml If the html should be pretty formatted. Default value is true.
 * For production environment set this should be false.
 */
data class WebAppConfig(
    val endpointPackage: String,
    val port: Int = 5555,
    val logger: ILogger = ConsoleLogger(prettyPrint = true),
    val localTimezone: String = "Europe/Stockholm",
    val prettyFormatHtml: Boolean = true,
)