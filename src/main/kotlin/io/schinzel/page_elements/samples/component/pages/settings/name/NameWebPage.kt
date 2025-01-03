package io.schinzel.page_elements.samples.component.pages.settings.name

import io.schinzel.page_elements.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class NameWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}