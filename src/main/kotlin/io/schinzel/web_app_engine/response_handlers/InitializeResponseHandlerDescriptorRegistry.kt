package io.schinzel.web_app_engine.response_handlers

import io.schinzel.web_app_engine.response_handlers.response_handlers.*

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