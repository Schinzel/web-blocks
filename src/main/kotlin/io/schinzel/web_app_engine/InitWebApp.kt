package io.schinzel.web_app_engine

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_app_engine.route_handler.log.ILogger
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_mapping.initializeRouteRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes2

class InitWebApp(
    webPackage: String,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
) {

    init {
        initializeRouteRegistry()
        setUpRoutes2(webPackage, localTimezone, logger)

        "*".repeat(30).println()
        "Project started".println()
        "*".repeat(30).println()
    }
}