package io.schinzel.web_blocks.web.test_routes2.pages.api

import io.schinzel.web_blocks.web.response.IWebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Suppress("unused")
@Page
class MyPage : IWebBlockRoute {
    override suspend fun getResponse(): IWebBlockResponse = html("")
}
