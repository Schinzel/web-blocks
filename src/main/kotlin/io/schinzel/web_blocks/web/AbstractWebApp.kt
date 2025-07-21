package io.schinzel.web_blocks.web

import io.javalin.Javalin
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web_blocks.web.request_handler.log.ConsoleLogger
import io.schinzel.web_blocks.web.request_handler.log.ILogger
import io.schinzel.web_blocks.web.request_handler.setUpErrorHandling
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistryInit
import io.schinzel.web_blocks.web.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket

abstract class AbstractWebApp {
    // Optional configuration with defaults
    open val port: Int = 5555
    open val logger: ILogger = ConsoleLogger(prettyPrint = true)
    open val localTimezone: String = "Europe/Stockholm"
    open val prettyFormatHtml: Boolean = true
    open val printStartupMessages: Boolean = true
    open val environment: Environment = Environment.DEVELOPMENT

    private var javalin: Javalin? = null

    fun start() {
        val webAppConfig =
            WebAppConfig(
                webRootClass = this,
                port = port,
                logger = logger,
                localTimezone = localTimezone,
                prettyFormatHtml = prettyFormatHtml,
                printStartupMessages = printStartupMessages,
                environment = environment,
            )

        Thrower
            .throwIfFalse(isPortAvailable(port))
            .message("Port $port is not available")

        RouteDescriptorRegistry.clear()
        RouteDescriptorRegistryInit(webAppConfig.webRootPackage)
        javalin = setUpRoutes(webAppConfig)
        javalin?.setUpErrorHandling(webAppConfig)
        if (printStartupMessages) {
            "*".repeat(30).println()
            "Project started on port $port".println()
            "*".repeat(30).println()
        }
    }

    fun stop() {
        javalin?.stop()
        javalin = null
        RouteDescriptorRegistry.clear()
    }

    private fun isPortAvailable(port: Int): Boolean =
        try {
            ServerSocket(port).use { true }
        } catch (_: IOException) {
            false
        }
}
