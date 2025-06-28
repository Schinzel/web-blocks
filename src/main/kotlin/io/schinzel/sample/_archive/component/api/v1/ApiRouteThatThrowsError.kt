package io.schinzel.sample._archive.component.api.v1

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class ApiRouteThatThrowsError: IApiRoute {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}