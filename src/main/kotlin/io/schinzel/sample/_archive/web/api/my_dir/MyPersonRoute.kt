package io.schinzel.sample._archive.web.api.my_dir

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class MyPersonRoute: IApiRoute {
    override fun getResponse(): Any {
        return Person("John", "Doe", 42)
    }

    data class Person(
        val firstName: String,
        val lastName: String,
        val age: Int)
}