package io.schinzel.sample.pages.sub_dir.sub_dir_2

import io.schinzel.page_elements.web_response.IWebPage

@Suppress("unused")
class MyWebPage: IWebPage {
    override fun getResponse(): String {
        return "<h1>Sub dir page</h1>"
    }
}