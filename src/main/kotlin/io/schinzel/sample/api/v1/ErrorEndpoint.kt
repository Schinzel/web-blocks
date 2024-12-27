package io.schinzel.sample.api.v1

import io.schinzel.web_app_engine.route_registry.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class ErrorEndpoint: IApiEndpointResponseHandler {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}