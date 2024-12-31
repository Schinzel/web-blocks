package io.schinzel.sample

import io.schinzel.web_app_engine.InitWebApp
import io.schinzel.web_app_engine.WebAppConfig
import io.schinzel.web_app_engine.request_handler.log.ConsoleLogger

fun main() {
    InitWebApp(
        WebAppConfig(
            endpointPackage = "io.schinzel.sample",
            port = 5555,
            logger = ConsoleLogger(prettyPrint = true),
            localTimezone = "Europe/Stockholm",
            prettyFormatHtml = true,
        )
    )
}
