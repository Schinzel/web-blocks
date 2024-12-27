package io.schinzel.sample.pages.settings.name

import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class NameWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}