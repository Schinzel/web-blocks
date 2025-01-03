package io.schinzel.page_elements.samples.component.api.v1

import io.schinzel.page_elements.web.response_handlers.IApiEndpointResponseHandler

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