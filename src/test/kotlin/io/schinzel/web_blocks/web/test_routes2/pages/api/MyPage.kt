package io.schinzel.web_blocks.web.test_routes2.pages.api

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Suppress("unused")
@Page
class MyPage : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse = html("")
}
