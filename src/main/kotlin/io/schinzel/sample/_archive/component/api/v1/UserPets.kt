package io.schinzel.sample._archive.component.api.v1

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class UserPets : IApiRoute {
    override fun getResponse(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}