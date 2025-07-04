package io.schinzel.sample.api

import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class UserInformationEndpoint(private val userId: String = "") : IApiRoute {
    override suspend fun getResponse(): Any {
        if (userId.isNotEmpty()) {
            return UserInformation(userId, "Jane Doe", 24)
        }
        return UserInformation("", "", 0)
    }

    data class UserInformation(val userId: String, val name: String, val age: Int)
}

