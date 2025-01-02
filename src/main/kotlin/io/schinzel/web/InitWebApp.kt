package io.schinzel.web

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web.response_handlers.*
import io.schinzel.web.set_up_routes.setUpRoutes
import java.io.IOException
import java.net.ServerSocket
import java.time.ZoneId

/**
 * The purpose of this class is to initialize the web app.
 */
class InitWebApp(
    webAppConfig: WebAppConfig
) {

    init {
        Thrower.throwIfFalse(webAppConfig.port in 1..65535)
            .message("Incorrect port '${webAppConfig.port}'. Port must be between 1 and 65535.")
        Thrower.throwIfFalse(isPortAvailable(webAppConfig.port))
            .message("Port ${webAppConfig.port} is not available")
        Thrower.throwIfFalse(isValidTimezone(webAppConfig.localTimezone))
            .message("'${webAppConfig.localTimezone}' is not a valid timezone")
        Thrower.throwIfFalse(isValidPackage(webAppConfig.endpointPackage))
            .message("'${webAppConfig.endpointPackage}' is not a valid package")

        initializeResponseHandlerDescriptorRegistry(webAppConfig.endpointPackage)
        setUpRoutes(webAppConfig)
        "*".repeat(30).println()
        "Project started on port ${webAppConfig.port}".println()
        "*".repeat(30).println()
    }


    companion object {
        fun isValidPackage(packageName: String): Boolean {
            return ClassLoader.getSystemClassLoader()
                .definedPackages
                .any { it.name == packageName }
        }

        fun isPortAvailable(port: Int): Boolean {
            return try {
                ServerSocket(port).use { true }
            } catch (e: IOException) {
                false
            }
        }

        private fun isValidTimezone(timezone: String): Boolean =
            try {
                ZoneId.of(timezone)
                true
            } catch (e: Exception) {
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