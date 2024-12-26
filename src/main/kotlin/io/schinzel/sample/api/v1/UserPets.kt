package io.schinzel.sample.api.v1

import io.schinzel.web_app_engine.route_registry.processors.IApiEndpoint

@Suppress("unused")
class UserPets : IApiEndpoint {
    override fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}