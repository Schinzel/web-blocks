package io.schinzel.web_app_engine

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_registry.initializeRouteRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InitWebAppTest {

    @Test
    fun apa() {
        initializeRouteRegistry()
        val javalin = setUpRoutes("io.schinzel.web_app_engine", "Europe/Stockholm", PrettyConsoleLogger())
            ?: throw Exception("Javalin is null")

        javalin.port().println()
        assertTrue(true)
    }

}