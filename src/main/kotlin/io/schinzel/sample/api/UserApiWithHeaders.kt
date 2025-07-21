package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.JsonSuccessResponse
import io.schinzel.web_blocks.web.routes.IJsonRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

/**
 * The purpose of this class is to demonstrate using JsonResponseBuilder
 * to add custom headers and status codes to API responses.
 *
 * Written by Claude Sonnet 4
 */
@Suppress("unused")
@Api
class UserApiWithHeaders(
    val userId: Int = 123,
) : IJsonRoute {
    override suspend fun getResponse(): IJsonResponse {
        val user = fetchUser(userId)
        return JsonSuccessResponse
            .builder()
            .setData(user)
            .setStatus(200)
            .addHeader("X-Total-Count", "1")
            .addHeader("X-Rate-Limit-Remaining", "99")
            .addHeader("X-User-Type", user.type)
            .build()
    }

    private fun fetchUser(id: Int): User =
        User(
            id = id,
            name = "Sample User $id",
            email = "user$id@example.com",
            type = "premium",
        )

    data class User(
        val id: Int,
        val name: String,
        val email: String,
        val type: String,
    )
}
