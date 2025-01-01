package io.schinzel.sample.component.api.v1

import io.schinzel.web_app_engine.response_handlers.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class UserPets : IApiEndpointResponseHandler {
    override fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}