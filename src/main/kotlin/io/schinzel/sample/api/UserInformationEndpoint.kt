package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.jsonSuccess
import io.schinzel.web_blocks.web.routes.IApiRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Suppress("unused")
@Api
class UserInformationEndpoint(
    private val userId: String = "",
) : IApiRoute {
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
