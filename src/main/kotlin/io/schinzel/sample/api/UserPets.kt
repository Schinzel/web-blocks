package io.schinzel.sample.api

import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class UserPets : IApiRoute {
    override suspend fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}