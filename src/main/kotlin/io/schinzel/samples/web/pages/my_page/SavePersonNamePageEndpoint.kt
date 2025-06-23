package io.schinzel.samples.web.pages.my_page

import io.schinzel.page_elements.web.response_handlers.IPageEndpointResponseHandler

@Suppress("unused")
class SavePersonNamePageEndpoint(
    val userId: String,
    val firstName: String
): IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        return "Set the first name to be '$firstName' for user '$userId'"
    }
}