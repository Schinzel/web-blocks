package io.schinzel.web_blocks.web.test_routes4.page_api.static

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Suppress("unused")
@Page
class MyPage : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse = html("")
}
