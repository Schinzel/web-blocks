package io.schinzel.samples.component.api.v1

import io.schinzel.web.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class ErrorEndpoint: IApiEndpointResponseHandler {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}