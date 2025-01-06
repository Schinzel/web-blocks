package io.schinzel.page_elements.web

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.page_elements.web.response_handlers.*
import io.schinzel.page_elements.web.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket

/**
 * The purpose of this class is to initialize the web app.
 */
class InitWebApp(
    webAppConfig: WebAppConfig
) {

    init {
        val port = webAppConfig.port
        Thrower.throwIfFalse(isPortAvailable(port))
            .message("Port $port is not available")

        initializeResponseHandlerDescriptorRegistry(webAppConfig.webRootPackage)
        setUpRoutes(webAppConfig)
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
        fun initializeResponseHandlerDescriptorRegistry(endpointPackage: String) {
            ResponseHandlerDescriptorRegistry
                .register(IPageResponseHandler::class, PageResponseHandlerDescriptor(endpointPackage))
            ResponseHandlerDescriptorRegistry
                .register(IPageEndpointResponseHandler::class, PageEndpointResponseHandlerDescriptor(endpointPackage))
            ResponseHandlerDescriptorRegistry
                .register(IApiEndpointResponseHandler::class, ApiEndpointResponseHandlerDescriptor(endpointPackage))
        }
    }
}