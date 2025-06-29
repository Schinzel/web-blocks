package io.schinzel.web_blocks.web.test_routes4.page_api.static

import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class MyPage : IPageRoute {
    override suspend fun getResponse(): String {
        return ""
    }
}