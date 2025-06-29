package io.schinzel.sample.api

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class ApiRouteThatThrowsError: IApiRoute {
    override suspend fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}