package io.schinzel.samples.web

import io.schinzel.web_app_engine.InitWebApp
import io.schinzel.web_app_engine.WebAppConfig
import io.schinzel.web_app_engine.request_handler.log.ConsoleLogger

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
