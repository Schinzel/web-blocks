package io.schinzel.sample.apis.v1

import io.schinzel.page_elements.IApi

@Suppress("unused")
class UserPets : IApi {
    override fun getData(): Any {
        return listOf(
            Pet("Fluffy", "Cat"),
            Pet("Rex", "Dog")
        )
    }

    data class Pet(val name: String, val type: String)
}