package io.schinzel.page_elements.samples.web

import io.schinzel.page_elements.web.InitWebApp
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.request_handler.log.ConsoleLogger

/**
 * The purpose of this class is to start a demo of the web framework.
 * See README.md for links to use with this demo.
 */
fun main() {
    InitWebApp(
        WebAppConfig(
            webRootPackage = "io.schinzel.page_elements.samples.web",
            port = 5555,
            logger = ConsoleLogger(prettyPrint = true),
            localTimezone = "Europe/Stockholm",
            prettyFormatHtml = true,
            printStartupMessages = true,
        )
    )
}
