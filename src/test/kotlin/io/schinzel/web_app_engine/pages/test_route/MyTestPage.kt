package io.schinzel.web_app_engine.pages.test_route

import io.schinzel.web_app_engine.route_registry.processors.IWebPage

@Suppress("unused")
class MyTestPage : IWebPage {
    override fun getResponse(): String {
        return "MyTestPage"
    }
}