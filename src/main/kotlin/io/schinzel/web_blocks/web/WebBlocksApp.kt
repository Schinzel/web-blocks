package io.schinzel.web_blocks.web

import io.javalin.Javalin
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web_blocks.web.request_handler.log.ConsoleLogger
import io.schinzel.web_blocks.web.request_handler.log.ILogger
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistryInit
import io.schinzel.web_blocks.web.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

abstract class WebBlocksApp {
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

        // Create a future to wait for server startup completion
        val serverReadyFuture = CompletableFuture<Unit>()

        // Add event listeners for server lifecycle events
        javalin?.events { eventConfig ->
            // Signal successful server startup when Javalin fires SERVER_STARTED event
            eventConfig.serverStarted {
                // Complete the future to unblock the waiting thread
                serverReadyFuture.complete(Unit)
            }
            // Signal startup failure when Javalin fires SERVER_START_FAILED event
            eventConfig.serverStartFailed {
                // Complete the future with exception to unblock and signal failure
                serverReadyFuture.completeExceptionally(
                    RuntimeException("Server failed to start"),
                )
            }
        }

        // Start the server and wait for it to be ready
        try {
            // Actually start the Javalin server
            javalin?.start(port)
            // Wait for server to be ready with 5-second timeout
            serverReadyFuture.get(5, TimeUnit.SECONDS)

            // If is to print a start up message
            if (printStartupMessages) {
                // Print start up message
                "*".repeat(30).println()
                "Project started on port $port".println()
                "*".repeat(30).println()
            }
        } catch (e: Exception) {
            javalin?.stop()
            javalin = null
            throw RuntimeException("Server failed to start within 5 seconds", e)
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
