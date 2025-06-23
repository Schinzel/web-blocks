package io.schinzel.samples.component.api.v1

import io.schinzel.page_elements.web.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class ErrorEndpoint: IApiEndpointResponseHandler {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}