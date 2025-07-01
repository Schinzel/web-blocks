package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.json
import io.schinzel.web_blocks.web.routes.IApiRoute

@Suppress("unused")
class UserInformationEndpoint(private val userId: String = "") : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        if (userId.isNotEmpty()) {
            return json(UserInformation(userId, "Jane Doe", 24))
        }
        return json(UserInformation("", "", 0))
    }

    data class UserInformation(val userId: String, val name: String, val age: Int)
}

