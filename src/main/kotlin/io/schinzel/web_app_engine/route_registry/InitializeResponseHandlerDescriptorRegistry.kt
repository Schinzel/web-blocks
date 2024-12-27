package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.*

/**
 * Register the default descriptors
 */
fun initializeResponseHandlerDescriptorRegistry() {
    ResponseHandlerDescriptorRegistry
        .register(IPageResponseHandler::class, PageResponseHandlerDescriptor())
    ResponseHandlerDescriptorRegistry
        .register(IPageEndpointResponseHandler::class, PageEndpointResponseHandlerDescriptor())
    ResponseHandlerDescriptorRegistry
        .register(IEndpointResponseHandler::class, EndpointResponseHandlerDescriptor())
}