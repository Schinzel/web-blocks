package io.schinzel.samples.component.pages.settings.name

import io.schinzel.web.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class NameWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}