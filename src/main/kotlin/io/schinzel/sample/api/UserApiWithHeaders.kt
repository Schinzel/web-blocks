package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.JsonResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IApiRoute

/**
 * The purpose of this class is to demonstrate using JsonResponseBuilder
 * to add custom headers and status codes to API responses.
 *
 * Written by Claude Sonnet 4
 */
@Suppress("unused")
class UserApiWithHeaders(
    val userId: Int = 123,
) : IApiRoute {
    override suspend fun getResponse(): WebBlockResponse {
        val user = fetchUser(userId)
        return JsonResponse
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
