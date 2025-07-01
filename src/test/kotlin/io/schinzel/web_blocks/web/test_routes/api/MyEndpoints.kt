package io.schinzel.web_blocks.web.test_routes.api

import io.schinzel.web_blocks.web.routes.IApiRoute
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json

@Suppress("unused")
class GetPetsEndpoint : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return json(listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        ))
    }

    data class Pet(val name: String, val type: String)
}