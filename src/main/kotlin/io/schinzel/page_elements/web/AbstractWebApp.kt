package io.schinzel.page_elements.web

import io.schinzel.page_elements.web.request_handler.log.ConsoleLogger
import io.schinzel.page_elements.web.request_handler.log.ILogger

abstract class AbstractWebApp {
    // Optional configuration with defaults
    open val port: Int = 5555
    open val logger: ILogger = ConsoleLogger(prettyPrint = true)
    open val localTimezone: String = "Europe/Stockholm"
    open val prettyFormatHtml: Boolean = true
    open val printStartupMessages: Boolean = true
    open val environment: Environment = Environment.DEVELOPMENT



    fun start() {
        val webAppConfig = WebAppConfig(
            webRootClass = this,
            port = port,
            logger = logger,
            localTimezone = localTimezone,
            prettyFormatHtml = prettyFormatHtml,
            printStartupMessages = printStartupMessages,
            environment = environment,
        )
        InitWebApp(webAppConfig)
    }
}
