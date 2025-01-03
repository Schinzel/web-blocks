package io.schinzel.page_elements.samples.component.api.v1

import io.schinzel.page_elements.web.response_handlers.IApiEndpointResponseHandler

@Suppress("unused")
class UserInformationEndpoint(private val userId: String = "") : IApiEndpointResponseHandler {
    override fun getResponse(): Any {
        if (userId.isNotEmpty()) {
            return UserInformation(userId, "Jane Doe", 24)
        }
        return UserInformation("", "", 0)
    }

    data class UserInformation(val userId: String, val name: String, val age: Int)
}

