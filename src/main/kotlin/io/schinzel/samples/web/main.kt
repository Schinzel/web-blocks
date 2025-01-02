package io.schinzel.samples.web

import io.schinzel.web.InitWebApp
import io.schinzel.web.WebAppConfig
import io.schinzel.web.request_handler.log.ConsoleLogger

/**
 * The purpose of this class is to start a demo of the web framework.
 * See README.md for links to use with this demo.
 */
fun main() {
    InitWebApp(
        WebAppConfig(
            routesPackage = "io.schinzel.samples.web",
            port = 5555,
            logger = ConsoleLogger(prettyPrint = true),
            localTimezone = "Europe/Stockholm",
            prettyFormatHtml = true,
        )
    )
}
