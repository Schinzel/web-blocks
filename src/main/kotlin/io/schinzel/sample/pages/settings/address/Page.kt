package io.schinzel.sample.pages.settings.address

import io.schinzel.page_elements.IPage

@Suppress("unused")
class Page: IPage {
    override fun getHtml(): String {
        return "<h1>Address</h1>"
    }
}