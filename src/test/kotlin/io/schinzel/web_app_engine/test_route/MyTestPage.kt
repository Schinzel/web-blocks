package io.schinzel.web_app_engine.test_route

import io.schinzel.web_app_engine.route_registry.processors.IWebPage

class MyTestPage : IWebPage {
    override fun getResponse(): String {
        return "MyTestPage"
    }
}