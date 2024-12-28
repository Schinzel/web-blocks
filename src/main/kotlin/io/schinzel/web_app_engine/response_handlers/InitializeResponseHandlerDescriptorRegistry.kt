package io.schinzel.web_app_engine.response_handlers

import io.schinzel.web_app_engine.response_handlers.response_handlers.*

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