package io.schinzel.samples.web.api.my_dir

import io.schinzel.page_elements.web.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class MyPersonEndpoint: IApiEndpointResponseHandler {
    override fun getResponse(): Any {
        return Person("John", "Doe", 42)
    }

    data class Person(
        val firstName: String,
        val lastName: String,
        val age: Int)
}