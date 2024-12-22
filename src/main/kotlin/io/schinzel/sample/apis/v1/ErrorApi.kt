package io.schinzel.sample.apis.v1

import io.schinzel.page_elements.IApi

@Suppress("unused")
class ErrorApi: IApi {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}