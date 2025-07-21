package io.schinzel.sample.api

import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.routes.IJsonRoute
import io.schinzel.web_blocks.web.routes.annotations.Api

@Api
class ApiThatThrowsError : IJsonRoute {
    override suspend fun getResponse(): IJsonResponse = throw RuntimeException("Something went wrong!!")
}
