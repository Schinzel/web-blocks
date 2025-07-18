package io.schinzel.web_blocks.web.test_routes3.pages.static

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class MyPage : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse = html("")
}
