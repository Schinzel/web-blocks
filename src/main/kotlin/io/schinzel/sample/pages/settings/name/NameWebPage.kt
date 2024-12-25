package io.schinzel.sample.pages.settings.name

import io.schinzel.web_app_engine.IWebPage

@Suppress("unused")
class NameWebPage: IWebPage {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}