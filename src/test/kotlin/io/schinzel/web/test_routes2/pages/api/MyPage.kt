package io.schinzel.web.test_routes2.pages.api

import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyPage : IPageResponseHandler {
    override fun getResponse(): String {
        return ""
    }
}