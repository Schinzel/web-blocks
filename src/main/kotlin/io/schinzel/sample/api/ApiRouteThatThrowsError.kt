package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi

@WebBlockApi
@Suppress("unused")
class ApiRouteThatThrowsError : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = throw RuntimeException("Something went wrong!!")
}
