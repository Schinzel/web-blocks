package io.schinzel.web_app_engine

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_app_engine.route_handler.log.ILogger
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_registry.initializeResponseHandlerDescriptorRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes

class InitWebApp(
    endpointPackage: String,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
) {

    init {
        initializeResponseHandlerDescriptorRegistry()
        setUpRoutes(endpointPackage, localTimezone, logger)

        "*".repeat(30).println()
        "Project started".println()
        "*".repeat(30).println()
    }
}