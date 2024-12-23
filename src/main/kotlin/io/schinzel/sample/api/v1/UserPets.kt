package io.schinzel.sample.api.v1

import io.schinzel.page_elements.web_response.IEndpoint

@Suppress("unused")
class UserPets : IEndpoint {
    override fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}