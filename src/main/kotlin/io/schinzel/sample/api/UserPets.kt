package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.IApiRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Suppress("unused")
@Api
class UserPets : IApiRoute {
    override suspend fun getResponse(): IJsonResponse =
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
