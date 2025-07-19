package io.schinzel.web_blocks.web.test_routes.api

import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Suppress("unused")
@Api
class GetPetsEndpoint : IWebBlockRoute {
    override suspend fun getResponse(): IWebBlockResponse =
        jsonSuccess(
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
