package io.schinzel._old_samples.component.api.v1

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class ApiRouteThatThrowsError: IApiRoute {
    override fun getResponse(): String {
        throw RuntimeException("Something went wrong!!")
    }
}