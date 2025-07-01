package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class UserPets : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return json(listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        ))
    }

    data class Pet(val name: String, val type: String)
}