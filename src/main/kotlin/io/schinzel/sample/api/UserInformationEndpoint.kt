package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.IJsonRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Suppress("unused")
@Api
class UserInformationEndpoint(
    private val userId: String = "",
) : IJsonRoute {
    override suspend fun getResponse(): IJsonResponse {
        if (userId.isNotEmpty()) {
            return jsonSuccess(UserInformation(userId, "Jane Doe", 24))
        }
        return jsonSuccess(UserInformation("", "", 0))
    }

    data class UserInformation(
        val userId: String,
        val name: String,
        val age: Int,
    )
}
