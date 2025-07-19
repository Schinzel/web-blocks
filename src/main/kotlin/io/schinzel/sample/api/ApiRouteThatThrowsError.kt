package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Api
class ApiRouteThatThrowsError : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = throw RuntimeException("Something went wrong!!")
}
