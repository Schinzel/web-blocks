package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.routes.IApiRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Api
class ApiRouteThatThrowsError : IApiRoute {
    override suspend fun getResponse(): IJsonResponse = throw RuntimeException("Something went wrong!!")
}
