package io.schinzel.samples.web.pages.my_page

import io.schinzel.web.response_handlers.IPageEndpointResponseHandler

@Suppress("unused")
class SavePersonNamePageEndpoint(
    val userId: String,
    val firstName: String
): IPageEndpointResponseHandler {
    override fun getResponse(): Any {
        return "Saved $firstName for user $userId"
    }
}