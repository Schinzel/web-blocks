package io.schinzel._old_samples.component.api.v1

import io.schinzel.page_elements.web.routes.IApiRoute

@Suppress("unused")
class UserInformationEndpoint(private val userId: String = "") : IApiRoute {
    override fun getResponse(): Any {
        if (userId.isNotEmpty()) {
            return UserInformation(userId, "Jane Doe", 24)
        }
        return UserInformation("", "", 0)
    }

    data class UserInformation(val userId: String, val name: String, val age: Int)
}

