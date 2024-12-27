package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.response_handlers.*

/**
 * Register the default descriptors
 */
fun initializeResponseHandlerDescriptorRegistry() {
    ResponseHandlerDescriptorRegistry
        .register(IPageResponseHandler::class, PageResponseHandlerDescriptor())
    ResponseHandlerDescriptorRegistry
        .register(IPageEndpointResponseHandler::class, PageEndpointResponseHandlerDescriptor())
    ResponseHandlerDescriptorRegistry
        .register(IApiEndpointResponseHandler::class, ApiEndpointResponseHandlerDescriptor())
}