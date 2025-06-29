package io.schinzel.page_elements.web.test_routes.api

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class GetPetsEndpoint : IApiRoute {
    override suspend fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}