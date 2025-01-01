package io.schinzel.samples.component

import io.schinzel.web_app_engine.InitWebApp
import io.schinzel.web_app_engine.WebAppConfig
import io.schinzel.web_app_engine.request_handler.log.ConsoleLogger

fun main() {
    InitWebApp(
        WebAppConfig(
            endpointPackage = "io.schinzel.samples.component",
            port = 5555,
            logger = ConsoleLogger(prettyPrint = true),
            localTimezone = "Europe/Stockholm",
            prettyFormatHtml = true,
        )
    )
}
