package io.schinzel.samples.component.pages.sub_dir.sub_dir_2

import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Sub dir page</h1>"
    }
}