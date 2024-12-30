package io.schinzel.web_app_engine

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web_app_engine.request_handler.log.ILogger
import io.schinzel.web_app_engine.request_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.response_handlers.initializeResponseHandlerDescriptorRegistry
import io.schinzel.web_app_engine.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket

class InitWebApp(
    endpointPackage: String,
    port: Int = 5555,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
) {

    init {
        Thrower.throwIfFalse(isPortAvailable(port))
            .message("Port $port is not available")
        initializeResponseHandlerDescriptorRegistry(endpointPackage)
        setUpRoutes(endpointPackage, localTimezone, logger, port)

        "*".repeat(30).println()
        "Project started on port $port".println()
        "*".repeat(30).println()
    }


    companion object {
        fun isPortAvailable(port: Int): Boolean {
            return try {
                ServerSocket(port).use { true }
            } catch (e: IOException) {
                false
            }
        }
    }
}