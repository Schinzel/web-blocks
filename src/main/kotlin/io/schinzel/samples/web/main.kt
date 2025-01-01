package io.schinzel.samples.web

import io.schinzel.web.InitWebApp
import io.schinzel.web.WebAppConfig
import io.schinzel.web.request_handler.log.ConsoleLogger

fun main() {
    InitWebApp(
        WebAppConfig(
            endpointPackage = "io.schinzel.samples.web",
            port = 5555,
            logger = ConsoleLogger(prettyPrint = true),
            localTimezone = "Europe/Stockholm",
            prettyFormatHtml = true,
        )
    )
}
