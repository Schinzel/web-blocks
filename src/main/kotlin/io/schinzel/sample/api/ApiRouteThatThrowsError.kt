package io.schinzel.sample.api

import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class ApiRouteThatThrowsError: IApiRoute {
    override suspend fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}