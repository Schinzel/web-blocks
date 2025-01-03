package io.schinzel.web.test_routes4.page_api.static

import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyPage : IPageResponseHandler {
    override fun getResponse(): String {
        return ""
    }
}