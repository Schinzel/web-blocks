package io.schinzel.web_blocks.web.test_routes2.pages.api

import io.schinzel.web_blocks.web.routes.IPageRoute
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html

@Suppress("unused")
class MyPage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return html("")
    }
}