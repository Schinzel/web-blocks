package io.schinzel.page_elements.samples.component.pages.sub_dir.sub_dir_2

import io.schinzel.page_elements.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Sub dir page</h1>"
    }
}