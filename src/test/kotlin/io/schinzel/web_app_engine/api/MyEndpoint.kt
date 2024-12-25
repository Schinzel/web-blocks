package io.schinzel.web_app_engine.api

import io.schinzel.web_app_engine.route_registry.processors.IEndpoint

@Suppress("unused")
class GetPetsEndpoint : IEndpoint {
    override fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}