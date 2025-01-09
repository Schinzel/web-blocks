package io.schinzel.page_elements.web

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.web.errors.ErrorPages
import io.schinzel.page_elements.web.request_handler.log.ConsoleLogger
import io.schinzel.page_elements.web.request_handler.log.ILogger

abstract class WebApp {
    // Optional configuration with defaults
    open val port: Int = 5555
    open val logger: ILogger = ConsoleLogger(prettyPrint = true)
    open val localTimezone: String = "Europe/Stockholm"
    open val prettyFormatHtml: Boolean = true
    open val printStartupMessages: Boolean = true

    fun start() {


        val webAppConfig = WebAppConfig(
            webRootClass = this,
            port = port,
            logger = logger,
            localTimezone = localTimezone,
            prettyFormatHtml = prettyFormatHtml,
            printStartupMessages = false
        )
        //ErrorPages(this, Environment.PRODUCTION)
       // ErrorPages.getFileNameV2(this, Environment.STAGING, 501).printlnWithPrefix("Error page file name")

        ErrorPages(this, Environment.PRODUCTION)
            .getErrorPage(404)
            .printlnWithPrefix("Error page")
        //InitWebApp(webAppConfig)
    }
}
