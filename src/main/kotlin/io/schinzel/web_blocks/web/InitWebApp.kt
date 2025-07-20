package io.schinzel.web_blocks.web

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web_blocks.web.request_handler.setUpErrorHandling
import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorApi
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorPage
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorWebBlockApi
import io.schinzel.web_blocks.web.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket

/**
 * The purpose of this class is to initialize the web app.
 */
class InitWebApp(
    webAppConfig: WebAppConfig,
) {
    init {
        val port = webAppConfig.port
        Thrower
            .throwIfFalse(isPortAvailable(port))
            .message("Port $port is not available")

        initializeRouteDescriptorRegistry(webAppConfig.webRootPackage)
        val javalin = setUpRoutes(webAppConfig)
        javalin.setUpErrorHandling(webAppConfig)
        if (webAppConfig.printStartupMessages) {
            "*".repeat(30).println()
            "Project started on port $port".println()
            "*".repeat(30).println()
        }
    }

    companion object {
        private fun isPortAvailable(port: Int): Boolean =
            try {
                ServerSocket(port).use { true }
            } catch (_: IOException) {
                false
            }

        /**
         * Register the default descriptors
         */
        fun initializeRouteDescriptorRegistry(endpointPackage: String) {
            // Register annotation-based route descriptors
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.PAGE,
                RouteDescriptorPage(endpointPackage),
            )
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.API,
                RouteDescriptorWebBlockApi(endpointPackage),
            )
            RouteDescriptorRegistry.registerAnnotation(
                RouteTypeEnum.PAGE_API,
                RouteDescriptorApi(endpointPackage),
            )
        }
    }
}
