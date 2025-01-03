package io.schinzel.page_elements.web.test_routes.api

import io.schinzel.page_elements.web.response_handlers.IApiEndpointResponseHandler

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