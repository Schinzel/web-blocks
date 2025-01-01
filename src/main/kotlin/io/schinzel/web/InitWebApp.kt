package io.schinzel.web

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web.request_handler.log.ILogger
import io.schinzel.web.request_handler.log.ConsoleLogger
import io.schinzel.web.response_handlers.initializeResponseHandlerDescriptorRegistry
import io.schinzel.web.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket

data class WebAppConfig(
    val endpointPackage: String,
    val port: Int = 5555,
    val logger: ILogger = ConsoleLogger(true),
    val localTimezone: String = "Europe/Stockholm",
    val prettyFormatHtml: Boolean = true,
)

class InitWebApp(
    webAppConfig: WebAppConfig
) {

    init {
        Thrower.throwIfFalse(isPortAvailable(webAppConfig.port))
            .message("Port ${webAppConfig.port} is not available")
        initializeResponseHandlerDescriptorRegistry(webAppConfig.endpointPackage)
        setUpRoutes(webAppConfig)
        "*".repeat(30).println()
        "Project started on port ${webAppConfig.port}".println()
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