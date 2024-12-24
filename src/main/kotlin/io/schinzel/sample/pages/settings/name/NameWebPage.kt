package io.schinzel.sample.pages.settings.name

import io.schinzel.web_app_engine.web_response.IWebPage

@Suppress("unused")
class NameWebPage: IWebPage {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}