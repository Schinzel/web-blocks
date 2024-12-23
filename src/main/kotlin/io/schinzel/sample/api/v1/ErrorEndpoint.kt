package io.schinzel.sample.api.v1

import io.schinzel.page_elements.web_response.IEndpoint

@Suppress("unused")
class ErrorEndpoint: IEndpoint {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}