package io.schinzel.page_elements.web.test_routes3.pages.static

import io.schinzel.page_elements.web.routes.IPageRoute

@Suppress("unused")
class MyPage : IPageRoute {
    override suspend fun getResponse(): String {
        return ""
    }
}