package io.schinzel.sample.api.v1

import io.schinzel.web_app_engine.IEndpoint

@Suppress("unused")
class ErrorEndpoint: IEndpoint {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}