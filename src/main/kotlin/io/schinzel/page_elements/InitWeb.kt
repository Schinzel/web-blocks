package io.schinzel.page_elements

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.route_handler.log.ILogger
import io.schinzel.page_elements.route_handler.log.PrettyConsoleLogger
import io.schinzel.page_elements.set_up_routes.setUpRoutes

class InitWeb(
    pagePackage: String,
    apiPackage: String,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
) {

    init {
        setUpRoutes(pagePackage, apiPackage, localTimezone, logger)

        "*".repeat(30).println()
        "Project started".println()
        "*".repeat(30).println()
    }
}