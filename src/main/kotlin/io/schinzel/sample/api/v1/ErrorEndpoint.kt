package io.schinzel.sample.api.v1

import io.schinzel.web_app_engine.route_registry.processors.IEndpointResponseHandler

@Suppress("unused")
class ErrorEndpoint: IEndpointResponseHandler {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}