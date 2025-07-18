package io.schinzel.web_blocks.web.test_routes.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class GetPetsEndpoint : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse =
        json(
            listOf(
                Pet("Fluffy", "Cat"),
                Pet("Rex", "Dog"),
            ),
        )

    data class Pet(
        val name: String,
        val type: String,
    )
}
