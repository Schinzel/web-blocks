package io.schinzel.web_app_engine.api

import io.schinzel.web_app_engine.route_registry.response_handlers.IApiEndpointResponseHandler

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