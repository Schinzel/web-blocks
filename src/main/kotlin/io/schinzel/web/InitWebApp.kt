package io.schinzel.web

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web.response_handlers.*
import io.schinzel.web.set_up_routes.setUpRoutes

/**
 * The purpose of this class is to initialize the web app.
 */
class InitWebApp(
    webAppConfig: WebAppConfig
) {

    init {
        initializeResponseHandlerDescriptorRegistry(webAppConfig.endpointPackage)
        setUpRoutes(webAppConfig)
        "*".repeat(30).println()
        "Project started on port ${webAppConfig.port}".println()
        "*".repeat(30).println()
    }


    companion object {

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