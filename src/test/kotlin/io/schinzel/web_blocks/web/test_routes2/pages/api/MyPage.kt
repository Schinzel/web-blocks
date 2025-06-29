package io.schinzel.web_blocks.web.test_routes2.pages.api

import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class MyPage : IPageRoute {
    override suspend fun getResponse(): String {
        return ""
    }
}