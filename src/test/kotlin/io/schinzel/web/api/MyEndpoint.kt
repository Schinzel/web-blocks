package io.schinzel.web.api

import io.schinzel.web.response_handlers.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class GetPetsEndpoint : IApiEndpointResponseHandler {
    override fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}